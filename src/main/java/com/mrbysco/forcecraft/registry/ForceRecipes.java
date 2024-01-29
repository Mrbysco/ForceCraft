package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.recipe.FreezingRecipe;
import com.mrbysco.forcecraft.recipe.GrindingRecipe;
import com.mrbysco.forcecraft.recipe.InfuseRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ForceRecipes {
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, Reference.MOD_ID);

	public static final Supplier<RecipeType<InfuseRecipe>> INFUSER_TYPE = RECIPE_TYPES.register("infuser", () -> new RecipeType<>() {
	});
	public static final Supplier<RecipeType<FreezingRecipe>> FREEZING = RECIPE_TYPES.register("freezing", () -> new RecipeType<>() {
	});
	public static final Supplier<RecipeType<GrindingRecipe>> GRINDING = RECIPE_TYPES.register("grinding", () -> new RecipeType<>() {
	});
}
