package com.mrbysco.forcecraft.compat.rei;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.compat.rei.infuser.category.InfuserCategory;
import com.mrbysco.forcecraft.compat.rei.infuser.display.InfuserDisplay;
import com.mrbysco.forcecraft.compat.rei.multipleoutput.category.FreezingCategory;
import com.mrbysco.forcecraft.compat.rei.multipleoutput.category.GrindingCategory;
import com.mrbysco.forcecraft.compat.rei.multipleoutput.display.FreezingDisplay;
import com.mrbysco.forcecraft.compat.rei.multipleoutput.display.GrindingDisplay;
import com.mrbysco.forcecraft.recipe.FreezingRecipe;
import com.mrbysco.forcecraft.recipe.GrindingRecipe;
import com.mrbysco.forcecraft.recipe.InfuseRecipe;
import com.mrbysco.forcecraft.registry.ForceRecipes;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

@REIPluginClient
public class REIPlugin implements REIClientPlugin {
	public static final ResourceLocation RECIPE_MULTIPLES_JEI = Reference.modLoc("textures/gui/jei/multiples.png");
	public static final ResourceLocation RECIPE_INFUSER_JEI = Reference.modLoc("textures/gui/jei/infuser.png");

	public static final CategoryIdentifier<FreezingDisplay> FREEZING_CATEGORY = CategoryIdentifier.of(Reference.MOD_ID, "freezing");
	public static final CategoryIdentifier<GrindingDisplay> GRINDING_CATEGORY = CategoryIdentifier.of(Reference.MOD_ID, "grinding");
	public static final CategoryIdentifier<InfuserDisplay> INFUSER_CATEGORY = CategoryIdentifier.of(Reference.MOD_ID, "infuser");

	@Override
	public void registerCategories(CategoryRegistry registry) {
		registry.add(new FreezingCategory());
		registry.add(new GrindingCategory());
		registry.add(new InfuserCategory());

		registry.addWorkstations(BuiltinPlugin.SMELTING, EntryStacks.of(ForceRegistry.FORCE_FURNACE.get()));
		registry.addWorkstations(BuiltinPlugin.SMELTING, EntryStacks.of(ForceRegistry.BLACK_FORCE_FURNACE.get()));
		registry.addWorkstations(BuiltinPlugin.SMELTING, EntryStacks.of(ForceRegistry.BLUE_FORCE_FURNACE.get()));
		registry.addWorkstations(BuiltinPlugin.SMELTING, EntryStacks.of(ForceRegistry.BROWN_FORCE_FURNACE.get()));
		registry.addWorkstations(BuiltinPlugin.SMELTING, EntryStacks.of(ForceRegistry.CYAN_FORCE_FURNACE.get()));
		registry.addWorkstations(BuiltinPlugin.SMELTING, EntryStacks.of(ForceRegistry.GRAY_FORCE_FURNACE.get()));
		registry.addWorkstations(BuiltinPlugin.SMELTING, EntryStacks.of(ForceRegistry.GREEN_FORCE_FURNACE.get()));
		registry.addWorkstations(BuiltinPlugin.SMELTING, EntryStacks.of(ForceRegistry.LIGHT_BLUE_FORCE_FURNACE.get()));
		registry.addWorkstations(BuiltinPlugin.SMELTING, EntryStacks.of(ForceRegistry.LIGHT_GRAY_FORCE_FURNACE.get()));
		registry.addWorkstations(BuiltinPlugin.SMELTING, EntryStacks.of(ForceRegistry.LIME_FORCE_FURNACE.get()));
		registry.addWorkstations(BuiltinPlugin.SMELTING, EntryStacks.of(ForceRegistry.MAGENTA_FORCE_FURNACE.get()));
		registry.addWorkstations(BuiltinPlugin.SMELTING, EntryStacks.of(ForceRegistry.ORANGE_FORCE_FURNACE.get()));
		registry.addWorkstations(BuiltinPlugin.SMELTING, EntryStacks.of(ForceRegistry.PINK_FORCE_FURNACE.get()));
		registry.addWorkstations(BuiltinPlugin.SMELTING, EntryStacks.of(ForceRegistry.PURPLE_FORCE_FURNACE.get()));
		registry.addWorkstations(BuiltinPlugin.SMELTING, EntryStacks.of(ForceRegistry.RED_FORCE_FURNACE.get()));
		registry.addWorkstations(BuiltinPlugin.SMELTING, EntryStacks.of(ForceRegistry.WHITE_FORCE_FURNACE.get()));

		registry.addWorkstations(FREEZING_CATEGORY, EntryStacks.of(ForceRegistry.FREEZING_CORE.get()));
		registry.addWorkstations(GRINDING_CATEGORY, EntryStacks.of(ForceRegistry.GRINDING_CORE.get()));
		registry.addWorkstations(INFUSER_CATEGORY, EntryStacks.of(ForceRegistry.INFUSER.get()));
	}

	@Override
	public void registerDisplays(DisplayRegistry registry) {
		Minecraft minecraft = Minecraft.getInstance();
		ClientLevel level = minecraft.level;
		if (level == null) {
			throw new NullPointerException("level must not be null.");
		}
		RegistryAccess registryAccess = level.registryAccess();

		List<RecipeHolder<FreezingRecipe>> freezingHolders = registry.getRecipeManager().getAllRecipesFor(ForceRecipes.FREEZING.get());
		freezingHolders.forEach((holder) -> {
			FreezingRecipe recipe = holder.value();
			registry.add(new FreezingDisplay(
					recipe.getIngredients().getFirst(),
					recipe.getRecipeOutputs()
			));
		});

		List<RecipeHolder<GrindingRecipe>> grindingHolders = registry.getRecipeManager().getAllRecipesFor(ForceRecipes.GRINDING.get());
		grindingHolders.forEach((holder) -> {
			GrindingRecipe recipe = holder.value();
			registry.add(new GrindingDisplay(
					recipe.getIngredients().getFirst(),
					recipe.getRecipeOutputs()
			));
		});

		List<RecipeHolder<InfuseRecipe>> infuserHolders = registry.getRecipeManager().getAllRecipesFor(ForceRecipes.INFUSER_TYPE.get());
		infuserHolders.forEach((holder) -> {
			InfuseRecipe recipe = holder.value();
			registry.add(new InfuserDisplay(
					recipe.getCenter(), recipe.getIngredient(), recipe.getModifier(),
					recipe.getTier(), recipe.getResultItem(registryAccess), recipe.getTime()
			));
		});
	}
}
