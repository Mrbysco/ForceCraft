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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record GuideEntry(ItemStack stack, Component vagueHint, Component realText, Component header,
                         boolean discovered) {
	public static List<GuideEntry> buildEntries(UpgradeBookData book, ContextMap context) {
		Map<String, GuideEntry> entries = new LinkedHashMap<>();
		for (RecipeHolder<InfuseRecipe> holder : ForceRecipeCache.getInfuseRecipes()) {
			InfuseRecipe recipe = holder.value();
			if (recipe.getTier().asInt() > book.tier().asInt()) continue;

			ItemStack stack = recipe.getIngredient().display().resolveForFirstStack(context);
			if (stack.isEmpty()) continue;

			String key = guideKey(holder, recipe);
			Identifier usedId = recipe.group().isEmpty()
					? holder.id().identifier()
					: holder.id().identifier().withPath(recipe.group());
			boolean discovered = recipe.getTier().asInt() < book.tier().asInt()
					|| book.recipesUsed().contains(usedId);

			GuideEntry existing = entries.get(key);
			if (existing != null) {
				if (discovered && !existing.discovered()) {
					entries.put(key, new GuideEntry(existing.stack(), existing.vagueHint(), existing.realText(), existing.header(), true));
				}
				continue;
			}
			entries.put(key, new GuideEntry(stack,
					Component.translatable("guide.forcecraft.infuser." + key + ".hint"),
					Component.translatable("guide.forcecraft.infuser." + key + ".text"),
					Component.translatable("guide.forcecraft.infuser." + key + ".header"),
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
