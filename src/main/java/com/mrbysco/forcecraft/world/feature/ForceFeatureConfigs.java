package com.mrbysco.forcecraft.world.feature;

import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

import java.util.List;

public class ForceFeatureConfigs {
	private static final BlockState FORCE_LOG = ForceRegistry.FORCE_LOG.get().defaultBlockState();
	private static final BlockState FORCE_LEAVES = ForceRegistry.FORCE_LEAVES.get().defaultBlockState();
	private static final BlockState FORCE_ORE = ForceRegistry.POWER_ORE.get().defaultBlockState();
	private static final BlockState DEEPSLATE_FORCE_ORE = ForceRegistry.DEEPSLATE_POWER_ORE.get().defaultBlockState();
	private static final List<TargetBlockState> ORE_FORCE_TARGET_LIST =
			List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, FORCE_ORE),
					OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, DEEPSLATE_FORCE_ORE));

	public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_FORCE = FeatureUtils.register("forcecraft:ore_force", Feature.ORE, new OreConfiguration(ORE_FORCE_TARGET_LIST, 6));
	public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_FORCE_BURIED = FeatureUtils.register("forcecraft:ore_force_buried", Feature.ORE, new OreConfiguration(ORE_FORCE_TARGET_LIST, 6, 1.0F));

	public static final Holder<PlacedFeature> PLACED_ORE_FORCE = PlacementUtils.register("forcecraft:ore_force", ORE_FORCE, commonOrePlacement(5, HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(48))));
	public static final Holder<PlacedFeature> PLACED_ORE_FORCE_BURIED = PlacementUtils.register("forcecraft:ore_force_buried", ORE_FORCE_BURIED, commonOrePlacement(3, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64))));

	private static List<PlacementModifier> orePlacement(PlacementModifier modifier1, PlacementModifier modifier2) {
		return List.of(modifier1, InSquarePlacement.spread(), modifier2, BiomeFilter.biome());
	}

	private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier modifier) {
		return orePlacement(CountPlacement.of(count), modifier);
	}

	public static final Holder<ConfiguredFeature<TreeConfiguration, ?>> FORCE_TREE = FeatureUtils.register("forcecraft:force_tree",
			ForceFeatures.FORCE_TREE.get(), createForceTree().build());

	private static final BeehiveDecorator BEEHIVE_005 = new BeehiveDecorator(0.05F);
	public static final Holder<ConfiguredFeature<TreeConfiguration, ?>> FORCE_TREE_WITH_MORE_BEEHIVES_CONFIG = FeatureUtils.register("forcecraft:force_tree_with_bees",
			ForceFeatures.FORCE_TREE.get(), createForceTree().decorators(List.of(BEEHIVE_005)).build());

	public static final Holder<PlacedFeature> PLACED_FORCE_TREE = PlacementUtils.register("forcecraft:force_tree",
			ForceFeatureConfigs.FORCE_TREE, VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(3), ForceRegistry.FORCE_SAPLING.get()));
	public static final Holder<PlacedFeature> PLACED_FORCE_TREE_BEES_002 = PlacementUtils.register("forcecraft:force_tree_bees_002",
			ForceFeatureConfigs.FORCE_TREE_WITH_MORE_BEEHIVES_CONFIG, VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(3), ForceRegistry.FORCE_SAPLING.get()));

	public static final Holder<ConfiguredFeature<RandomFeatureConfiguration, ?>> TREES_FORCE = FeatureUtils.register("forcecraft:trees_force",
			Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
					List.of(new WeightedPlacedFeature(PLACED_FORCE_TREE_BEES_002, 0.2F)), PLACED_FORCE_TREE));

	private static TreeConfiguration.TreeConfigurationBuilder createStraightBlobTree(BlockState trunkState, BlockState foliageState, int baseHeight, int heightRandA, int heightRandB, int p_195152_) {
		return new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(trunkState), new StraightTrunkPlacer(baseHeight, heightRandA, heightRandB),
				BlockStateProvider.simple(foliageState), new BlobFoliagePlacer(ConstantInt.of(p_195152_), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1));
	}

	private static TreeConfiguration.TreeConfigurationBuilder createForceTree() {
		return createStraightBlobTree(FORCE_LOG, FORCE_LEAVES, 4, 2, 0, 2).ignoreVines();
	}

	public static void initialize() {

	}
}
