package com.mrbysco.forcecraft.recipe;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

public class ForceRecipes {
	public static void init() {

	}

	public static final RecipeType<InfuseRecipe> INFUSER_TYPE = RecipeType.register(new ResourceLocation(Reference.MOD_ID, "infuser").toString());
	public static final RecipeType<FreezingRecipe> FREEZING = RecipeType.register(new ResourceLocation(Reference.MOD_ID, "freezing").toString());
	public static final RecipeType<GrindingRecipe> GRINDING = RecipeType.register(new ResourceLocation(Reference.MOD_ID, "grinding").toString());
}
