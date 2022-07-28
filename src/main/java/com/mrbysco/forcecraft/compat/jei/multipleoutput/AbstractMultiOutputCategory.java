package com.mrbysco.forcecraft.compat.jei.multipleoutput;

import com.mrbysco.forcecraft.compat.jei.JeiCompat;
import com.mrbysco.forcecraft.recipe.MultipleOutputFurnaceRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.List;

public abstract class AbstractMultiOutputCategory<T extends MultipleOutputFurnaceRecipe> implements IRecipeCategory<T> {
	private final IDrawable background;
	private final IDrawable icon;
	private final Component localizedName;
	private final boolean showChance;

	public AbstractMultiOutputCategory(IGuiHelper guiHelper, Block icon, String translationKey, int yOffset, boolean showChance) {
		this.background = guiHelper.createDrawable(JeiCompat.RECIPE_MULTIPLES_JEI, 0, yOffset, 140, 37);
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(icon));
		this.localizedName = Component.translatable(translationKey);
		this.showChance = showChance;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 10, 10).addIngredients(recipe.getIngredients().get(0));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 83, 10)
				.addItemStack(recipe.getRecipeOutputs().get(0));
		if (recipe.getRecipeOutputs().size() > 1) {
			IRecipeSlotBuilder secondOutputBuilder = builder.addSlot(RecipeIngredientRole.OUTPUT, 113, 10)
					.addItemStack(recipe.getRecipeOutputs().get(1));

			if (showChance) {
				secondOutputBuilder.addTooltipCallback(new ChanceTooltip(recipe));
			}
		}
	}

	@Override
	public Component getTitle() {
		return localizedName;
	}

	public static class ChanceTooltip implements IRecipeSlotTooltipCallback {
		private final MultipleOutputFurnaceRecipe recipe;

		public ChanceTooltip(MultipleOutputFurnaceRecipe recipe) {
			this.recipe = recipe;
		}

		@Override
		public void onTooltip(IRecipeSlotView recipeSlotView, List<Component> tooltip) {
			tooltip.add(Component.literal(recipe.getSecondaryChance() * 100 + " ").append(Component.translatable("forcecraft.gui.jei.category.grinding.tooltip")).withStyle(ChatFormatting.YELLOW));
		}
	}
}