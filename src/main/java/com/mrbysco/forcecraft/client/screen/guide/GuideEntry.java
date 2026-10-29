package com.mrbysco.forcecraft.client.screen.guide;

import com.mrbysco.forcecraft.client.ForceRecipeCache;
import com.mrbysco.forcecraft.recipe.InfuseRecipe;
import com.mrbysco.forcecraft.upgrade.UpgradeBookTier;
import net.minecraft.network.chat.Component;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public record GuideEntry(ItemStack stack, Component vagueHint, Component realText) {

	public static List<GuideEntry> buildEntries(UpgradeBookTier tier) {
		List<GuideEntry> entries = new ArrayList<>();

		Collection<RecipeHolder<InfuseRecipe>> infuseRecipes = ForceRecipeCache.getInfuseRecipes();
		List<InfuseRecipe> validRecipes = new ArrayList<>();
		for (RecipeHolder<InfuseRecipe> holder : infuseRecipes) {
			InfuseRecipe recipe = holder.value();
			if (recipe.getTier().asInt() <= tier.asInt()) {
				validRecipes.add(recipe);
			}
		}
		validRecipes.removeIf(recipe -> recipe.getIngredient().display().resolveForFirstStack(ContextMap.EMPTY).isEmpty());
		for (InfuseRecipe recipe : validRecipes) {
			entries.add(
					new GuideEntry(
							recipe.getIngredient().display().resolveForFirstStack(ContextMap.EMPTY),
							Component.literal("This is vague. "),
							Component.literal("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum")
					)
			);
		}

		return entries;
	}
}
