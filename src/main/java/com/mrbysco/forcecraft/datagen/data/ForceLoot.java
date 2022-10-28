package com.mrbysco.forcecraft.datagen.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.mrbysco.forcecraft.registry.ForceRegistry.*;

public class ForceLoot extends LootTableProvider {
	public ForceLoot(DataGenerator gen) {
		super(gen);
	}

	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
		return ImmutableList.of(Pair.of(ForceBlocks::new, LootParameterSets.BLOCK), Pair.of(SpoilsBagLootTables::new, LootParameterSets.GIFT));
	}

	private static class ForceBlocks extends BlockLootTables {
		@Override
		protected void addTables() {
			add(POWER_ORE.get(), (ore) -> {
				return createSilkTouchDispatchTable(ore, applyExplosionDecay(POWER_ORE_ITEM.get(), ItemLootEntry.lootTableItem(FORCE_GEM.get())
						.apply(SetCount.setCount(RandomValueRange.between(2.0F, 4.0F)))
						.apply(ApplyBonus.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
			});

			dropSelf(INFUSER.get());
			add(FORCE_FURNACE.get(), BlockLootTables::createNameableBlockEntityTable);
			add(BLACK_FORCE_FURNACE.get(), BlockLootTables::createNameableBlockEntityTable);
			add(BLUE_FORCE_FURNACE.get(), BlockLootTables::createNameableBlockEntityTable);
			add(BROWN_FORCE_FURNACE.get(), BlockLootTables::createNameableBlockEntityTable);
			add(CYAN_FORCE_FURNACE.get(), BlockLootTables::createNameableBlockEntityTable);
			add(GRAY_FORCE_FURNACE.get(), BlockLootTables::createNameableBlockEntityTable);
			add(GREEN_FORCE_FURNACE.get(), BlockLootTables::createNameableBlockEntityTable);
			add(LIGHT_BLUE_FORCE_FURNACE.get(), BlockLootTables::createNameableBlockEntityTable);
			add(LIGHT_GRAY_FORCE_FURNACE.get(), BlockLootTables::createNameableBlockEntityTable);
			add(LIME_FORCE_FURNACE.get(), BlockLootTables::createNameableBlockEntityTable);
			add(MAGENTA_FORCE_FURNACE.get(), BlockLootTables::createNameableBlockEntityTable);
			add(ORANGE_FORCE_FURNACE.get(), BlockLootTables::createNameableBlockEntityTable);
			add(PINK_FORCE_FURNACE.get(), BlockLootTables::createNameableBlockEntityTable);
			add(PURPLE_FORCE_FURNACE.get(), BlockLootTables::createNameableBlockEntityTable);
			add(RED_FORCE_FURNACE.get(), BlockLootTables::createNameableBlockEntityTable);
			add(WHITE_FORCE_FURNACE.get(), BlockLootTables::createNameableBlockEntityTable);

			add(FORCE_ENGINE.get(), BlockLootTables::createNameableBlockEntityTable);

			dropSelf(FORCE_SAPLING.get());
			dropSelf(FORCE_LOG.get());
			dropSelf(FORCE_WOOD.get());
			dropSelf(FORCE_PLANKS.get());
			add(FORCE_LEAVES.get(), (leaves) -> createOakLeavesDrops(leaves, FORCE_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));

			dropSelf(FORCE_TORCH.get());
			dropSelf(FORCE_RED_TORCH.get());
			dropSelf(FORCE_ORANGE_TORCH.get());
			dropSelf(FORCE_GREEN_TORCH.get());
			dropSelf(FORCE_BLUE_TORCH.get());
			dropSelf(FORCE_WHITE_TORCH.get());
			dropSelf(FORCE_BLACK_TORCH.get());
			dropSelf(FORCE_BROWN_TORCH.get());
			dropSelf(FORCE_LIGHT_BLUE_TORCH.get());
			dropSelf(FORCE_MAGENTA_TORCH.get());
			dropSelf(FORCE_PINK_TORCH.get());
			dropSelf(FORCE_LIGHT_GRAY_TORCH.get());
			dropSelf(FORCE_LIME_TORCH.get());
			dropSelf(FORCE_CYAN_TORCH.get());
			dropSelf(FORCE_PURPLE_TORCH.get());
			dropSelf(FORCE_GRAY_TORCH.get());
			dropSelf(TIME_TORCH.get());

			dropOther(WALL_FORCE_TORCH.get(), FORCE_TORCH.get());
			dropOther(WALL_FORCE_RED_TORCH.get(), FORCE_RED_TORCH.get());
			dropOther(WALL_FORCE_ORANGE_TORCH.get(), FORCE_ORANGE_TORCH.get());
			dropOther(WALL_FORCE_GREEN_TORCH.get(), FORCE_GREEN_TORCH.get());
			dropOther(WALL_FORCE_BLUE_TORCH.get(), FORCE_BLUE_TORCH.get());
			dropOther(WALL_FORCE_WHITE_TORCH.get(), FORCE_WHITE_TORCH.get());
			dropOther(WALL_FORCE_BLACK_TORCH.get(), FORCE_BLACK_TORCH.get());
			dropOther(WALL_FORCE_BROWN_TORCH.get(), FORCE_BROWN_TORCH.get());
			dropOther(WALL_FORCE_LIGHT_BLUE_TORCH.get(), FORCE_LIGHT_BLUE_TORCH.get());
			dropOther(WALL_FORCE_MAGENTA_TORCH.get(), FORCE_MAGENTA_TORCH.get());
			dropOther(WALL_FORCE_PINK_TORCH.get(), FORCE_PINK_TORCH.get());
			dropOther(WALL_FORCE_LIGHT_GRAY_TORCH.get(), FORCE_LIGHT_GRAY_TORCH.get());
			dropOther(WALL_FORCE_LIME_TORCH.get(), FORCE_LIME_TORCH.get());
			dropOther(WALL_FORCE_CYAN_TORCH.get(), FORCE_CYAN_TORCH.get());
			dropOther(WALL_FORCE_PURPLE_TORCH.get(), FORCE_PURPLE_TORCH.get());
			dropOther(WALL_FORCE_GRAY_TORCH.get(), FORCE_GRAY_TORCH.get());
			dropSelf(WALL_TIME_TORCH.get());

			//Bricks
			dropSelf(FORCE_BRICK_RED.get());
			dropSelf(FORCE_BRICK_YELLOW.get());
			dropSelf(FORCE_BRICK_GREEN.get());
			dropSelf(FORCE_BRICK_BLUE.get());
			dropSelf(FORCE_BRICK_WHITE.get());
			dropSelf(FORCE_BRICK_BLACK.get());
			dropSelf(FORCE_BRICK_BROWN.get());
			dropSelf(FORCE_BRICK_ORANGE.get());
			dropSelf(FORCE_BRICK_LIGHT_BLUE.get());
			dropSelf(FORCE_BRICK_MAGENTA.get());
			dropSelf(FORCE_BRICK_PINK.get());
			dropSelf(FORCE_BRICK_LIGHT_GRAY.get());
			dropSelf(FORCE_BRICK_LIME.get());
			dropSelf(FORCE_BRICK_CYAN.get());
			dropSelf(FORCE_BRICK_PURPLE.get());
			dropSelf(FORCE_BRICK_GRAY.get());
			dropSelf(FORCE_BRICK.get());

			//Stairs
			dropSelf(FORCE_PLANK_STAIRS.get());
			dropSelf(FORCE_BRICK_RED_STAIRS.get());
			dropSelf(FORCE_BRICK_YELLOW_STAIRS.get());
			dropSelf(FORCE_BRICK_GREEN_STAIRS.get());
			dropSelf(FORCE_BRICK_BLUE_STAIRS.get());
			dropSelf(FORCE_BRICK_WHITE_STAIRS.get());
			dropSelf(FORCE_BRICK_BLACK_STAIRS.get());
			dropSelf(FORCE_BRICK_BROWN_STAIRS.get());
			dropSelf(FORCE_BRICK_ORANGE_STAIRS.get());
			dropSelf(FORCE_BRICK_LIGHT_BLUE_STAIRS.get());
			dropSelf(FORCE_BRICK_MAGENTA_STAIRS.get());
			dropSelf(FORCE_BRICK_PINK_STAIRS.get());
			dropSelf(FORCE_BRICK_LIGHT_GRAY_STAIRS.get());
			dropSelf(FORCE_BRICK_LIME_STAIRS.get());
			dropSelf(FORCE_BRICK_CYAN_STAIRS.get());
			dropSelf(FORCE_BRICK_PURPLE_STAIRS.get());
			dropSelf(FORCE_BRICK_GRAY_STAIRS.get());
			dropSelf(FORCE_BRICK_STAIRS.get());

			//Slabs
			add(FORCE_PLANK_SLAB.get(), BlockLootTables::createSlabItemTable);
			add(FORCE_BRICK_RED_SLAB.get(), BlockLootTables::createSlabItemTable);
			add(FORCE_BRICK_YELLOW_SLAB.get(), BlockLootTables::createSlabItemTable);
			add(FORCE_BRICK_GREEN_SLAB.get(), BlockLootTables::createSlabItemTable);
			add(FORCE_BRICK_BLUE_SLAB.get(), BlockLootTables::createSlabItemTable);
			add(FORCE_BRICK_WHITE_SLAB.get(), BlockLootTables::createSlabItemTable);
			add(FORCE_BRICK_BLACK_SLAB.get(), BlockLootTables::createSlabItemTable);
			add(FORCE_BRICK_BROWN_SLAB.get(), BlockLootTables::createSlabItemTable);
			add(FORCE_BRICK_ORANGE_SLAB.get(), BlockLootTables::createSlabItemTable);
			add(FORCE_BRICK_LIGHT_BLUE_SLAB.get(), BlockLootTables::createSlabItemTable);
			add(FORCE_BRICK_MAGENTA_SLAB.get(), BlockLootTables::createSlabItemTable);
			add(FORCE_BRICK_PINK_SLAB.get(), BlockLootTables::createSlabItemTable);
			add(FORCE_BRICK_LIGHT_GRAY_SLAB.get(), BlockLootTables::createSlabItemTable);
			add(FORCE_BRICK_LIME_SLAB.get(), BlockLootTables::createSlabItemTable);
			add(FORCE_BRICK_CYAN_SLAB.get(), BlockLootTables::createSlabItemTable);
			add(FORCE_BRICK_PURPLE_SLAB.get(), BlockLootTables::createSlabItemTable);
			add(FORCE_BRICK_GRAY_SLAB.get(), BlockLootTables::createSlabItemTable);
			add(FORCE_BRICK_SLAB.get(), BlockLootTables::createSlabItemTable);
		}

		@Override
		protected Iterable<Block> getKnownBlocks() {
			return (Iterable<Block>) ForceRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
		}
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationTracker) {
//		map.forEach((location, lootTable) -> {
//			LootTables.validate(validationContext, location, lootTable);
//		});
	}
}
