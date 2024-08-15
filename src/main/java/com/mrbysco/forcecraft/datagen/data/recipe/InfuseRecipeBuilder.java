package com.mrbysco.forcecraft.datagen.data.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.blockentities.InfuserModifierType;
import com.mrbysco.forcecraft.items.infuser.UpgradeBookTier;
import com.mrbysco.forcecraft.registry.ForceRecipeSerializers;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.CraftingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class InfuseRecipeBuilder extends CraftingRecipeBuilder implements RecipeBuilder {
	private final Ingredient center;
	private final Ingredient ingredient;
	private final UpgradeBookTier tier;
	private final int time;
	private InfuserModifierType resultModifier = InfuserModifierType.ITEM;
	private ItemStack output = ItemStack.EMPTY;

	public InfuseRecipeBuilder(Ingredient center, Ingredient ingredient, UpgradeBookTier tier, int time) {
		this.ingredient = ingredient;
		this.center = center;
		this.tier = tier;
		this.time = time;
	}

	public static InfuseRecipeBuilder infuse(Ingredient ingredient, Ingredient center, UpgradeBookTier tier, int time) {
		return new InfuseRecipeBuilder(center, ingredient, tier, time);
	}

	public InfuseRecipeBuilder modifierType(InfuserModifierType type) {
		this.resultModifier = type;
		return this;
	}

	public InfuseRecipeBuilder output(ItemLike outputItem) {
		this.output = new ItemStack(outputItem.asItem());
		this.resultModifier = InfuserModifierType.ITEM;
		return this;
	}

	public InfuseRecipeBuilder output(ItemLike outputItem, int count) {
		this.output = new ItemStack(outputItem, count);
		this.resultModifier = InfuserModifierType.ITEM;
		return this;
	}

	public InfuseRecipeBuilder output(ItemStack outputStack) {
		this.output = outputStack;
		this.resultModifier = InfuserModifierType.ITEM;
		return this;
	}

	@Override
	public RecipeBuilder unlockedBy(String id, CriterionTriggerInstance triggerInstance) {
		return this;
	}

	@Override
	public RecipeBuilder group(@Nullable String pGroupName) {
		return this;
	}

	@Override
	public Item getResult() {
		return this.output.getItem();
	}

	@Override
	public void save(Consumer<FinishedRecipe> recipeConsumer, ResourceLocation id) {
		recipeConsumer.accept(new InfuseRecipeBuilder.Result(id, this.center, this.ingredient, this.tier, this.time,
				this.resultModifier, this.output));
	}

	public static class Result extends CraftingRecipeBuilder.CraftingResult {
		private final ResourceLocation id;

		private final Ingredient center;
		private final Ingredient ingredient;
		private final UpgradeBookTier tier;
		private final int time;
		private final InfuserModifierType resultModifier;
		private final ItemStack output;

		public Result(ResourceLocation id, Ingredient center, Ingredient ingredient, UpgradeBookTier tier,
		              int time, InfuserModifierType resultModifier, ItemStack output) {
			super(CraftingBookCategory.MISC);
			this.id = id;
			this.center = center;
			this.ingredient = ingredient;
			this.tier = tier;
			this.time = time;
			this.resultModifier = resultModifier;
			this.output = output;
		}

		public void serializeRecipeData(JsonObject json) {
			json.add("center", center.toJson());
			json.add("ingredient", ingredient.toJson());
			json.addProperty("result", Reference.MOD_ID + ":" + resultModifier.name().toLowerCase());
			if (resultModifier == InfuserModifierType.ITEM) {
				json.add("output", serializeItemStack(output));
			}
			json.addProperty("tier", tier.ordinal());
			json.addProperty("time", time);
		}

		public RecipeSerializer<?> getType() {
			return ForceRecipeSerializers.INFUSER_SERIALIZER.get();
		}

		public ResourceLocation getId() {
			return this.id;
		}

		@Nullable
		public JsonObject serializeAdvancement() {
			return null;
		}

		@Nullable
		public ResourceLocation getAdvancementId() {
			return null;
		}

		static JsonElement serializeItemStack(@NotNull ItemStack stack) {
			JsonObject json = new JsonObject();
			json.addProperty("item", ForgeRegistries.ITEMS.getKey(stack.getItem()).toString());
			if (stack.getCount() > 1) {
				json.addProperty("count", stack.getCount());
			}
			if (stack.hasTag()) {
				json.addProperty("nbt", stack.getTag().toString());
			}
			return json;
		}
	}
}
