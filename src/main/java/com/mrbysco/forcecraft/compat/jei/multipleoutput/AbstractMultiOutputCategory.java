package com.mrbysco.forcecraft.compat.jei.multipleoutput;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.gui.ingredient.ITooltipCallback;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.util.Translator;
import com.mrbysco.forcecraft.compat.jei.JeiCompat;
import com.mrbysco.forcecraft.recipe.MultipleOutputFurnaceRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public abstract class AbstractMultiOutputCategory<T extends MultipleOutputFurnaceRecipe> implements IRecipeCategory<T> {
	private final IDrawable background;
	private final IDrawable icon;
	private final String localizedName;
	private final boolean showChance;

	public AbstractMultiOutputCategory(IGuiHelper guiHelper, Block icon, String translationKey, int yOffset, boolean showChance) {
		this.background = guiHelper.createDrawable(JeiCompat.RECIPE_MULTIPLES_JEI, 0, yOffset, 140, 37);
		this.icon = guiHelper.createDrawableIngredient(new ItemStack(icon));
		this.localizedName = Translator.translateToLocal(translationKey);
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
	public void setIngredients(T recipe, IIngredients ingredients) {
		ingredients.setInputIngredients(recipe.getIngredients());
		ingredients.setOutputs(VanillaTypes.ITEM, recipe.getRecipeOutputs());
	}

	@Override
	public void draw(T recipe, MatrixStack matrixStack, double mouseX, double mouseY) {

	}

	@Override
	public String getTitle() {
		return localizedName;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, T recipe, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(0, true, 9, 9);
		guiItemStacks.init(1, false, 82, 9);
		guiItemStacks.init(2, false, 112, 9);

		guiItemStacks.set(ingredients);
		if (showChance) {
			recipeLayout.getItemStacks().addTooltipCallback(new ITooltipCallback<ItemStack>() {
				@OnlyIn(Dist.CLIENT)
				@Override
				public void onTooltip(int slot, boolean input, ItemStack stack, List<ITextComponent> list) {
					if (!input && slot == 2) {
						list.add(new StringTextComponent(recipe.getSecondaryChance() * 100 + " ").append(new TranslationTextComponent("forcecraft.gui.jei.category.grinding.tooltip")).withStyle(TextFormatting.YELLOW));
					}
				}
			});
		}
	}
}