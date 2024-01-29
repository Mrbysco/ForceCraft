package com.mrbysco.forcecraft.compat.jei.infuser;

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
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
		Minecraft minecraft = Minecraft.getInstance();
		ClientLevel level = minecraft.level;
		if (level == null) {
			throw new NullPointerException("level must not be null.");
		}
		RegistryAccess registryAccess = level.registryAccess();

		final Optional<IFocus<?>> focused = focuses.getAllFocuses().stream().findFirst();
		ItemStack[] matchingStacks = recipe.getCenter().getItems();

		if (focused.isPresent() && focused.get().getTypedValue().getIngredient() instanceof ItemStack focusStack &&
				recipe.getCenter().test(focusStack)) {
			builder.addSlot(RecipeIngredientRole.INPUT, 46, 47).addItemStack(focusStack);

			List<ItemStack> stacks = new ArrayList<>();
			ItemStack[] modifierStack = recipe.getIngredient().getItems();
			if (modifierStack.length > 0) {
				builder.addSlot(RecipeIngredientRole.CATALYST, 46, 10).addItemStack(modifierStack[0]);
			}
			if (recipe.getResultItem(registryAccess).isEmpty()) {
				if (modifierStack.length > 0) {
					ItemStack modifier = modifierStack[0].copy();
					InfuserModifierType type = recipe.getModifier();
					UpgradeBookData fakeUpgradeBook = new UpgradeBookData(new ItemStack(ForceRegistry.UPGRADE_TOME.get()));
					fakeUpgradeBook.setTier(recipe.getTier());

					for (ItemStack center : matchingStacks) {
						ItemStack centerStack = center.copy();
						if (centerStack.getItem() == ForceRegistry.FORCE_PACK.get()) {
							type.apply(centerStack, modifier, fakeUpgradeBook);
							type.apply(centerStack, modifier, fakeUpgradeBook);
							type.apply(centerStack, modifier, fakeUpgradeBook);
							type.apply(centerStack, modifier, fakeUpgradeBook);
						} else {
							type.apply(centerStack, modifier, fakeUpgradeBook);
						}

						stacks.add(centerStack);

					}
					builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 47).addItemStacks(stacks);
				} else {
					builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 47).addItemStack(matchingStacks[0]);
				}
			} else {
				builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 47).addItemStack(recipe.getResultItem(registryAccess));
			}
		} else {
			builder.addSlot(RecipeIngredientRole.INPUT, 46, 47).addIngredients(recipe.getIngredients().get(0));

			List<ItemStack> stacks = new ArrayList<>();
			ItemStack[] modifierStack = recipe.getIngredient().getItems();
			if (modifierStack.length > 0) {
				builder.addSlot(RecipeIngredientRole.CATALYST, 46, 10).addItemStack(modifierStack[0]);
			}
			if (recipe.getResultItem(registryAccess).isEmpty()) {
				if (modifierStack.length > 0) {
					ItemStack modifier = modifierStack[0].copy();
					InfuserModifierType type = recipe.getModifier();
					UpgradeBookData fakeUpgradeBook = new UpgradeBookData(new ItemStack(ForceRegistry.UPGRADE_TOME.get()));
					fakeUpgradeBook.setTier(recipe.getTier());

					for (ItemStack center : matchingStacks) {
						ItemStack centerStack = center.copy();
						if (centerStack.getItem() == ForceRegistry.FORCE_PACK.get()) {
							type.apply(centerStack, modifier, fakeUpgradeBook);
							type.apply(centerStack, modifier, fakeUpgradeBook);
							type.apply(centerStack, modifier, fakeUpgradeBook);
							type.apply(centerStack, modifier, fakeUpgradeBook);
						} else {
							type.apply(centerStack, modifier, fakeUpgradeBook);
						}

						stacks.add(centerStack);

					}
					builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 47).addItemStacks(stacks);
				} else {
					builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 47).addItemStack(matchingStacks[0]);
				}
			} else {
				builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 47).addItemStack(recipe.getResultItem(registryAccess));
			}
		}
	}

	@Override
	public void draw(InfuseRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);

		Minecraft minecraft = Minecraft.getInstance();
		Font font = minecraft.font;
		guiGraphics.drawString(font, Component.translatable("forcecraft.gui.jei.category.infuser.tier", recipe.getTier().asInt()), 4, 4, 0xFFFFFFFF, false);
	}

	@Override
	public Component getTitle() {
		return localizedName;
	}
}
