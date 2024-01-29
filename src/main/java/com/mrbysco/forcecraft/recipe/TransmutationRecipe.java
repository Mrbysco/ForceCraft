package com.mrbysco.forcecraft.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.forcecraft.items.ExperienceTomeItem;
import com.mrbysco.forcecraft.items.tools.ForceRodItem;
import com.mrbysco.forcecraft.registry.ForceRecipeSerializers;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ExperienceBottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class TransmutationRecipe implements CraftingRecipe {
	private final String group;
	private final ItemStack result;
	private final NonNullList<Ingredient> ingredients;
	private final boolean isSimple;

	public TransmutationRecipe(String groupIn, ItemStack result, NonNullList<Ingredient> ingredients) {
		this.group = groupIn;
		this.result = result;
		this.ingredients = ingredients;
		this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ForceRecipeSerializers.TRANSMUTATION_SERIALIZER.get();
	}

	@Override
	public String getGroup() {
		return this.group;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return this.result;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.ingredients;
	}

	@Override
	public boolean isSpecial() {
		return false;
	}

	@Override
	public boolean matches(CraftingContainer inv, Level level) {
		StackedContents stackedContents = new StackedContents();
		java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
		int stacks = 0;

		for (int i = 0; i < inv.getContainerSize(); ++i) {
			ItemStack itemstack = inv.getItem(i);
			if (!itemstack.isEmpty()) { //TEMP fix!
				++stacks;
				if ((itemstack.getItem() instanceof ForceRodItem)) {
					if (isSimple)
						stackedContents.accountStack(itemstack, 1);
					else inputs.add(itemstack);
				} else {
					if (itemstack.getItem() instanceof ExperienceTomeItem) {
						if (itemstack.hasTag() && itemstack.getTag().getInt("Experience") < 100) {
							return false;
						} else {
							ItemStack experienceTome = new ItemStack(ForceRegistry.EXPERIENCE_TOME.get());
							CompoundTag tag = experienceTome.getOrCreateTag();
							tag.putInt("Experience", 100);
							experienceTome.setTag(tag);
							if (isSimple)
								stackedContents.accountStack(experienceTome, 1);
							else inputs.add(experienceTome);
						}
					} else {
						if (!itemstack.isDamaged()) {
							if (isSimple)
								stackedContents.accountStack(itemstack, 1);
							else inputs.add(itemstack);
						}
					}
				}
			}
		}

		return stacks == this.ingredients.size() && (isSimple ? stackedContents.canCraft(this, (IntList) null) : RecipeMatcher.findMatches(inputs, this.ingredients) != null);
	}

	@Override
	public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
		ItemStack resultStack = getResultItem(registryAccess).copy();
		for (int j = 0; j < inv.getContainerSize(); ++j) {
			ItemStack itemstack = inv.getItem(j);
			if ((itemstack.getItem() instanceof ExperienceTomeItem)) {
				CompoundTag tag = itemstack.getOrCreateTag();
				int experience = tag.getInt("Experience");
				int count = Math.min((int) ((float) experience / 100f), 64);
				resultStack.setCount(count);
			}
			if (itemstack.getItem() instanceof EnchantedBookItem && resultStack.getItem() instanceof ExperienceBottleItem) {
				Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(itemstack);
				if (!enchantmentMap.isEmpty()) {
					int amount = 0;
					for (Map.Entry<Enchantment, Integer> entry : enchantmentMap.entrySet()) {
						amount += entry.getValue();
					}
					resultStack.setCount(Mth.clamp(amount, 1, 64));
				}
			}
		}
		return resultStack;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= this.ingredients.size();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
		for (int i = 0; i < nonnulllist.size(); ++i) {
			ItemStack itemstack = inv.getItem(i);
			if (itemstack.hasCraftingRemainingItem()) {
				nonnulllist.set(i, itemstack.getCraftingRemainingItem());
			} else if (itemstack.getItem() instanceof ForceRodItem) {
				ItemStack itemstack1 = itemstack.copy();
				ForceRodItem rod = (ForceRodItem) itemstack1.getItem();
				int damage = rod.damageItem(itemstack1, 1);

				if (itemstack1.getItem().getDamage(itemstack1) >= itemstack1.getMaxDamage()) {
					itemstack1.shrink(1);
				} else {
					itemstack1.setDamageValue(itemstack1.getDamageValue() + damage);
					nonnulllist.set(i, itemstack1);
				}
				continue;
			} else if ((itemstack.getItem() instanceof ExperienceTomeItem)) {
				ItemStack itemstack1 = itemstack.copy();
				CompoundTag tag = itemstack.getOrCreateTag();
				int experience = tag.getInt("Experience");
				int count = (int) ((float) experience / 100f);
				int newExperience;
				if (count > 64) {
					newExperience = experience - (64 * 100);
				} else {
					newExperience = experience - (count * 100);
				}
				tag.putInt("Experience", newExperience);
				itemstack1.setTag(tag);
				nonnulllist.set(i, itemstack1);
			}
		}

		return nonnulllist;
	}

	@Override
	public CraftingBookCategory category() {
		return CraftingBookCategory.MISC;
	}

	public static class SerializerTransmutationRecipe implements RecipeSerializer<TransmutationRecipe> {

		private static final Codec<TransmutationRecipe> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
								ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(recipe -> recipe.group),
								ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
								Ingredient.CODEC_NONEMPTY
										.listOf()
										.fieldOf("ingredients")
										.flatXmap(
												array -> {
													Ingredient[] aingredient = array.toArray(Ingredient[]::new);
													if (aingredient.length == 0) {
														return DataResult.error(() -> "No ingredients for shapeless recipe");
													} else {
														return aingredient.length > 9
																? DataResult.error(() -> "Too many ingredients for transmutation recipe. The maximum is: %s".formatted(9))
																: DataResult.success(NonNullList.of(Ingredient.EMPTY, aingredient));
													}
												},
												DataResult::success
										)
										.forGetter(recipe -> recipe.ingredients)
						)
						.apply(instance, TransmutationRecipe::new)
		);

		@Override
		public Codec<TransmutationRecipe> codec() {
			return CODEC;
		}

		@Nullable
		@Override
		public TransmutationRecipe fromNetwork(FriendlyByteBuf buffer) {
			String s = buffer.readUtf(32767);
			int i = buffer.readVarInt();
			NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

			for (int j = 0; j < nonnulllist.size(); ++j) {
				nonnulllist.set(j, Ingredient.fromNetwork(buffer));
			}

			ItemStack itemstack = buffer.readItem();
			return new TransmutationRecipe(s, itemstack, nonnulllist);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, TransmutationRecipe recipe) {
			buffer.writeUtf(recipe.group);
			buffer.writeVarInt(recipe.ingredients.size());

			for (Ingredient ingredient : recipe.ingredients) {
				ingredient.toNetwork(buffer);
			}

			buffer.writeItem(recipe.result);
		}
	}
}
