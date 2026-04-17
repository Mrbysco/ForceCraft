package com.mrbysco.forcecraft.compat.jei;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.gui.furnace.ForceFurnaceScreen;
import com.mrbysco.forcecraft.compat.jei.infuser.InfuserCategory;
import com.mrbysco.forcecraft.compat.jei.multipleoutput.FreezingCategory;
import com.mrbysco.forcecraft.compat.jei.multipleoutput.GrindingCategory;
import com.mrbysco.forcecraft.compat.jei.transfer.ItemCardTransferHandler;
import com.mrbysco.forcecraft.menu.furnace.ForceFurnaceMenu;
import com.mrbysco.forcecraft.recipe.FreezingRecipe;
import com.mrbysco.forcecraft.recipe.GrindingRecipe;
import com.mrbysco.forcecraft.recipe.InfuseRecipe;
import com.mrbysco.forcecraft.registry.ForceMenus;
import com.mrbysco.forcecraft.registry.ForceRecipes;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@JeiPlugin
public class JeiCompat implements IModPlugin {
	public static final Identifier RECIPE_MULTIPLES_JEI = Reference.modLoc("textures/gui/jei/multiples.png");
	public static final Identifier RECIPE_INFUSER_JEI = Reference.modLoc("textures/gui/jei/infuser.png");

	public static final Identifier PLUGIN_UID = Reference.modLoc("main");

	public static final Identifier FREEZING = Reference.modLoc("freezing");
	public static final Identifier GRINDING = Reference.modLoc("grinding");
	public static final Identifier INFUSER = Reference.modLoc("infuser");
	public static final IRecipeType<FreezingRecipe> FREEZING_TYPE = IRecipeType.create(Reference.MOD_ID, "freezing", FreezingRecipe.class);
	public static final IRecipeType<GrindingRecipe> GRINDING_TYPE = IRecipeType.create(Reference.MOD_ID, "grinding", GrindingRecipe.class);
	public static final IRecipeType<InfuseRecipe> INFUSER_TYPE = IRecipeType.create(Reference.MOD_ID, "infuser", InfuseRecipe.class);

	@Nullable
	private IRecipeCategory<FreezingRecipe> freezingCategory;
	@Nullable
	private IRecipeCategory<GrindingRecipe> grindingCategory;
	@Nullable
	private IRecipeCategory<InfuseRecipe> infuserCategory;

	@Override
	public Identifier getPluginUid() {
		return PLUGIN_UID;
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addCraftingStation(RecipeTypes.SMELTING, new ItemStack(ForceRegistry.FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING_FUEL, new ItemStack(ForceRegistry.FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING, new ItemStack(ForceRegistry.BLACK_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING_FUEL, new ItemStack(ForceRegistry.BLACK_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING, new ItemStack(ForceRegistry.BLUE_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING_FUEL, new ItemStack(ForceRegistry.BLUE_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING, new ItemStack(ForceRegistry.BROWN_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING_FUEL, new ItemStack(ForceRegistry.BROWN_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING, new ItemStack(ForceRegistry.CYAN_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING_FUEL, new ItemStack(ForceRegistry.CYAN_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING, new ItemStack(ForceRegistry.GRAY_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING_FUEL, new ItemStack(ForceRegistry.GRAY_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING, new ItemStack(ForceRegistry.GREEN_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING_FUEL, new ItemStack(ForceRegistry.GREEN_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING, new ItemStack(ForceRegistry.LIGHT_BLUE_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING_FUEL, new ItemStack(ForceRegistry.LIGHT_BLUE_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING, new ItemStack(ForceRegistry.LIGHT_GRAY_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING_FUEL, new ItemStack(ForceRegistry.LIGHT_GRAY_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING, new ItemStack(ForceRegistry.LIME_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING_FUEL, new ItemStack(ForceRegistry.LIME_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING, new ItemStack(ForceRegistry.MAGENTA_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING_FUEL, new ItemStack(ForceRegistry.MAGENTA_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING, new ItemStack(ForceRegistry.ORANGE_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING_FUEL, new ItemStack(ForceRegistry.ORANGE_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING, new ItemStack(ForceRegistry.PINK_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING_FUEL, new ItemStack(ForceRegistry.PINK_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING, new ItemStack(ForceRegistry.PURPLE_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING_FUEL, new ItemStack(ForceRegistry.PURPLE_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING, new ItemStack(ForceRegistry.RED_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING_FUEL, new ItemStack(ForceRegistry.RED_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING, new ItemStack(ForceRegistry.WHITE_FORCE_FURNACE.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING_FUEL, new ItemStack(ForceRegistry.WHITE_FORCE_FURNACE.get()));

		registration.addCraftingStation(FREEZING_TYPE, new ItemStack(ForceRegistry.FREEZING_CORE.get()));
		registration.addCraftingStation(GRINDING_TYPE, new ItemStack(ForceRegistry.GRINDING_CORE.get()));
		registration.addCraftingStation(INFUSER_TYPE, new ItemStack(ForceRegistry.INFUSER.get()));
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IJeiHelpers jeiHelpers = registration.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		registration.addRecipeCategories(
				freezingCategory = new FreezingCategory(guiHelper),
				grindingCategory = new GrindingCategory(guiHelper),
				infuserCategory = new InfuserCategory<>(guiHelper)
		);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(ForceFurnaceScreen.class, 78, 32, 28, 23, RecipeTypes.SMELTING, RecipeTypes.SMELTING_FUEL);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(new ItemCardTransferHandler(), RecipeTypes.CRAFTING);
		registration.addRecipeTransferHandler(ForceFurnaceMenu.class, ForceMenus.FORCE_FURNACE.get(), RecipeTypes.SMELTING, 0, 1, 3, 36);
		registration.addRecipeTransferHandler(ForceFurnaceMenu.class, ForceMenus.FORCE_FURNACE.get(), FREEZING_TYPE, 0, 1, 3, 36);
		registration.addRecipeTransferHandler(ForceFurnaceMenu.class, ForceMenus.FORCE_FURNACE.get(), GRINDING_TYPE, 0, 1, 3, 36);
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		assert FREEZING_TYPE != null;
		assert GRINDING_TYPE != null;
		assert INFUSER_TYPE != null;

		ClientLevel world = Objects.requireNonNull(Minecraft.getInstance().level);
		// TODO: Use the event to request the recipes from the server
//		registration.addRecipes(FREEZING_TYPE, world.getRecipeManager().getAllRecipesFor(ForceRecipes.FREEZING.get()).stream().map(RecipeHolder::value).toList());
//		registration.addRecipes(GRINDING_TYPE, world.getRecipeManager().getAllRecipesFor(ForceRecipes.GRINDING.get()).stream().map(RecipeHolder::value).toList());
//		registration.addRecipes(INFUSER_TYPE, world.getRecipeManager().getAllRecipesFor(ForceRecipes.INFUSER_TYPE.get()).stream().map(RecipeHolder::value).toList());
	}
}
