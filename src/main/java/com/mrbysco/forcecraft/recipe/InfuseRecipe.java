package com.mrbysco.forcecraft.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.forcecraft.attachment.storage.PackStackHandler;
import com.mrbysco.forcecraft.blockentities.InfuserBlockEntity;
import com.mrbysco.forcecraft.blockentities.InfuserModifierType;
import com.mrbysco.forcecraft.items.infuser.UpgradeBookData;
import com.mrbysco.forcecraft.items.infuser.UpgradeBookTier;
import com.mrbysco.forcecraft.registry.ForceRecipeSerializers;
import com.mrbysco.forcecraft.registry.ForceRecipes;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

public class InfuseRecipe implements Recipe<InfuserBlockEntity> {
	private static final int MAX_SLOTS = 8;
	public Ingredient ingredient = Ingredient.EMPTY;
	public InfuserModifierType resultModifier;
	ItemStack output = ItemStack.EMPTY;
	private UpgradeBookTier tier;
	private Ingredient center;
	private int time;

	public InfuseRecipe(Ingredient center, Ingredient input, InfuserModifierType resultType, UpgradeBookTier tier, ItemStack outputStack, int time) {
		this.ingredient = input;
		this.center = center;
		this.output = outputStack;
		this.resultModifier = resultType;
		this.tier = tier;
		this.time = time;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	@Override
	public boolean matches(InfuserBlockEntity inv, Level level) {
		for (int i = 0; i < inv.handler.getSlots(); i++) {
			ItemStack stack = inv.handler.getStackInSlot(i);
			if (i < InfuserBlockEntity.SLOT_TOOL) {
				return matchesModifier(inv, stack, false);
			}
		}
		return false;
	}

	public boolean matchesModifier(InfuserBlockEntity inv, ItemStack modifier, boolean ignoreInfused) {
		//Has the correct tier
		UpgradeBookData bd = new UpgradeBookData(inv.getBookInSlot());
		int bookTier = bd.getTier().ordinal();
		if (getTier().ordinal() > bookTier) {
			return false;
		}

		//Does the center match
		ItemStack centerStack = inv.handler.getStackInSlot(InfuserBlockEntity.SLOT_TOOL);
		boolean toolMatches = matchesTool(centerStack, ignoreInfused);
		boolean modifierMatches = matchesModifier(centerStack, modifier);

//		ForceCraft.LOGGER.info(cent + " does  match center for "+this.id);


		return toolMatches && modifierMatches;
	}

	public boolean matchesModifier(InfuserBlockEntity inv, ItemStack modifierStack) {
		ItemStack centerStack = inv.handler.getStackInSlot(InfuserBlockEntity.SLOT_TOOL);
		return matchesModifier(centerStack, modifierStack);
	}

	public boolean matchesModifier(ItemStack centerStack, ItemStack modifierStack) {
		if (modifierStack.getItem() == ForceRegistry.FORCE_PACK_UPGRADE.get()) {
			IItemHandler handler = centerStack.getCapability(Capabilities.ItemHandler.ITEM);
			if (handler instanceof PackStackHandler) {
				if (((PackStackHandler) handler).getUpgrades() != getTier().ordinal() - 2) {
					return false;
				}
			}
		}

		return this.ingredient.test(modifierStack);
	}

	public boolean matchesTool(ItemStack toolStack, boolean ignoreInfused) {
		if (!this.center.test(toolStack)) {
			// center doesn't match this recipe. move over
			return false;
		}
		if (!ignoreInfused) {
			//Ignore if the tool is infused in case of infusing for the first time
			return !toolStack.hasTag() || !toolStack.getTag().getBoolean("ForceInfused");
		}
		return true;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return height == 1 && width < MAX_SLOTS;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return output;
	}

	public boolean hasOutput() {
		return !output.isEmpty(); //should also be for modifier == ITEM
	}

	@Override
	public ItemStack assemble(InfuserBlockEntity inv, RegistryAccess registryAccess) {
		return getResultItem(registryAccess);
	}

	@Override
	public RecipeType<?> getType() {
		return ForceRecipes.INFUSER_TYPE.get();
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public Ingredient getCenter() {
		return center;
	}

	public InfuserModifierType getModifier() {
		return resultModifier;
	}

	public void setModifier(InfuserModifierType modifier) {
		this.resultModifier = modifier;
	}

	public UpgradeBookTier getTier() {
		return tier;
	}

	public void setTier(UpgradeBookTier tier) {
		this.tier = tier;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> ingredients = NonNullList.create();
		ingredients.add(center);
		ingredients.add(ingredient);
		return ingredients;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ForceRecipeSerializers.INFUSER_SERIALIZER.get();
	}

	public static class SerializeInfuserRecipe implements RecipeSerializer<InfuseRecipe> {
		private static final Codec<InfuseRecipe> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
								Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
								Ingredient.CODEC_NONEMPTY.fieldOf("center").forGetter(recipe -> recipe.center),
								InfuserModifierType.CODEC.fieldOf("resultType").forGetter(recipe -> recipe.resultModifier),
								UpgradeBookTier.CODEC.fieldOf("tier").forGetter(recipe -> recipe.tier),
								ExtraCodecs.strictOptionalField(ItemStack.ITEM_WITH_COUNT_CODEC, "output", ItemStack.EMPTY).forGetter(recipe -> recipe.output),
								Codec.INT.fieldOf("time").forGetter(recipe -> recipe.time)
						)
						.apply(instance, InfuseRecipe::new)
		);

		@Override
		public Codec<InfuseRecipe> codec() {
			return CODEC;
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, InfuseRecipe recipe) {
			recipe.center.toNetwork(buffer);
			recipe.ingredient.toNetwork(buffer);
			buffer.writeEnum(recipe.resultModifier);
			buffer.writeEnum(recipe.tier);
			buffer.writeItem(recipe.getResultItem(null));
			buffer.writeInt(recipe.getTime());
		}

		@Override
		public InfuseRecipe fromNetwork(FriendlyByteBuf buffer) {
			Ingredient center = Ingredient.fromNetwork(buffer);
			Ingredient ing = Ingredient.fromNetwork(buffer);
			InfuserModifierType infuserType = buffer.readEnum(InfuserModifierType.class);
			UpgradeBookTier tier = buffer.readEnum(UpgradeBookTier.class);
			return new InfuseRecipe(center, ing, infuserType, tier, buffer.readItem(), buffer.readInt());
		}
	}
}
