package com.mrbysco.forcecraft.recipe;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForceRecipes {
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Reference.MOD_ID);

	public static final RegistryObject<RecipeType<InfuseRecipe>> INFUSER_TYPE = RECIPE_TYPES.register("infuser", () -> new RecipeType<>() {
	});
	public static final RegistryObject<RecipeType<FreezingRecipe>> FREEZING = RECIPE_TYPES.register("freezing", () -> new RecipeType<>() {
	});
	public static final RegistryObject<RecipeType<GrindingRecipe>> GRINDING = RECIPE_TYPES.register("grinding", () -> new RecipeType<>() {
	});
}
