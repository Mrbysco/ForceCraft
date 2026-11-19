package com.mrbysco.forcecraft.datagen.data;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.lootmodifiers.SmeltingModifier;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
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
		this.add("smelting", new SmeltingModifier(
				new LootItemCondition[]{
						MatchTool.toolMatches(ItemPredicate.Builder.item().of(ForceTags.Items.FORCE_TOOLS)).build()
				})
		);
	}
}
