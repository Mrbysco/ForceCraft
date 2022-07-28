package com.mrbysco.forcecraft.datagen.data;

import com.mrbysco.forcecraft.registry.ForceTables;
import net.minecraft.data.loot.GiftLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

import static com.mrbysco.forcecraft.registry.ForceRegistry.*;

public class SpoilsBagLootTables extends GiftLoot {

	@Override
	public void accept(BiConsumer<ResourceLocation, Builder> consumer) {
		consumer.accept(ForceTables.TIER_1, LootTable.lootTable()
				.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1.0F, 4.0F))
						.name("Force Loot")
						.add(LootItem.lootTableItem(FORCE_MITT.get()).setWeight(15))
						.add(LootItem.lootTableItem(BUCKET_FLUID_FORCE.get()).setWeight(12))
						.add(LootItem.lootTableItem(FORCE_GEM.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
						.add(LootItem.lootTableItem(FORCE_INGOT.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
						.add(LootItem.lootTableItem(FORCE_INGOT.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 8.0F))))
						.add(LootItem.lootTableItem(FORCE_ARROW.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 11.0F))))
						.add(LootItem.lootTableItem(FORTUNE_COOKIE.get()).setWeight(10))
						.add(LootItem.lootTableItem(FORCE_SAPLING.get()).setWeight(12))
						.add(LootItem.lootTableItem(BACONATOR.get()).setWeight(10))
						.add(LootItem.lootTableItem(Items.CAKE).setWeight(6))
				)
				.withPool(LootPool.lootPool()
						.name("Other tables")
						.add(LootTableReference.lootTableReference(BuiltInLootTables.VILLAGE_TOOLSMITH))
						.add(LootTableReference.lootTableReference(BuiltInLootTables.VILLAGE_WEAPONSMITH))
						.add(LootTableReference.lootTableReference(BuiltInLootTables.SPAWN_BONUS_CHEST)))
		);

		consumer.accept(ForceTables.TIER_2, LootTable.lootTable()
				.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1.0F, 4.0F))
						.name("Force Loot")
						.add(LootItem.lootTableItem(FORCE_MITT.get()).setWeight(15))
						.add(LootItem.lootTableItem(BUCKET_FLUID_FORCE.get()).setWeight(12))
						.add(LootItem.lootTableItem(FORCE_GEM.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
						.add(LootItem.lootTableItem(FORCE_INGOT.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
						.add(LootItem.lootTableItem(FORCE_INGOT.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 8.0F))))
						.add(LootItem.lootTableItem(FORCE_ARROW.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 11.0F))))
						.add(LootItem.lootTableItem(FORTUNE_COOKIE.get()).setWeight(10))
						.add(LootItem.lootTableItem(LIFE_CARD.get()).setWeight(1))
						.add(LootItem.lootTableItem(DARKNESS_CARD.get()).setWeight(1))
						.add(LootItem.lootTableItem(UNDEATH_CARD.get()).setWeight(1))
				)
				.withPool(LootPool.lootPool()
						.name("Other Tables")
						.add(LootTableReference.lootTableReference(BuiltInLootTables.SIMPLE_DUNGEON))
						.add(LootTableReference.lootTableReference(BuiltInLootTables.DESERT_PYRAMID))
						.add(LootTableReference.lootTableReference(BuiltInLootTables.JUNGLE_TEMPLE))
						.add(LootTableReference.lootTableReference(BuiltInLootTables.NETHER_BRIDGE))
				)
		);

		consumer.accept(ForceTables.TIER_3, LootTable.lootTable()
				.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1.0F, 4.0F))
						.name("Force Loot")
						.add(LootItem.lootTableItem(FORCE_MITT.get()).setWeight(15))
						.add(LootItem.lootTableItem(BUCKET_FLUID_FORCE.get()).setWeight(12))
						.add(LootItem.lootTableItem(FORCE_GEM.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
						.add(LootItem.lootTableItem(FORCE_INGOT.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
						.add(LootItem.lootTableItem(FORCE_INGOT.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 8.0F))))
						.add(LootItem.lootTableItem(FORCE_PICKAXE.get()).setWeight(15))
						.add(LootItem.lootTableItem(FORCE_AXE.get()).setWeight(15))
						.add(LootItem.lootTableItem(FORCE_SWORD.get()).setWeight(15))
						.add(LootItem.lootTableItem(FORCE_SHEARS.get()).setWeight(15))
						.add(LootItem.lootTableItem(FORCE_SHOVEL.get()).setWeight(15))
						.add(LootItem.lootTableItem(LIFE_CARD.get()).setWeight(1))
						.add(LootItem.lootTableItem(DARKNESS_CARD.get()).setWeight(1))
						.add(LootItem.lootTableItem(UNDEATH_CARD.get()).setWeight(1))
				)
				.withPool(LootPool.lootPool()
						.name("Other Tables")
						.add(LootTableReference.lootTableReference(BuiltInLootTables.ABANDONED_MINESHAFT))
						.add(LootTableReference.lootTableReference(BuiltInLootTables.STRONGHOLD_CORRIDOR))
						.add(LootTableReference.lootTableReference(BuiltInLootTables.STRONGHOLD_LIBRARY))
						.add(LootTableReference.lootTableReference(BuiltInLootTables.STRONGHOLD_CROSSING))
						.add(LootTableReference.lootTableReference(BuiltInLootTables.BASTION_TREASURE))
						.add(LootTableReference.lootTableReference(BuiltInLootTables.BURIED_TREASURE))
						.add(LootTableReference.lootTableReference(BuiltInLootTables.END_CITY_TREASURE))
				)
		);
	}
}
