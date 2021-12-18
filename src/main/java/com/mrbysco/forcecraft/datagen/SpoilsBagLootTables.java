package com.mrbysco.forcecraft.datagen;

import com.mrbysco.forcecraft.registry.ForceTables;
import net.minecraft.data.loot.GiftLootTables;
import net.minecraft.item.Items;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiConsumer;

import static com.mrbysco.forcecraft.registry.ForceRegistry.*;

public class SpoilsBagLootTables extends GiftLootTables {

	@Override
	public void accept(BiConsumer<ResourceLocation, Builder> consumer) {
		consumer.accept(ForceTables.TIER_1, LootTable.lootTable()
				.withPool(LootPool.lootPool().setRolls(RandomValueRange.between(1.0F, 4.0F))
						.name("Force Loot")
						.add(ItemLootEntry.lootTableItem(FORCE_MITT.get()).setWeight(15))
						.add(ItemLootEntry.lootTableItem(BUCKET_FLUID_FORCE.get()).setWeight(12))
						.add(ItemLootEntry.lootTableItem(FORCE_GEM.get()).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(1.0F, 5.0F))))
						.add(ItemLootEntry.lootTableItem(FORCE_INGOT.get()).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1.0F, 8.0F))))
						.add(ItemLootEntry.lootTableItem(FORCE_INGOT.get()).setWeight(15).apply(SetCount.setCount(RandomValueRange.between(3.0F, 8.0F))))
						.add(ItemLootEntry.lootTableItem(FORCE_ARROW.get()).setWeight(15).apply(SetCount.setCount(RandomValueRange.between(3.0F, 11.0F))))
						.add(ItemLootEntry.lootTableItem(FORTUNE_COOKIE.get()).setWeight(10))
						.add(ItemLootEntry.lootTableItem(FORCE_SAPLING.get()).setWeight(12))
						.add(ItemLootEntry.lootTableItem(BACONATOR.get()).setWeight(10))
						.add(ItemLootEntry.lootTableItem(Items.CAKE).setWeight(6))
				)
				.withPool(LootPool.lootPool()
						.name("Other tables")
						.add(TableLootEntry.lootTableReference(LootTables.VILLAGE_TOOLSMITH))
						.add(TableLootEntry.lootTableReference(LootTables.VILLAGE_WEAPONSMITH))
						.add(TableLootEntry.lootTableReference(LootTables.SPAWN_BONUS_CHEST)))
		);

		consumer.accept(ForceTables.TIER_2, LootTable.lootTable()
				.withPool(LootPool.lootPool().setRolls(RandomValueRange.between(1.0F, 4.0F))
						.name("Force Loot")
						.add(ItemLootEntry.lootTableItem(FORCE_MITT.get()).setWeight(15))
						.add(ItemLootEntry.lootTableItem(BUCKET_FLUID_FORCE.get()).setWeight(12))
						.add(ItemLootEntry.lootTableItem(FORCE_GEM.get()).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(1.0F, 5.0F))))
						.add(ItemLootEntry.lootTableItem(FORCE_INGOT.get()).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1.0F, 8.0F))))
						.add(ItemLootEntry.lootTableItem(FORCE_INGOT.get()).setWeight(15).apply(SetCount.setCount(RandomValueRange.between(3.0F, 8.0F))))
						.add(ItemLootEntry.lootTableItem(FORCE_ARROW.get()).setWeight(15).apply(SetCount.setCount(RandomValueRange.between(3.0F, 11.0F))))
						.add(ItemLootEntry.lootTableItem(FORTUNE_COOKIE.get()).setWeight(10))
						.add(ItemLootEntry.lootTableItem(LIFE_CARD.get()).setWeight(1))
						.add(ItemLootEntry.lootTableItem(DARKNESS_CARD.get()).setWeight(1))
						.add(ItemLootEntry.lootTableItem(UNDEATH_CARD.get()).setWeight(1))
				)
				.withPool(LootPool.lootPool()
						.name("Other Tables")
						.add(TableLootEntry.lootTableReference(LootTables.SIMPLE_DUNGEON))
						.add(TableLootEntry.lootTableReference(LootTables.DESERT_PYRAMID))
						.add(TableLootEntry.lootTableReference(LootTables.JUNGLE_TEMPLE))
						.add(TableLootEntry.lootTableReference(LootTables.NETHER_BRIDGE))
				)
		);

		consumer.accept(ForceTables.TIER_3, LootTable.lootTable()
				.withPool(LootPool.lootPool().setRolls(RandomValueRange.between(1.0F, 4.0F))
						.name("Force Loot")
						.add(ItemLootEntry.lootTableItem(FORCE_MITT.get()).setWeight(15))
						.add(ItemLootEntry.lootTableItem(BUCKET_FLUID_FORCE.get()).setWeight(12))
						.add(ItemLootEntry.lootTableItem(FORCE_GEM.get()).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(1.0F, 5.0F))))
						.add(ItemLootEntry.lootTableItem(FORCE_INGOT.get()).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1.0F, 8.0F))))
						.add(ItemLootEntry.lootTableItem(FORCE_INGOT.get()).setWeight(15).apply(SetCount.setCount(RandomValueRange.between(3.0F, 8.0F))))
						.add(ItemLootEntry.lootTableItem(FORCE_PICKAXE.get()).setWeight(15))
						.add(ItemLootEntry.lootTableItem(FORCE_AXE.get()).setWeight(15))
						.add(ItemLootEntry.lootTableItem(FORCE_SWORD.get()).setWeight(15))
						.add(ItemLootEntry.lootTableItem(FORCE_SHEARS.get()).setWeight(15))
						.add(ItemLootEntry.lootTableItem(FORCE_SHOVEL.get()).setWeight(15))
						.add(ItemLootEntry.lootTableItem(LIFE_CARD.get()).setWeight(1))
						.add(ItemLootEntry.lootTableItem(DARKNESS_CARD.get()).setWeight(1))
						.add(ItemLootEntry.lootTableItem(UNDEATH_CARD.get()).setWeight(1))
				)
				.withPool(LootPool.lootPool()
						.name("Other Tables")
						.add(TableLootEntry.lootTableReference(LootTables.ABANDONED_MINESHAFT))
						.add(TableLootEntry.lootTableReference(LootTables.STRONGHOLD_CORRIDOR))
						.add(TableLootEntry.lootTableReference(LootTables.STRONGHOLD_LIBRARY))
						.add(TableLootEntry.lootTableReference(LootTables.STRONGHOLD_CROSSING))
						.add(TableLootEntry.lootTableReference(LootTables.BASTION_TREASURE))
						.add(TableLootEntry.lootTableReference(LootTables.BURIED_TREASURE))
						.add(TableLootEntry.lootTableReference(LootTables.END_CITY_TREASURE))
				)
		);
	}
}
