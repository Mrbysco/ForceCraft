package com.mrbysco.forcecraft.components.card;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record RecipeContentsData(NonNullList<ItemStack> recipeItems, ItemStack resultItem) {
	public static final RecipeContentsData EMPTY = new RecipeContentsData(NonNullList.withSize(9, ItemStack.EMPTY), ItemStack.EMPTY);
	public static final Codec<RecipeContentsData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
					ItemStack.OPTIONAL_CODEC
							.listOf()
							.fieldOf("recipeItems")
							.flatXmap(
									array -> {
										ItemStack[] stacks = array.toArray(ItemStack[]::new);
										return stacks.length != 9
												? DataResult.error(() -> "More than 9 items for stored recipe")
												: DataResult.success(NonNullList.of(ItemStack.EMPTY, stacks));
									},
									DataResult::success
							)
							.forGetter(recipe -> recipe.recipeItems),
					ItemStack.CODEC.fieldOf("resultItem").forGetter(RecipeContentsData::resultItem))
			.apply(inst, RecipeContentsData::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, RecipeContentsData> STREAM_CODEC = StreamCodec.of(
			RecipeContentsData::toNetwork, RecipeContentsData::fromNetwork
	);

	public RecipeContentsData {
		if (recipeItems.size() != 9) {
			throw new IllegalArgumentException("Recipe items must be 9 items long");
		}
	}

	private static RecipeContentsData fromNetwork(RegistryFriendlyByteBuf byteBuf) {
		NonNullList<ItemStack> recipeItems = NonNullList.withSize(9, ItemStack.EMPTY);
		for (int i = 0; i < 9; i++) {
			recipeItems.set(i, ItemStack.OPTIONAL_STREAM_CODEC.decode(byteBuf));
		}
		ItemStack resultItem = ItemStack.STREAM_CODEC.decode(byteBuf);
		return new RecipeContentsData(recipeItems, resultItem);
	}

	private static void toNetwork(RegistryFriendlyByteBuf byteBuf, RecipeContentsData playerCompassData) {
		for (int i = 0; i < 9; i++) {
			ItemStack.OPTIONAL_STREAM_CODEC.encode(byteBuf, playerCompassData.recipeItems.get(i));
		}
		ItemStack.STREAM_CODEC.encode(byteBuf, playerCompassData.resultItem);
	}
}
