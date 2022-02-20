package com.mrbysco.forcecraft.compat.jei;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.gui.furnace.ForceFurnaceScreen;
import com.mrbysco.forcecraft.compat.jei.infuser.InfuserCategory;
import com.mrbysco.forcecraft.compat.jei.multipleoutput.FreezingCategory;
import com.mrbysco.forcecraft.compat.jei.multipleoutput.GrindingCategory;
import com.mrbysco.forcecraft.compat.jei.transfer.ItemCardTransferHandler;
import com.mrbysco.forcecraft.menu.furnace.ForceFurnaceMenu;
import com.mrbysco.forcecraft.recipe.ForceRecipes;
import com.mrbysco.forcecraft.recipe.FreezingRecipe;
import com.mrbysco.forcecraft.recipe.GrindingRecipe;
import com.mrbysco.forcecraft.recipe.InfuseRecipe;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.util.ErrorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Objects;

@JeiPlugin
public class JeiCompat implements IModPlugin {
	public static final ResourceLocation RECIPE_MULTIPLES_JEI = new ResourceLocation(Reference.MOD_ID, "textures/gui/jei/multiples.png");
	public static final ResourceLocation RECIPE_INFUSER_JEI = new ResourceLocation(Reference.MOD_ID, "textures/gui/jei/infuser.png");

	public static final ResourceLocation PLUGIN_UID = new ResourceLocation(Reference.MOD_ID, "main");
	public static final ResourceLocation FREEZING = new ResourceLocation(Reference.MOD_ID, "freezing");
	public static final ResourceLocation GRINDING = new ResourceLocation(Reference.MOD_ID, "grinding");
	public static final ResourceLocation INFUSER = new ResourceLocation(Reference.MOD_ID, "infuser");
	@Nullable
	private IRecipeCategory<FreezingRecipe> freezingCategory;
	@Nullable
	private IRecipeCategory<GrindingRecipe> grindingCategory;
	@Nullable
	private IRecipeCategory<InfuseRecipe> infuserCategory;

	@Override
	public ResourceLocation getPluginUid() {
		return PLUGIN_UID;
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.BLACK_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.BLUE_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.BROWN_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.CYAN_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.GRAY_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.GREEN_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.LIGHT_BLUE_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.LIGHT_GRAY_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.LIME_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.MAGENTA_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.ORANGE_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.PINK_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.PURPLE_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.RED_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.WHITE_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);

		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.BLACK_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.BLUE_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.BROWN_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.CYAN_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.GRAY_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.GREEN_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.LIGHT_BLUE_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.LIGHT_GRAY_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.LIME_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.MAGENTA_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.ORANGE_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.PINK_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.PURPLE_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.RED_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.WHITE_FORCE_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);

		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.FREEZING_CORE.get()), FREEZING);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.GRINDING_CORE.get()), GRINDING);
		registration.addRecipeCatalyst(new ItemStack(ForceRegistry.INFUSER.get()), INFUSER);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IJeiHelpers jeiHelpers = registration.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		registration.addRecipeCategories(
			freezingCategory = new FreezingCategory(guiHelper),
			grindingCategory = new GrindingCategory(guiHelper),
			infuserCategory = new InfuserCategory<InfuseRecipe>(guiHelper)
		);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(ForceFurnaceScreen.class, 78, 32, 28, 23, VanillaRecipeCategoryUid.FURNACE, VanillaRecipeCategoryUid.FUEL);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(new ItemCardTransferHandler(), VanillaRecipeCategoryUid.CRAFTING);
		registration.addRecipeTransferHandler(ForceFurnaceMenu.class, VanillaRecipeCategoryUid.FURNACE, 0, 1, 3, 36);
		registration.addRecipeTransferHandler(ForceFurnaceMenu.class, FREEZING, 0, 1, 3, 36);
		registration.addRecipeTransferHandler(ForceFurnaceMenu.class, GRINDING, 0, 1, 3, 36);
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		ErrorUtil.checkNotNull(freezingCategory, "freezingCategory");
		ErrorUtil.checkNotNull(grindingCategory, "grindingCategory");
		ErrorUtil.checkNotNull(infuserCategory, "grindingCategory");

		ClientLevel world = Objects.requireNonNull(Minecraft.getInstance().level);
		registration.addRecipes(world.getRecipeManager().getAllRecipesFor(ForceRecipes.FREEZING), FREEZING);
		registration.addRecipes(world.getRecipeManager().getAllRecipesFor(ForceRecipes.GRINDING), GRINDING);
		registration.addRecipes(world.getRecipeManager().getAllRecipesFor(ForceRecipes.INFUSER_TYPE), INFUSER);
	}
}
