package com.mrbysco.forcecraft.compat.jei.multipleoutput;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.forcecraft.compat.jei.JeiCompat;
import com.mrbysco.forcecraft.recipe.MultipleOutputFurnaceRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.gui.ingredient.ITooltipCallback;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public abstract class AbstractMultiOutputCategory<T extends MultipleOutputFurnaceRecipe> implements IRecipeCategory<T> {
	private final IDrawable background;
	private final IDrawable icon;
	private final Component localizedName;
	private final boolean showChance;

	public AbstractMultiOutputCategory(IGuiHelper guiHelper, Block icon, String translationKey, int yOffset, boolean showChance) {
		this.background = guiHelper.createDrawable(JeiCompat.RECIPE_MULTIPLES_JEI, 0, yOffset, 140, 37);
		this.icon = guiHelper.createDrawableIngredient(new ItemStack(icon));
		this.localizedName = new TranslatableComponent(translationKey);
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
	public void draw(T recipe, PoseStack matrixStack, double mouseX, double mouseY) {

	}

	@Override
	public Component getTitle() {
		return localizedName;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, T recipe, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(0, true, 9, 9);
		guiItemStacks.init(1, false, 82, 9);
		guiItemStacks.init(2, false, 112, 9);

		guiItemStacks.set(ingredients);
		if(showChance) {
			recipeLayout.getItemStacks().addTooltipCallback(new ITooltipCallback<ItemStack>() {
				@OnlyIn(Dist.CLIENT)
				@Override
				public void onTooltip(int slot, boolean input, ItemStack stack, List<Component> list) {
					if(!input && slot == 2) {
						list.add(new TextComponent(recipe.getSecondaryChance() * 100 + " ").append(new TranslatableComponent("forcecraft.gui.jei.category.grinding.tooltip")).withStyle(ChatFormatting.YELLOW));
					}
				}
			});
		}
	}
}