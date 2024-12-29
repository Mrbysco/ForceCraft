package com.mrbysco.forcecraft.handlers;

import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class LootTableHandler {
	public static final ResourceKey<LootTable> BAT = EntityType.BAT.getDefaultLootTable();

	// To test use: /summon bat ~ ~ ~ {NoAI:1b}
	@SubscribeEvent
	public void onLootTableLoadEvent(LootTableLoadEvent event) {
		HolderLookup.Provider registries = ServerLifecycleHooks.getCurrentServer() != null ?
				ServerLifecycleHooks.getCurrentServer().registryAccess() : RegistryAccess.EMPTY;
		if (event.getName().equals(BAT.location())) {
			LootPool.Builder builder = LootPool.lootPool();
			builder.add(LootItem.lootTableItem(ForceRegistry.CLAW.get())
							.setWeight(1)
							.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
//							.apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(0.0F, 1.0F)))
					)
					.name("forcecraft_inject");
			builder.add(EmptyLootItem.emptyItem()
							.setWeight(1))
					.name("forcecraft_empty_roll");
			LootPool pool = builder.build();

			event.getTable().addPool(pool);
		}
	}
}
