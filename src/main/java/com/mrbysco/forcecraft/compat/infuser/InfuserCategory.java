package com.mrbysco.forcecraft.compat.infuser;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.util.Translator;
import com.mrbysco.forcecraft.blocks.infuser.InfuserModifierType;
import com.mrbysco.forcecraft.compat.JeiCompat;
import com.mrbysco.forcecraft.items.infuser.UpgradeBookData;
import com.mrbysco.forcecraft.recipe.InfuseRecipe;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class InfuserCategory<T extends InfuseRecipe> implements IRecipeCategory<InfuseRecipe> {
	private final IDrawable background;
	private final IDrawable icon;
	private final String localizedName;

	public InfuserCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(JeiCompat.RECIPE_INFUSER_JEI, 0, 0, 137, 109);
		this.icon = guiHelper.createDrawableIngredient(new ItemStack(ForceRegistry.INFUSER.get()));
		this.localizedName = Translator.translateToLocal("forcecraft.gui.jei.category.infuser");
	}

	@Override
	public ResourceLocation getUid() {
		return JeiCompat.INFUSER;
	}

	@Override
	public Class<? extends InfuseRecipe> getRecipeClass() {
		return InfuseRecipe.class;
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
	public void setIngredients(InfuseRecipe recipe, IIngredients ingredients) {
		ItemStack[] matchingStacks = recipe.getCenter().getMatchingStacks();

		ingredients.setInputIngredients(recipe.getIngredients());
		if(recipe.getRecipeOutput().isEmpty()) {
			List<ItemStack> stacks = new ArrayList<>();
			ItemStack[] modifierStack = recipe.getInput().getMatchingStacks();
			if(modifierStack.length > 0) {
				InfuserModifierType type = recipe.getModifier();
				UpgradeBookData fakeUpgradeBook = new UpgradeBookData(new ItemStack(ForceRegistry.UPGRADE_TOME.get()));
				fakeUpgradeBook.setTier(recipe.getTier());

				for(ItemStack center : matchingStacks) {
					ItemStack centerStack = center.copy();
					if(centerStack.getItem() == ForceRegistry.FORCE_PACK.get()) {
						type.apply(centerStack, modifierStack[0], fakeUpgradeBook);
						type.apply(centerStack, modifierStack[0], fakeUpgradeBook);
						type.apply(centerStack, modifierStack[0], fakeUpgradeBook);
						type.apply(centerStack, modifierStack[0], fakeUpgradeBook);
					} else {
						type.apply(centerStack, modifierStack[0], fakeUpgradeBook);
					}

					stacks.add(centerStack);
				}
				ingredients.setOutputs(VanillaTypes.ITEM, stacks);
			} else {
				ingredients.setOutput(VanillaTypes.ITEM, matchingStacks[0]);
			}
		} else {
			ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
		}
	}

	@Override
	public void draw(InfuseRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		Minecraft minecraft = Minecraft.getInstance();
		FontRenderer font = minecraft.fontRenderer;
		font.drawText(matrixStack, new TranslationTextComponent("forcecraft.gui.jei.category.infuser.tier", recipe.getTier().asInt()), 4, 4, 0xFFFFFFFF);

	}

	@Override
	public String getTitle() {
		return localizedName;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, InfuseRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(0, true, 45, 46);
		guiItemStacks.init(1, true, 45, 9);
		guiItemStacks.init(2, false, 119, 46);

		List<ItemStack> outputFull = new ArrayList<>();
		for(List<ItemStack> stackList : ingredients.getOutputs(VanillaTypes.ITEM)) {
			outputFull.add(stackList.get(0));
		}

		guiItemStacks.set(ingredients);
		guiItemStacks.set(2, outputFull);
	}
}
