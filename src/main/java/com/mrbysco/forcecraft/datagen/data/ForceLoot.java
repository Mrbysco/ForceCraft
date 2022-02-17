package com.mrbysco.forcecraft.datagen.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
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
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootContextParamSet>> getTables() {
		return ImmutableList.of(Pair.of(ForceLoot.ForceBlocks::new, LootContextParamSets.BLOCK), Pair.of(SpoilsBagLootTables::new, LootContextParamSets.GIFT));
	}

	private static class ForceBlocks extends BlockLoot {
		@Override
		protected void addTables() {
			add(POWER_ORE.get(), (ore) -> createSilkTouchDispatchTable(ore, applyExplosionDecay(POWER_ORE_ITEM.get(), LootItem.lootTableItem(FORCE_GEM.get())
					.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)))));

			dropSelf(INFUSER.get());
			add(FORCE_FURNACE.get(), BlockLoot::createNameableBlockEntityTable);
			add(BLACK_FORCE_FURNACE.get(), BlockLoot::createNameableBlockEntityTable);
			add(BLUE_FORCE_FURNACE.get(), BlockLoot::createNameableBlockEntityTable);
			add(BROWN_FORCE_FURNACE.get(), BlockLoot::createNameableBlockEntityTable);
			add(CYAN_FORCE_FURNACE.get(), BlockLoot::createNameableBlockEntityTable);
			add(GRAY_FORCE_FURNACE.get(), BlockLoot::createNameableBlockEntityTable);
			add(GREEN_FORCE_FURNACE.get(), BlockLoot::createNameableBlockEntityTable);
			add(LIGHT_BLUE_FORCE_FURNACE.get(), BlockLoot::createNameableBlockEntityTable);
			add(LIGHT_GRAY_FORCE_FURNACE.get(), BlockLoot::createNameableBlockEntityTable);
			add(LIME_FORCE_FURNACE.get(), BlockLoot::createNameableBlockEntityTable);
			add(MAGENTA_FORCE_FURNACE.get(), BlockLoot::createNameableBlockEntityTable);
			add(ORANGE_FORCE_FURNACE.get(), BlockLoot::createNameableBlockEntityTable);
			add(PINK_FORCE_FURNACE.get(), BlockLoot::createNameableBlockEntityTable);
			add(PURPLE_FORCE_FURNACE.get(), BlockLoot::createNameableBlockEntityTable);
			add(RED_FORCE_FURNACE.get(), BlockLoot::createNameableBlockEntityTable);
			add(WHITE_FORCE_FURNACE.get(), BlockLoot::createNameableBlockEntityTable);

			add(FORCE_ENGINE.get(), BlockLoot::createNameableBlockEntityTable);

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
			add(FORCE_PLANK_SLAB.get(), BlockLoot::createSlabItemTable);
			add(FORCE_BRICK_RED_SLAB.get(), BlockLoot::createSlabItemTable);
			add(FORCE_BRICK_YELLOW_SLAB.get(), BlockLoot::createSlabItemTable);
			add(FORCE_BRICK_GREEN_SLAB.get(), BlockLoot::createSlabItemTable);
			add(FORCE_BRICK_BLUE_SLAB.get(), BlockLoot::createSlabItemTable);
			add(FORCE_BRICK_WHITE_SLAB.get(), BlockLoot::createSlabItemTable);
			add(FORCE_BRICK_BLACK_SLAB.get(), BlockLoot::createSlabItemTable);
			add(FORCE_BRICK_BROWN_SLAB.get(), BlockLoot::createSlabItemTable);
			add(FORCE_BRICK_ORANGE_SLAB.get(), BlockLoot::createSlabItemTable);
			add(FORCE_BRICK_LIGHT_BLUE_SLAB.get(), BlockLoot::createSlabItemTable);
			add(FORCE_BRICK_MAGENTA_SLAB.get(), BlockLoot::createSlabItemTable);
			add(FORCE_BRICK_PINK_SLAB.get(), BlockLoot::createSlabItemTable);
			add(FORCE_BRICK_LIGHT_GRAY_SLAB.get(), BlockLoot::createSlabItemTable);
			add(FORCE_BRICK_LIME_SLAB.get(), BlockLoot::createSlabItemTable);
			add(FORCE_BRICK_CYAN_SLAB.get(), BlockLoot::createSlabItemTable);
			add(FORCE_BRICK_PURPLE_SLAB.get(), BlockLoot::createSlabItemTable);
			add(FORCE_BRICK_GRAY_SLAB.get(), BlockLoot::createSlabItemTable);
			add(FORCE_BRICK_SLAB.get(), BlockLoot::createSlabItemTable);
		}

		@Override
		protected Iterable<Block> getKnownBlocks() {
			return (Iterable<Block>) ForceRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
		}
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, @Nonnull ValidationContext validationContext) {
//		map.forEach((location, lootTable) -> {
//			LootTables.validate(validationContext, location, lootTable);
//		});
	}
}
