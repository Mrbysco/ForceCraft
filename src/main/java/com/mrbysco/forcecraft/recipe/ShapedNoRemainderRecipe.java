package com.mrbysco.forcecraft.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.forcecraft.registry.ForceRecipeSerializers;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

public class ShapedNoRemainderRecipe extends ShapedRecipe {
	final ShapedRecipePattern pattern;
	final ItemStack result;
	final String group;
	final CraftingBookCategory category;
	final boolean showNotification;

	public ShapedNoRemainderRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result, boolean showNotification) {
		super(group, category, pattern, result, showNotification);
		this.group = group;
		this.category = category;
		this.pattern = pattern;
		this.result = result;
		this.showNotification = showNotification;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ForceRecipeSerializers.SHAPED_NO_REMAINDER_SERIALIZER.get();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);

		return nonnulllist;
	}

	public static class SerializerShapedNoRemainderRecipe implements RecipeSerializer<ShapedNoRemainderRecipe> {
		public static final Codec<ShapedNoRemainderRecipe> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
								ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(recipe -> recipe.group),
								CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(recipe -> recipe.category),
								ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
								ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
								ExtraCodecs.strictOptionalField(Codec.BOOL, "show_notification", true).forGetter(recipe -> recipe.showNotification)
						)
						.apply(instance, ShapedNoRemainderRecipe::new)
		);

		@Override
		public Codec<ShapedNoRemainderRecipe> codec() {
			return CODEC;
		}

		public ShapedNoRemainderRecipe fromNetwork(FriendlyByteBuf pBuffer) {
			String s = pBuffer.readUtf();
			CraftingBookCategory craftingbookcategory = pBuffer.readEnum(CraftingBookCategory.class);
			ShapedRecipePattern shapedrecipepattern = ShapedRecipePattern.fromNetwork(pBuffer);
			ItemStack itemstack = pBuffer.readItem();
			boolean flag = pBuffer.readBoolean();
			return new ShapedNoRemainderRecipe(s, craftingbookcategory, shapedrecipepattern, itemstack, flag);
		}

		public void toNetwork(FriendlyByteBuf pBuffer, ShapedNoRemainderRecipe pRecipe) {
			pBuffer.writeUtf(pRecipe.group);
			pBuffer.writeEnum(pRecipe.category);
			pRecipe.pattern.toNetwork(pBuffer);
			pBuffer.writeItem(pRecipe.result);
			pBuffer.writeBoolean(pRecipe.showNotification);
		}
	}
}
