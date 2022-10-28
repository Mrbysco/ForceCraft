package com.mrbysco.forcecraft.compat.jei.transfer;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiIngredient;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import com.mrbysco.forcecraft.container.ItemCardContainer;
import com.mrbysco.forcecraft.networking.PacketHandler;
import com.mrbysco.forcecraft.networking.message.RecipeToCardMessage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemCardTransferHandler implements IRecipeTransferHandler<ItemCardContainer> {
	public ItemCardTransferHandler() {

	}

	@Nullable
	@Override
	public Class<ItemCardContainer> getContainerClass() {
		return ItemCardContainer.class;
	}

	@Nullable
	@Override
	public IRecipeTransferError transferRecipe(ItemCardContainer container, Object recipe, IRecipeLayout recipeLayout, PlayerEntity player, boolean maxTransfer, boolean doTransfer) {
		Map<Integer, ? extends IGuiIngredient<ItemStack>> guiIngredients = recipeLayout.getItemStacks().getGuiIngredients();
		List<ItemStack> items = new ArrayList<>(10);
		for (int i = 0; i < 10; i++) {
			items.add(ItemStack.EMPTY);
		}
		for (Map.Entry<Integer, ? extends IGuiIngredient<ItemStack>> entry : guiIngredients.entrySet()) {
			int recipeSlot = entry.getKey();
			List<ItemStack> allIngredients = entry.getValue().getAllIngredients();
			if (!allIngredients.isEmpty()) {
				items.set(recipeSlot, allIngredients.get(0));
			}
		}
		PacketHandler.CHANNEL.sendToServer(new RecipeToCardMessage(items));

		return null;
	}
}
