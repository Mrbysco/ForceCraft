package com.mrbysco.forcecraft.datagen.server;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.loot.SmeltModifier;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;

import java.util.concurrent.CompletableFuture;

public class ForceLootModifierProvider extends GlobalLootModifierProvider {
	public ForceLootModifierProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider, Reference.MOD_ID);
	}

	@Override
	protected void start() {
		HolderGetter<Item> items = registries.lookupOrThrow(Registries.ITEM);

		this.add("smelting", new SmeltModifier(
				new LootItemCondition[]{
						MatchTool.toolMatches(ItemPredicate.Builder.item().of(items, ForceTags.Items.FORCE_TOOLS)).build()
				}, 1000)
		);
	}
}
