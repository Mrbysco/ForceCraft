package com.mrbysco.forcecraft.compat.jei.infuser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.forcecraft.blockentities.InfuserModifierType;
import com.mrbysco.forcecraft.compat.jei.JeiCompat;
import com.mrbysco.forcecraft.items.infuser.UpgradeBookData;
import com.mrbysco.forcecraft.recipe.InfuseRecipe;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InfuserCategory<T extends InfuseRecipe> implements IRecipeCategory<InfuseRecipe> {
	private final IDrawable background;
	private final IDrawable icon;
	private final Component localizedName;

	public InfuserCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(JeiCompat.RECIPE_INFUSER_JEI, 0, 0, 137, 109);
		this.icon = guiHelper.createDrawableIngredient(new ItemStack(ForceRegistry.INFUSER.get()));
		this.localizedName = new TranslatableComponent("forcecraft.gui.jei.category.infuser");
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
		ItemStack[] matchingStacks = recipe.getCenter().getItems();

		ingredients.setInputIngredients(recipe.getIngredients());
		if(recipe.getResultItem().isEmpty()) {
			List<ItemStack> stacks = new ArrayList<>();
			ItemStack[] modifierStack = recipe.getInput().getItems();
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
			ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
		}
	}

	@Override
	public void draw(InfuseRecipe recipe, PoseStack poseStack, double mouseX, double mouseY) {
		Minecraft minecraft = Minecraft.getInstance();
		Font font = minecraft.font;
		font.draw(poseStack, new TranslatableComponent("forcecraft.gui.jei.category.infuser.tier", recipe.getTier().asInt()), 4, 4, 0xFFFFFFFF);

	}

	@Override
	public Component getTitle() {
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
