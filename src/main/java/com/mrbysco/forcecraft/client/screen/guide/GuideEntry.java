package com.mrbysco.forcecraft.client.screen.guide;

import com.mrbysco.forcecraft.client.ForceRecipeCache;
import com.mrbysco.forcecraft.recipe.InfuseRecipe;
import com.mrbysco.forcecraft.upgrade.UpgradeBookData;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record GuideEntry(ItemStack stack, Component vagueHint, Component realText, Component header,
                         boolean discovered) {
	public static List<GuideEntry> buildEntries(UpgradeBookData book, ContextMap context) {
		Map<String, GuideEntry> entries = new LinkedHashMap<>();
		int bookTier = book.tier().asInt();

		List<RecipeHolder<InfuseRecipe>> recipes = new ArrayList<>(ForceRecipeCache.getInfuseRecipes());
		recipes.sort(Comparator.comparingInt(recipe -> recipe.value().getTier().ordinal()));

		for (RecipeHolder<InfuseRecipe> holder : recipes) {
			InfuseRecipe recipe = holder.value();
			int recipeTier = recipe.getTier().asInt();
			if (recipeTier > bookTier) continue;

			ItemStack stack = recipe.getIngredient().display().resolveForFirstStack(context);
			if (stack.isEmpty()) continue;

			Identifier recipeId = holder.id().identifier();
			String recipeGroup = recipe.group();
			Identifier usedId = recipeGroup.isEmpty() ? recipeId : recipeId.withPath(recipeGroup);
			boolean discovered = recipeTier < bookTier
					|| book.recipesUsed().contains(usedId);

			String key = guideKey(holder, recipe);
			GuideEntry existing = entries.get(key);
			if (existing != null) {
				if (discovered && !existing.discovered()) {
					entries.put(key, new GuideEntry(existing.stack(), existing.vagueHint(), existing.realText(), existing.header(), true));
				}
				continue;
			}
			entries.put(key, new GuideEntry(stack,
					Component.translatableWithFallback("guide.forcecraft.infuser." + key + ".hint", "Please ask the developer to add language keys for the " + key + " infusion."),
					Component.translatableWithFallback("guide.forcecraft.infuser." + key + ".text", "Please ask the developer to add language keys for the " + key + " infusion."),
					Component.translatableWithFallback("guide.forcecraft.infuser." + key + ".header", "Unknown Upgrade"),
					discovered)
			);
		}
		return new ArrayList<>(entries.values());
	}

	private static String guideKey(RecipeHolder<InfuseRecipe> holder, InfuseRecipe recipe) {
		if (!recipe.group().isEmpty()) {
			return recipe.group();
		}
		return holder.id().identifier().toLanguageKey().replace('/', '.');
	}
}
