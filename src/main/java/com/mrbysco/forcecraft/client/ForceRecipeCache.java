package com.mrbysco.forcecraft.client;

import com.google.common.collect.ImmutableList;
import com.mrbysco.forcecraft.recipe.FreezingRecipe;
import com.mrbysco.forcecraft.recipe.GrindingRecipe;
import com.mrbysco.forcecraft.recipe.InfuseRecipe;
import com.mrbysco.forcecraft.registry.ForceRecipes;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EventBusSubscriber(Dist.CLIENT)
public class ForceRecipeCache {
	private static final List<RecipeHolder<InfuseRecipe>> infuseRecipes = new ArrayList<>();
	private static final List<RecipeHolder<FreezingRecipe>> freezingRecipes = new ArrayList<>();
	private static final List<RecipeHolder<GrindingRecipe>> grindingRecipes = new ArrayList<>();

	@SubscribeEvent
	public static void onRecipeReceived(final RecipesReceivedEvent event) {
		infuseRecipes.clear();
		Collection<RecipeHolder<InfuseRecipe>> infuseRecipes = event.getRecipeMap().byType(ForceRecipes.INFUSE.get());
		ForceRecipeCache.infuseRecipes.addAll(infuseRecipes);

		freezingRecipes.clear();
		Collection<RecipeHolder<FreezingRecipe>> freezingRecipes = event.getRecipeMap().byType(ForceRecipes.FREEZING.get());
		ForceRecipeCache.freezingRecipes.addAll(freezingRecipes);

		grindingRecipes.clear();
		Collection<RecipeHolder<GrindingRecipe>> grindingRecipes = event.getRecipeMap().byType(ForceRecipes.GRINDING.get());
		ForceRecipeCache.grindingRecipes.addAll(grindingRecipes);
	}

	public static Collection<RecipeHolder<InfuseRecipe>> getInfuseRecipes() {
		return ImmutableList.copyOf(infuseRecipes);
	}

	public static Collection<RecipeHolder<FreezingRecipe>> getFreezingRecipes() {
		return ImmutableList.copyOf(freezingRecipes);
	}

	public static Collection<RecipeHolder<GrindingRecipe>> getGrindingRecipes() {
		return ImmutableList.copyOf(grindingRecipes);
	}
}
