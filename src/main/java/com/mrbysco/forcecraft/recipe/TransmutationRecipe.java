package com.mrbysco.forcecraft.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceRecipes;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.util.ForceUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ExperienceBottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.NormalCraftingRecipe;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransmutationRecipe extends NormalCraftingRecipe {
	public static final MapCodec<TransmutationRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
			i -> i.group(
							Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
							CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
							ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result),
							com.mojang.serialization.Codec.lazyInitialized(() -> Ingredient.CODEC.listOf(1, 3 * 3))
									.fieldOf("ingredients").forGetter(o -> o.ingredients)
					)
					.apply(i, TransmutationRecipe::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, TransmutationRecipe> STREAM_CODEC = StreamCodec.composite(
			Recipe.CommonInfo.STREAM_CODEC,
			o -> o.commonInfo,
			CraftingRecipe.CraftingBookInfo.STREAM_CODEC,
			o -> o.bookInfo,
			ItemStackTemplate.STREAM_CODEC,
			o -> o.result,
			Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
			o -> o.ingredients,
			TransmutationRecipe::new
	);

	public static final RecipeSerializer<TransmutationRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
	private final ItemStackTemplate result;
	private final List<Ingredient> ingredients;

	public TransmutationRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo, ItemStackTemplate result, List<Ingredient> ingredients) {
		super(commonInfo, bookInfo);
		this.result = result;
		this.ingredients = ingredients;
	}

	public List<Ingredient> getIngredients() {
		return this.ingredients;
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		List<ItemStack> inputs = new ArrayList<>(input.ingredientCount());
		for (ItemStack stack : input.items()) {
			if (stack.isEmpty()) continue;

			if (stack.is(ForceRegistry.EXPERIENCE_TOME)) {
				if (stack.has(ForceComponents.TOME_EXPERIENCE) // Needs 100 experience stored
						&& stack.getOrDefault(ForceComponents.TOME_EXPERIENCE, 0) < 100) {
					return false;
				}
			} else if (!(stack.is(ForceRegistry.FORCE_ROD)) && stack.isDamaged()) {
				return false;
			}
			inputs.add(stack);
		}
		return inputs.size() == this.ingredients.size()
				&& RecipeMatcher.findMatches(inputs, this.ingredients) != null;
	}

	@Override
	public ItemStack assemble(CraftingInput input) {
		ItemStack resultStack = this.result.create();
		for (ItemStack stack : input.items()) {
			if (stack.is(ForceRegistry.EXPERIENCE_TOME) && stack.has(ForceComponents.TOME_EXPERIENCE)) {
				int experience = stack.getOrDefault(ForceComponents.TOME_EXPERIENCE, 0);
				resultStack.setCount(Math.min(experience / 100, 64));
			}
			if (stack.is(Items.ENCHANTED_BOOK) && resultStack.getItem() instanceof ExperienceBottleItem) {
				ItemEnchantments enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
				if (!enchantments.isEmpty()) {
					int amount = 0;
					for (Map.Entry<Holder<Enchantment>, Integer> entry : enchantments.entrySet()) {
						amount += entry.getValue();
					}
					resultStack.setCount(Mth.clamp(amount, 1, 64));
				}
			}
		}
		return resultStack;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
		NonNullList<ItemStack> remaining = super.getRemainingItems(input);
		for (int i = 0; i < input.size(); i++) {
			ItemStack stack = input.getItem(i);
			if (stack.is(ForceRegistry.FORCE_ROD)) {
				ItemStack copy = stack.copyWithCount(1);
				int damage = ForceUtil.damageForceItem(copy, 1);
				if (copy.getDamageValue() + damage >= copy.getMaxDamage()) {
					remaining.set(i, ItemStack.EMPTY);
				} else {
					if (damage > 0) copy.setDamageValue(copy.getDamageValue() + damage);
					remaining.set(i, copy);
				}
			} else if (stack.is(ForceRegistry.EXPERIENCE_TOME)) {
				ItemStack copy = stack.copyWithCount(1);
				int experience = stack.getOrDefault(ForceComponents.TOME_EXPERIENCE, 0);
				copy.set(ForceComponents.TOME_EXPERIENCE, experience - Math.min(experience / 100, 64) * 100);
				remaining.set(i, copy);
			}
		}
		return remaining;
	}

	@Override
	public RecipeSerializer<? extends NormalCraftingRecipe> getSerializer() {
		return ForceRecipes.TRANSMUTE_SERIALIZER.get();
	}

	@Override
	protected PlacementInfo createPlacementInfo() {
		return PlacementInfo.create(this.ingredients);
	}

	@Override
	public List<RecipeDisplay> display() {
		return List.of(
				new ShapelessCraftingRecipeDisplay(
						this.ingredients.stream().map(Ingredient::display).toList(),
						new SlotDisplay.ItemStackSlotDisplay(this.result),
						new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)
				)
		);
	}
}
