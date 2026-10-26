package com.mrbysco.forcecraft.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.forcecraft.blockentity.infuser.InfuserBlockEntity;
import com.mrbysco.forcecraft.blockentity.infuser.InfuserUpgrade;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceRecipes;
import com.mrbysco.forcecraft.upgrade.UpgradeBookData;
import com.mrbysco.forcecraft.upgrade.UpgradeBookTier;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class InfuseRecipe implements Recipe<RecipeInput> {
	public static final MapCodec<InfuseRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
			i -> i.group(
					Ingredient.CODEC.fieldOf("center").forGetter(o -> o.center),
					Ingredient.CODEC.fieldOf("ingredient").forGetter(o -> o.ingredient),
					InfuserUpgrade.CODEC.fieldOf("upgrade").forGetter(o -> o.upgrade),
					UpgradeBookTier.CODEC.fieldOf("tier").forGetter(o -> o.tier),
					ItemStackTemplate.CODEC.optionalFieldOf("output").forGetter(o -> o.output),
					Codec.INT.fieldOf("points").forGetter(o -> o.points),
					Codec.INT.fieldOf("time").forGetter(o -> o.time),
					Codec.STRING.optionalFieldOf("group", "").forGetter(o -> o.group)
			).apply(i, InfuseRecipe::new)
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, InfuseRecipe> STREAM_CODEC = StreamCodec.composite(
			Ingredient.CONTENTS_STREAM_CODEC,
			o -> o.center,
			Ingredient.CONTENTS_STREAM_CODEC,
			o -> o.ingredient,
			ByteBufCodecs.fromCodecWithRegistries(InfuserUpgrade.CODEC),
			o -> o.upgrade,
			ByteBufCodecs.idMapper(id -> UpgradeBookTier.values()[id], UpgradeBookTier::ordinal),
			o -> o.tier,
			ByteBufCodecs.optional(ItemStackTemplate.STREAM_CODEC),
			o -> o.output,
			ByteBufCodecs.VAR_INT,
			o -> o.time,
			ByteBufCodecs.VAR_INT,
			o -> o.time,
			ByteBufCodecs.STRING_UTF8,
			o -> o.group,
			InfuseRecipe::new
	);

	public static final RecipeSerializer<InfuseRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	private final Ingredient center;
	private final Ingredient ingredient;
	private final InfuserUpgrade upgrade;
	private final UpgradeBookTier tier;
	private final Optional<ItemStackTemplate> output;
	private final int points;
	private final int time;
	private final String group;

	public InfuseRecipe(Ingredient center, Ingredient ingredient, InfuserUpgrade upgrade, UpgradeBookTier tier,
	                    Optional<ItemStackTemplate> output, int points, int time, String group) {
		this.center = center;
		this.ingredient = ingredient;
		this.upgrade = upgrade;
		this.tier = tier;
		this.output = output;
		this.points = points;
		this.time = time;
		this.group = group;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public boolean showNotification() {
		return false;
	}

	@Override
	public String group() {
		return group;
	}

	@Override
	public boolean matches(RecipeInput inv, Level level) {
		ItemStack tool = inv.getItem(InfuserBlockEntity.SLOT_TOOL);
		UpgradeBookData book = inv.getItem(InfuserBlockEntity.SLOT_BOOK)
				.getOrDefault(ForceComponents.UPGRADE_BOOK, UpgradeBookData.DEFAULT);
		for (int i = 0; i < InfuserBlockEntity.SLOT_TOOL; i++) {
			if (matchesModifier(tool, inv.getItem(i), book, false)) return true;
		}
		return false;
	}

	public boolean matchesModifier(ItemStack tool, ItemStack modifier, UpgradeBookData book, boolean ignoreInfused) {
		return tier.ordinal() <= book.tier().ordinal()
				&& matchesTool(tool, ignoreInfused)
				&& ingredient.test(modifier)
				&& upgrade.canApply(tool, tier);
	}

	public boolean matchesTool(ItemStack tool, boolean ignoreInfused) {
		if (!center.test(tool)) return false;
		return ignoreInfused || !tool.has(ForceComponents.FORCE_INFUSED);
	}

	@Override
	public ItemStack assemble(RecipeInput inv) {
		return getOutput();
	}

	public boolean hasOutput() {
		return output.isPresent();
	}

	@Override
	public RecipeType<? extends Recipe<RecipeInput>> getType() {
		return ForceRecipes.INFUSE.get();
	}

	@Override
	public RecipeSerializer<? extends Recipe<RecipeInput>> getSerializer() {
		return ForceRecipes.INFUSE_SERIALIZER.get();
	}

	@Override
	public PlacementInfo placementInfo() {
		return PlacementInfo.NOT_PLACEABLE;
	}

	@Override
	public RecipeBookCategory recipeBookCategory() {
		return RecipeBookCategories.CRAFTING_MISC;
	}

	public Ingredient getCenter() {
		return center;
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public InfuserUpgrade getUpgrade() {
		return upgrade;
	}

	public UpgradeBookTier getTier() {
		return tier;
	}

	public ItemStack getOutput() {
		return output.map(ItemStackTemplate::create).orElse(ItemStack.EMPTY);
	}

	public int getPoints() {
		return points;
	}

	public int getTime() {
		return time;
	}

	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> ingredients = NonNullList.create();
		ingredients.add(center);
		ingredients.add(ingredient);
		return ingredients;
	}
}