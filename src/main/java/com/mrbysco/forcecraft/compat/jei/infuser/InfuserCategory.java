package com.mrbysco.forcecraft.compat.jei.infuser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.forcecraft.blockentities.InfuserModifierType;
import com.mrbysco.forcecraft.compat.jei.JeiCompat;
import com.mrbysco.forcecraft.items.infuser.UpgradeBookData;
import com.mrbysco.forcecraft.recipe.InfuseRecipe;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InfuserCategory<T extends InfuseRecipe> implements IRecipeCategory<InfuseRecipe> {
	private final IDrawable background;
	private final IDrawable icon;
	private final Component localizedName;

	public InfuserCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(JeiCompat.RECIPE_INFUSER_JEI, 0, 0, 137, 109);
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ForceRegistry.INFUSER.get()));
		this.localizedName = Component.translatable("forcecraft.gui.jei.category.infuser");
	}

	@Override
	public RecipeType<InfuseRecipe> getRecipeType() {
		return JeiCompat.INFUSER_TYPE;
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
	public void setRecipe(IRecipeLayoutBuilder builder, InfuseRecipe recipe, IFocusGroup focuses) {
		ItemStack[] matchingStacks = recipe.getCenter().getItems();

		builder.addSlot(RecipeIngredientRole.INPUT, 46, 47).addIngredients(recipe.getIngredients().get(0));

		List<ItemStack> stacks = new ArrayList<>();
		ItemStack[] modifierStack = recipe.getInput().getItems();
		if (modifierStack.length > 0) {
			InfuserModifierType type = recipe.getModifier();
			UpgradeBookData fakeUpgradeBook = new UpgradeBookData(new ItemStack(ForceRegistry.UPGRADE_TOME.get()));
			fakeUpgradeBook.setTier(recipe.getTier());

			for (ItemStack center : matchingStacks) {
				ItemStack centerStack = center.copy();
				if (centerStack.getItem() == ForceRegistry.FORCE_PACK.get()) {
					type.apply(centerStack, modifierStack[0], fakeUpgradeBook);
					type.apply(centerStack, modifierStack[0], fakeUpgradeBook);
					type.apply(centerStack, modifierStack[0], fakeUpgradeBook);
					type.apply(centerStack, modifierStack[0], fakeUpgradeBook);
				} else {
					type.apply(centerStack, modifierStack[0], fakeUpgradeBook);
				}

				stacks.addAll(Arrays.stream(modifierStack).toList());
			}
			builder.addSlot(RecipeIngredientRole.OUTPUT, 46, 10).addItemStacks(stacks);
		} else {
			builder.addSlot(RecipeIngredientRole.OUTPUT, 46, 10).addItemStack(matchingStacks[0]);
		}
		builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 47).addItemStack(recipe.getResultItem());
	}

	@Override
	public void draw(InfuseRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
		IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);

		Minecraft minecraft = Minecraft.getInstance();
		Font font = minecraft.font;
		font.draw(stack, Component.translatable("forcecraft.gui.jei.category.infuser.tier", recipe.getTier().asInt()), 4, 4, 0xFFFFFFFF);
	}

	@Override
	public Component getTitle() {
		return localizedName;
	}
}
