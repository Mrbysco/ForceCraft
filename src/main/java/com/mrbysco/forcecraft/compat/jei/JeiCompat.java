package com.mrbysco.forcecraft.compat.jei;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.ForceRecipeCache;
import com.mrbysco.forcecraft.client.screen.ForceFurnaceScreen;
import com.mrbysco.forcecraft.compat.jei.category.FreezingCategory;
import com.mrbysco.forcecraft.compat.jei.category.GrindingCategory;
import com.mrbysco.forcecraft.compat.jei.category.InfuserCategory;
import com.mrbysco.forcecraft.compat.jei.category.TransmutationCategory;
import com.mrbysco.forcecraft.compat.jei.subtype.RodSubtypes;
import com.mrbysco.forcecraft.menu.ForceFurnaceMenu;
import com.mrbysco.forcecraft.recipe.FreezingRecipe;
import com.mrbysco.forcecraft.recipe.GrindingRecipe;
import com.mrbysco.forcecraft.recipe.InfuseRecipe;
import com.mrbysco.forcecraft.recipe.TransmutationRecipe;
import com.mrbysco.forcecraft.registry.ForceMenus;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.helper.ForceFurnaceReg;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.IExtendableCraftingRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@JeiPlugin
public class JeiCompat implements IModPlugin {
	public static final Identifier PLUGIN_UID = Reference.modLoc("main");

	public static final Identifier INFUSER = Reference.modLoc("infuser");
	public static final Identifier FREEZING = Reference.modLoc("freezing");
	public static final Identifier GRINDING = Reference.modLoc("grinding");
	public static final IRecipeType<InfuseRecipe> INFUSER_TYPE = IRecipeType.create(Reference.MOD_ID, "infuser", InfuseRecipe.class);
	public static final IRecipeType<FreezingRecipe> FREEZING_TYPE = IRecipeType.create(Reference.MOD_ID, "freezing", FreezingRecipe.class);
	public static final IRecipeType<GrindingRecipe> GRINDING_TYPE = IRecipeType.create(Reference.MOD_ID, "grinding", GrindingRecipe.class);

	@Nullable
	private IRecipeCategory<InfuseRecipe> infuserCategory;
	@Nullable
	private IRecipeCategory<FreezingRecipe> freezingCategory;
	@Nullable
	private IRecipeCategory<GrindingRecipe> grindingCategory;

	@Override
	public Identifier getPluginUid() {
		return PLUGIN_UID;
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addCraftingStation(INFUSER_TYPE, new ItemStack(ForceRegistry.FORCE_INFUSER.get()));

		registration.addCraftingStation(RecipeTypes.SMELTING, new ItemStack(ForceRegistry.FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING_FUEL, new ItemStack(ForceRegistry.FORCE_FURNACE.get()));
		for (ForceFurnaceReg furnaceReg : ForceRegistry.getFurnaces()) {
			registration.addCraftingStation(RecipeTypes.SMELTING, new ItemStack(furnaceReg.getFurnace()));
			registration.addCraftingStation(RecipeTypes.SMELTING_FUEL, new ItemStack(furnaceReg.getFurnace()));
		}

		registration.addCraftingStation(FREEZING_TYPE, new ItemStack(ForceRegistry.FROZEN_CORE.get()));
		registration.addCraftingStation(GRINDING_TYPE, new ItemStack(ForceRegistry.GRINDING_CORE.get()));
	}

	@Override
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
		IExtendableCraftingRecipeCategory craftingCategory = registration.getCraftingCategory();
		craftingCategory.addExtension(TransmutationRecipe.class, new TransmutationCategory());
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IJeiHelpers jeiHelpers = registration.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		registration.addRecipeCategories(
				freezingCategory = new FreezingCategory(guiHelper),
				grindingCategory = new GrindingCategory(guiHelper),
				infuserCategory = new InfuserCategory(guiHelper)
		);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(ForceFurnaceScreen.class, 78, 32, 28, 23, RecipeTypes.SMELTING, RecipeTypes.SMELTING_FUEL);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(ForceFurnaceMenu.class, ForceMenus.FORCE_FURNACE.get(), RecipeTypes.SMELTING, 0, 1, 3, 36);
		registration.addRecipeTransferHandler(ForceFurnaceMenu.class, ForceMenus.FORCE_FURNACE.get(), FREEZING_TYPE, 0, 1, 3, 36);
		registration.addRecipeTransferHandler(ForceFurnaceMenu.class, ForceMenus.FORCE_FURNACE.get(), GRINDING_TYPE, 0, 1, 3, 36);
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		registration.registerSubtypeInterpreter(ForceRegistry.FORCE_ROD.get(), new RodSubtypes());
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		assert FREEZING_TYPE != null;
		assert GRINDING_TYPE != null;
		assert INFUSER_TYPE != null;

		List<InfuseRecipe> recipes = new ArrayList<>(ForceRecipeCache.getInfuseRecipes().stream().map(RecipeHolder::value).toList());
		recipes.sort(Comparator.comparingInt(recipe -> recipe.getTier().ordinal()));
		registration.addRecipes(INFUSER_TYPE, recipes);

		List<FreezingRecipe> freezingRecipes = new ArrayList<>(ForceRecipeCache.getFreezingRecipes().stream().map(RecipeHolder::value).toList());
		registration.addRecipes(FREEZING_TYPE, freezingRecipes);

		List<GrindingRecipe> grindingRecipes = new ArrayList<>(ForceRecipeCache.getGrindingRecipes().stream().map(RecipeHolder::value).toList());
		registration.addRecipes(GRINDING_TYPE, grindingRecipes);

	}
}
