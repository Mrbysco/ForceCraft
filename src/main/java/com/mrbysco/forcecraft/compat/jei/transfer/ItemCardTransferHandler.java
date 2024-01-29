package com.mrbysco.forcecraft.compat.jei.transfer;

import com.mrbysco.forcecraft.menu.ItemCardMenu;
import com.mrbysco.forcecraft.networking.message.RecipeToCardPayload;
import com.mrbysco.forcecraft.registry.ForceMenus;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemCardTransferHandler implements IRecipeTransferHandler<ItemCardMenu, CraftingRecipe> {
	public ItemCardTransferHandler() {

	}

	@Override
	public Optional<MenuType<ItemCardMenu>> getMenuType() {
		return Optional.of(ForceMenus.ITEM_CARD.get());
	}

	@Override
	public RecipeType<CraftingRecipe> getRecipeType() {
		return null;
	}

	@Nullable
	@Override
	public Class<ItemCardMenu> getContainerClass() {
		return ItemCardMenu.class;
	}

	@Override
	public @Nullable IRecipeTransferError transferRecipe(ItemCardMenu container, CraftingRecipe recipe, IRecipeSlotsView recipeSlots, Player player, boolean maxTransfer, boolean doTransfer) {
		List<ItemStack> items = new ArrayList<>(10);
		for (int i = 0; i < 10; i++) {
			items.add(ItemStack.EMPTY);
		}

		List<IRecipeSlotView> ingredients = recipeSlots.getSlotViews(RecipeIngredientRole.INPUT);
		for (int i = 0; i < ingredients.size(); i++) {
			if (ingredients.get(i).getDisplayedItemStack().isPresent())
				items.set(i, ingredients.get(i).getDisplayedItemStack().get());
		}
		List<IRecipeSlotView> outputs = recipeSlots.getSlotViews(RecipeIngredientRole.OUTPUT);

		if (outputs.get(0).getDisplayedItemStack().isPresent())
			items.set(9, outputs.get(0).getDisplayedItemStack().get());

		PacketDistributor.SERVER.noArg().send(new RecipeToCardPayload(items));

		return null;
	}
}
