package com.mrbysco.forcecraft.datagen.server;

import com.mrbysco.forcecraft.registry.ForceDatamaps;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ForceDataMapProvider extends DataMapProvider {

	public ForceDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	protected void gather(HolderLookup.Provider provider) {
		compostables(ForceRegistry.FORCE_LEAVES.asItem(), 0.3F);
		compostables(ForceRegistry.FORCE_SAPLING.asItem(), 0.3F);

		final Builder<Block, Block> iceConversions = builder(ForceDatamaps.ICE_CONVERSIONS);
		iceConversions.add(Blocks.WATER.builtInRegistryHolder(), Blocks.ICE, false);
		iceConversions.add(Blocks.LAVA.builtInRegistryHolder(), Blocks.COBBLESTONE, false);
		iceConversions.add(Blocks.COBBLESTONE.builtInRegistryHolder(), Blocks.STONE, false);
		iceConversions.add(Blocks.STONE.builtInRegistryHolder(), Blocks.STONE_BRICKS, false);

		final Builder<Block, Block> rodConversions = builder(ForceDatamaps.ROD_CONVERSIONS);
		List<Block> saplings = List.of(
				Blocks.OAK_SAPLING, Blocks.SPRUCE_SAPLING, Blocks.BIRCH_SAPLING,
				Blocks.JUNGLE_SAPLING, Blocks.ACACIA_SAPLING, Blocks.DARK_OAK_SAPLING,
				Blocks.PALE_OAK_SAPLING, Blocks.AZALEA, Blocks.FLOWERING_AZALEA,
				Blocks.MANGROVE_PROPAGULE, Blocks.CHERRY_SAPLING
		);
		saplings.forEach(sapling -> rodConversions.add(sapling.builtInRegistryHolder(), ForceRegistry.FORCE_SAPLING.value(), false));
	}

	public void compostables(Item item, float chance) {
		this.builder(NeoForgeDataMaps.COMPOSTABLES).add(BuiltInRegistries.ITEM.getKey(item), new Compostable(chance), false);
	}
}
