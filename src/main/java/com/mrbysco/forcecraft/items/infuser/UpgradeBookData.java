package com.mrbysco.forcecraft.items.infuser;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.blockentities.InfuserBlockEntity;
import com.mrbysco.forcecraft.components.ForceComponents;
import com.mrbysco.forcecraft.recipe.InfuseRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;

public record UpgradeBookData(UpgradeBookTier tier, List<Identifier> recipesUsed,
                              int points, String progressCache) {
	public static final UpgradeBookData DEFAULT = new UpgradeBookData(UpgradeBookTier.ZERO, new ArrayList<>(), 0, "");
	public static final Codec<UpgradeBookData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
					UpgradeBookTier.CODEC.fieldOf("tier").forGetter(UpgradeBookData::tier),
					Codec.list(Identifier.CODEC).fieldOf("recipesUsed").forGetter(UpgradeBookData::recipesUsed),
					Codec.INT.fieldOf("points").forGetter(UpgradeBookData::points),
					Codec.STRING.fieldOf("progressCache").forGetter(UpgradeBookData::progressCache))
			.apply(inst, UpgradeBookData::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, UpgradeBookData> STREAM_CODEC = StreamCodec.of(
			UpgradeBookData::toNetwork, UpgradeBookData::fromNetwork
	);

	private static UpgradeBookData fromNetwork(RegistryFriendlyByteBuf byteBuf) {
		UpgradeBookTier tier = UpgradeBookTier.values()[byteBuf.readVarInt()];
		List<Identifier> recipesUsed = byteBuf.readList(Identifier.STREAM_CODEC);
		int points = byteBuf.readVarInt();
		String progressCache = byteBuf.readUtf(32767);
		return new UpgradeBookData(tier, recipesUsed, points, progressCache);
	}

	private static void toNetwork(RegistryFriendlyByteBuf byteBuf, UpgradeBookData playerCompassData) {
		byteBuf.writeVarInt(playerCompassData.tier().ordinal());
		byteBuf.writeCollection(playerCompassData.recipesUsed(), Identifier.STREAM_CODEC);
		byteBuf.writeVarInt(playerCompassData.points());
		byteBuf.writeUtf(playerCompassData.progressCache());
	}

	// how many we need for next tier increment
	public int nextTier(ItemStack stack) {
		UpgradeBookData data = stack.getOrDefault(ForceComponents.UPGRADE_BOOK, UpgradeBookData.DEFAULT);
		if (data.tier() == UpgradeBookTier.FINAL) {
			return 0;
		}
		return Math.max(0, data.tier().pointsForLevelup() - points);
	}

	public void onRecipeApply(RecipeHolder<InfuseRecipe> recipeHolder, ItemStack bookStack) {
		UpgradeBookData data = bookStack.getOrDefault(ForceComponents.UPGRADE_BOOK, UpgradeBookData.DEFAULT);
		if (InfuserBlockEntity.LEVEL_RECIPE_LIST.get(data.tier.asInt()).contains(recipeHolder.id())) {
			List<Identifier> recipesUsed = new ArrayList<>(data.recipesUsed());
			if (!recipesUsed.contains(recipeHolder.id())) {
				recipesUsed.add(recipeHolder.id().identifier());
				bookStack.set(ForceComponents.UPGRADE_BOOK,
						new UpgradeBookData(
								data.tier,
								recipesUsed,
								data.points,
								data.progressCache
						)
				);
			}
		}
		tryLevelUp(bookStack);
	}

	public static void incrementPoints(ItemStack stack, int incoming) {
		UpgradeBookData data = stack.getOrDefault(ForceComponents.UPGRADE_BOOK, UpgradeBookData.DEFAULT);
		stack.set(ForceComponents.UPGRADE_BOOK,
				new UpgradeBookData(
						data.tier,
						data.recipesUsed,
						data.points + incoming,
						data.progressCache
				)
		);
		tryLevelUp(stack);
	}

	// update level and points if levelup is possible
	private static void tryLevelUp(ItemStack stack) {
		UpgradeBookData data = stack.getOrDefault(ForceComponents.UPGRADE_BOOK, UpgradeBookData.DEFAULT);
		if (canLevelUp(stack)) {
			// then go
			int newPoints = data.points() - data.tier().pointsForLevelup();
			setTierAndPoints(stack, data.tier().incrementTier(), newPoints);

			updateCache(stack);
		}
	}

	private static void updateCache(ItemStack stack) {
		UpgradeBookData data = stack.getOrDefault(ForceComponents.UPGRADE_BOOK, UpgradeBookData.DEFAULT);
		//Update tooltip
		List<Identifier> thisTier = InfuserBlockEntity.LEVEL_RECIPE_LIST.get(data.tier().ordinal());
		List<Identifier> usedRecipes = data.recipesUsed();
		ForceCraft.LOGGER.info("thisTier {}", thisTier);
		ForceCraft.LOGGER.info("RecipesUsed {}", usedRecipes);
		int recipesThisTier = (usedRecipes == null) ? 0 : usedRecipes.size();
		if (!InfuserBlockEntity.LEVEL_RECIPE_LIST.isEmpty()) {
			int totalThisTier = thisTier.size();
			stack.set(ForceComponents.UPGRADE_BOOK,
					new UpgradeBookData(
							data.tier,
							data.recipesUsed,
							data.points,
							recipesThisTier + "/" + totalThisTier
					)
			);
		}
	}

	private static boolean canLevelUp(ItemStack stack) {
		UpgradeBookData data = stack.getOrDefault(ForceComponents.UPGRADE_BOOK, UpgradeBookData.DEFAULT);
		// check more
		List<Identifier> thisTier = data.recipesUsed();
		int recipesThisTier = (thisTier == null) ? 0 : thisTier.size();
		int totalThisTier = InfuserBlockEntity.LEVEL_RECIPE_LIST.get(data.tier().ordinal()).size();

//		ForceCraft.LOGGER.debug("can lvlup?  ?  " + recipesThisTier + " >= " + totalThisTier);

		updateCache(stack);

		if (data.points() < data.tier().pointsForLevelup() || data.tier() == UpgradeBookTier.FINAL) {
			return false;
		}

		// if this tier has total=5 recipes, I need to craft at least 5 unique recipes
		// this tier
		return recipesThisTier >= totalThisTier;
	}

	private static void setTierAndPoints(ItemStack stack, UpgradeBookTier tier, int points) {
		UpgradeBookData data = stack.getOrDefault(ForceComponents.UPGRADE_BOOK, UpgradeBookData.DEFAULT);
		stack.set(ForceComponents.UPGRADE_BOOK,
				new UpgradeBookData(
						tier,
						new ArrayList<>(), //Clear list on tier change
						points,
						data.progressCache
				)
		);
	}

	public static void setTier(ItemStack stack, UpgradeBookTier tier) {
		UpgradeBookData data = stack.getOrDefault(ForceComponents.UPGRADE_BOOK, UpgradeBookData.DEFAULT);
		stack.set(ForceComponents.UPGRADE_BOOK,
				new UpgradeBookData(
						tier,
						new ArrayList<>(), //Clear list on tier change
						data.points,
						data.progressCache
				)
		);
	}
}