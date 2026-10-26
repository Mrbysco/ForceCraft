package com.mrbysco.forcecraft.compat.jei.category;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.compat.jei.JeiCompat;
import com.mrbysco.forcecraft.recipe.InfuseRecipe;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.upgrade.UpgradeBookData;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InfuserCategory implements IRecipeCategory<InfuseRecipe> {
	private static final Identifier INFUSER_TEXTURE = Reference.modLoc("textures/gui/jei/infuser.png");
	private final IDrawable background;
	private final IDrawable icon;
	private final Component localizedName;

	public InfuserCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(INFUSER_TEXTURE, 0, 0, 137, 109);
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ForceRegistry.FORCE_INFUSER.get()));
		this.localizedName = Component.translatable("forcecraft.gui.jei.category.infuser");
	}

	@Override
	public IRecipeType<InfuseRecipe> getRecipeType() {
		return JeiCompat.INFUSER_TYPE;
	}

	@Override
	public Component getTitle() {
		return localizedName;
	}

	@Override
	public int getWidth() {
		return 137;
	}

	@Override
	public int getHeight() {
		return 109;
	}

	@Override
	public @Nullable IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, InfuseRecipe recipe, IFocusGroup focuses) {
		List<ItemStack> tools = getTools(recipe, focuses);

		builder.addSlot(RecipeIngredientRole.INPUT, 46, 47).addItemStacks(tools);
		builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 46, 10).add(recipe.getIngredient());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 47).addItemStacks(getResults(recipe, tools));
	}

	private List<ItemStack> getTools(InfuseRecipe recipe, IFocusGroup focuses) {
		for (IFocus<ItemStack> focus : focuses.getFocuses(VanillaTypes.ITEM_STACK).toList()) {
			ItemStack focusStack = focus.getTypedValue().getIngredient();
			if (recipe.getCenter().test(focusStack)) {
				return List.of(focusStack);
			}
		}
		return recipe.getCenter().items()
				.map(holder -> new ItemStack(holder.value()))
				.toList();
	}

	private List<ItemStack> getResults(InfuseRecipe recipe, List<ItemStack> tools) {
		if (recipe.hasOutput()) {
			return List.of(recipe.getOutput());
		}

		UpgradeBookData book = UpgradeBookData.DEFAULT.withTier(recipe.getTier());

		List<ItemStack> results = new ArrayList<>();
		for (ItemStack tool : tools) {
			ItemStack result = tool.copy();
			recipe.getUpgrade().apply(result, book);
			results.add(result);
		}
		return results;
	}

	@Override
	public void draw(InfuseRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
		this.background.draw(guiGraphics);
		IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);

		Minecraft minecraft = Minecraft.getInstance();
		Font font = minecraft.font;
		guiGraphics.text(font, Component.translatable("forcecraft.gui.jei.category.infuser.tier", recipe.getTier().ordinal() + 1), 4, 4, 0xFFFFFFFF, false);
	}
}
