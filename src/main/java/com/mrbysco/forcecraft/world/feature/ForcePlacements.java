package com.mrbysco.forcecraft.world.feature;

import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public class ForcePlacements {
	public static final PlacedFeature ORE_FORCE = PlacementUtils.register("ore_force", OreFeatures.ORE_LAPIS.placed(commonOrePlacement(5, HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(48)))));
	public static final PlacedFeature ORE_FORCE_BURIED = PlacementUtils.register("ore_force_buried", OreFeatures.ORE_LAPIS_BURIED.placed(commonOrePlacement(3, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)))));

	public static final PlacedFeature FORCE_TREE_CHECKED = PlacementUtils.register("force_tree_checked",
			ForceFeatureConfigs.FORCE_TREE.placed(BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ForceRegistry.FORCE_SAPLING.get().defaultBlockState(), BlockPos.ZERO))));
	public static final PlacedFeature FORCE_TREE_BEES_002 = PlacementUtils.register("force_tree_bees_002",
			ForceFeatureConfigs.FORCE_TREE_WITH_MORE_BEEHIVES_CONFIG.filteredByBlockSurvival(Blocks.OAK_SAPLING));

	private static List<PlacementModifier> orePlacement(PlacementModifier modifier1, PlacementModifier modifier2) {
		return List.of(modifier1, InSquarePlacement.spread(), modifier2, BiomeFilter.biome());
	}

	private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier modifier) {
		return orePlacement(CountPlacement.of(count), modifier);
	}

	public static void initialize() {

	}
}