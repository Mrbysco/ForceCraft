package com.mrbysco.forcecraft.datagen.data;

import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.mrbysco.forcecraft.registry.ForceRegistry.BLACK_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.BLUE_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.BROWN_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.CYAN_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.DEEPSLATE_POWER_ORE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BLACK_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BLUE_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BLACK;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BLACK_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BLACK_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BLUE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BLUE_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BLUE_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BROWN;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BROWN_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BROWN_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_CYAN;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_CYAN_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_CYAN_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_GRAY;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_GRAY_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_GRAY_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_GREEN;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_GREEN_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_GREEN_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIGHT_BLUE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIGHT_BLUE_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIGHT_BLUE_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIGHT_GRAY;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIGHT_GRAY_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIGHT_GRAY_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIME;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIME_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIME_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_MAGENTA;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_MAGENTA_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_MAGENTA_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_ORANGE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_ORANGE_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_ORANGE_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_PINK;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_PINK_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_PINK_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_PURPLE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_PURPLE_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_PURPLE_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_RED;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_RED_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_RED_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_WHITE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_WHITE_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_WHITE_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_YELLOW;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_YELLOW_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_YELLOW_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BROWN_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_CYAN_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_ENGINE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_GEM;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_GRAY_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_GREEN_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_LEAVES;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_LIGHT_BLUE_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_LIGHT_GRAY_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_LIME_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_LOG;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_MAGENTA_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_ORANGE_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_PINK_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_PLANKS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_PLANK_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_PLANK_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_PURPLE_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_RED_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_SAPLING;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_WHITE_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_WOOD;
import static com.mrbysco.forcecraft.registry.ForceRegistry.GRAY_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.GREEN_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.INFUSER;
import static com.mrbysco.forcecraft.registry.ForceRegistry.LIGHT_BLUE_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.LIGHT_GRAY_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.LIME_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.MAGENTA_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.ORANGE_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.PINK_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.POWER_ORE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.POWER_ORE_ITEM;
import static com.mrbysco.forcecraft.registry.ForceRegistry.PURPLE_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.RED_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.TIME_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.WALL_FORCE_BLACK_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.WALL_FORCE_BLUE_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.WALL_FORCE_BROWN_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.WALL_FORCE_CYAN_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.WALL_FORCE_GRAY_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.WALL_FORCE_GREEN_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.WALL_FORCE_LIGHT_BLUE_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.WALL_FORCE_LIGHT_GRAY_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.WALL_FORCE_LIME_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.WALL_FORCE_MAGENTA_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.WALL_FORCE_ORANGE_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.WALL_FORCE_PINK_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.WALL_FORCE_PURPLE_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.WALL_FORCE_RED_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.WALL_FORCE_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.WALL_FORCE_WHITE_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.WALL_TIME_TORCH;
import static com.mrbysco.forcecraft.registry.ForceRegistry.WHITE_FORCE_FURNACE;

public class ForceLoot extends LootTableProvider {
	public ForceLoot(PackOutput packOutput) {
		super(packOutput, Set.of(), List.of(
				new SubProviderEntry(ForceBlocks::new, LootContextParamSets.BLOCK),
				new SubProviderEntry(SpoilsBagLootTables::new, LootContextParamSets.GIFT)
		));
	}

	private static class ForceBlocks extends BlockLootSubProvider {

		protected ForceBlocks() {
			super(Set.of(), FeatureFlags.REGISTRY.allFlags());
		}

		@Override
		protected void generate() {
			add(POWER_ORE.get(), (ore) -> createSilkTouchDispatchTable(ore, applyExplosionDecay(POWER_ORE_ITEM.get(), LootItem.lootTableItem(FORCE_GEM.get())
					.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)))));
			add(DEEPSLATE_POWER_ORE.get(), (ore) -> createSilkTouchDispatchTable(ore, applyExplosionDecay(POWER_ORE_ITEM.get(), LootItem.lootTableItem(FORCE_GEM.get())
					.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)))));

			dropSelf(INFUSER.get());
			add(FORCE_FURNACE.get(), createNameableBlockEntityTable(FORCE_FURNACE.get()));
			add(BLACK_FORCE_FURNACE.get(), createNameableBlockEntityTable(BLACK_FORCE_FURNACE.get()));
			add(BLUE_FORCE_FURNACE.get(), createNameableBlockEntityTable(BLUE_FORCE_FURNACE.get()));
			add(BROWN_FORCE_FURNACE.get(), createNameableBlockEntityTable(BROWN_FORCE_FURNACE.get()));
			add(CYAN_FORCE_FURNACE.get(), createNameableBlockEntityTable(CYAN_FORCE_FURNACE.get()));
			add(GRAY_FORCE_FURNACE.get(), createNameableBlockEntityTable(GRAY_FORCE_FURNACE.get()));
			add(GREEN_FORCE_FURNACE.get(), createNameableBlockEntityTable(GREEN_FORCE_FURNACE.get()));
			add(LIGHT_BLUE_FORCE_FURNACE.get(), createNameableBlockEntityTable(LIGHT_BLUE_FORCE_FURNACE.get()));
			add(LIGHT_GRAY_FORCE_FURNACE.get(), createNameableBlockEntityTable(LIGHT_GRAY_FORCE_FURNACE.get()));
			add(LIME_FORCE_FURNACE.get(), createNameableBlockEntityTable(LIME_FORCE_FURNACE.get()));
			add(MAGENTA_FORCE_FURNACE.get(), createNameableBlockEntityTable(MAGENTA_FORCE_FURNACE.get()));
			add(ORANGE_FORCE_FURNACE.get(), createNameableBlockEntityTable(ORANGE_FORCE_FURNACE.get()));
			add(PINK_FORCE_FURNACE.get(), createNameableBlockEntityTable(PINK_FORCE_FURNACE.get()));
			add(PURPLE_FORCE_FURNACE.get(), createNameableBlockEntityTable(PURPLE_FORCE_FURNACE.get()));
			add(RED_FORCE_FURNACE.get(), createNameableBlockEntityTable(RED_FORCE_FURNACE.get()));
			add(WHITE_FORCE_FURNACE.get(), createNameableBlockEntityTable(WHITE_FORCE_FURNACE.get()));

			add(FORCE_ENGINE.get(), createNameableBlockEntityTable(FORCE_ENGINE.get()));

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
			add(FORCE_PLANK_SLAB.get(), createSlabItemTable(FORCE_PLANK_SLAB.get()));
			add(FORCE_BRICK_RED_SLAB.get(), createSlabItemTable(FORCE_BRICK_RED_SLAB.get()));
			add(FORCE_BRICK_YELLOW_SLAB.get(), createSlabItemTable(FORCE_BRICK_YELLOW_SLAB.get()));
			add(FORCE_BRICK_GREEN_SLAB.get(), createSlabItemTable(FORCE_BRICK_GREEN_SLAB.get()));
			add(FORCE_BRICK_BLUE_SLAB.get(), createSlabItemTable(FORCE_BRICK_BLUE_SLAB.get()));
			add(FORCE_BRICK_WHITE_SLAB.get(), createSlabItemTable(FORCE_BRICK_WHITE_SLAB.get()));
			add(FORCE_BRICK_BLACK_SLAB.get(), createSlabItemTable(FORCE_BRICK_BLACK_SLAB.get()));
			add(FORCE_BRICK_BROWN_SLAB.get(), createSlabItemTable(FORCE_BRICK_BROWN_SLAB.get()));
			add(FORCE_BRICK_ORANGE_SLAB.get(), createSlabItemTable(FORCE_BRICK_ORANGE_SLAB.get()));
			add(FORCE_BRICK_LIGHT_BLUE_SLAB.get(), createSlabItemTable(FORCE_BRICK_LIGHT_BLUE_SLAB.get()));
			add(FORCE_BRICK_MAGENTA_SLAB.get(), createSlabItemTable(FORCE_BRICK_MAGENTA_SLAB.get()));
			add(FORCE_BRICK_PINK_SLAB.get(), createSlabItemTable(FORCE_BRICK_PINK_SLAB.get()));
			add(FORCE_BRICK_LIGHT_GRAY_SLAB.get(), createSlabItemTable(FORCE_BRICK_LIGHT_GRAY_SLAB.get()));
			add(FORCE_BRICK_LIME_SLAB.get(), createSlabItemTable(FORCE_BRICK_LIME_SLAB.get()));
			add(FORCE_BRICK_CYAN_SLAB.get(), createSlabItemTable(FORCE_BRICK_CYAN_SLAB.get()));
			add(FORCE_BRICK_PURPLE_SLAB.get(), createSlabItemTable(FORCE_BRICK_PURPLE_SLAB.get()));
			add(FORCE_BRICK_GRAY_SLAB.get(), createSlabItemTable(FORCE_BRICK_GRAY_SLAB.get()));
			add(FORCE_BRICK_SLAB.get(), createSlabItemTable(FORCE_BRICK_SLAB.get()));
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
