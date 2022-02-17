package com.mrbysco.forcecraft.world.feature;

import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

import java.util.List;

public class ForceFeatureConfigs {
	private static final BeehiveDecorator MANY_BEEHIVES = new BeehiveDecorator(0.05F);
	private static final BlockState FORCE_LOG = ForceRegistry.FORCE_LOG.get().defaultBlockState();
	private static final BlockState FORCE_LEAVES = ForceRegistry.FORCE_LEAVES.get().defaultBlockState();
	private static final BlockState FORCE_ORE = ForceRegistry.POWER_ORE.get().defaultBlockState();
	private static final List<TargetBlockState> ORE_FORCE_TARGET_LIST =
			List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, FORCE_ORE)/*,
					OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, Blocks.DEEPSLATE_DIAMOND_ORE.defaultBlockState())*/);

	public static final ConfiguredFeature<?, ?> ORE_FORCE = FeatureUtils.register("ore_force", Feature.ORE.configured(new OreConfiguration(ORE_FORCE_TARGET_LIST, 6)));
	public static final ConfiguredFeature<?, ?> ORE_FORCE_BURIED = FeatureUtils.register("ore_force_buried", Feature.ORE.configured(new OreConfiguration(ORE_FORCE_TARGET_LIST, 6, 1.0F)));


	public static final ConfiguredFeature<TreeConfiguration, ?> FORCE_TREE = FeatureUtils.register("force_tree",
			ForceFeatures.FORCE_TREE.get().configured(createForceTree().build()));

	private static final BeehiveDecorator BEEHIVE_005 = new BeehiveDecorator(0.05F);
	public static final ConfiguredFeature<TreeConfiguration, ?> FORCE_TREE_WITH_MORE_BEEHIVES_CONFIG = FeatureUtils.register("force_tree_with_bees",
			ForceFeatures.FORCE_TREE.get().configured(createForceTree().decorators(List.of(BEEHIVE_005)).build()));

	private static TreeConfiguration.TreeConfigurationBuilder createStraightBlobTree(BlockState state, BlockState p_195148_, int p_195149_, int p_195150_, int p_195151_, int p_195152_) {
		return new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(state), new StraightTrunkPlacer(p_195149_, p_195150_, p_195151_), BlockStateProvider.simple(p_195148_), new BlobFoliagePlacer(ConstantInt.of(p_195152_), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1));
	}

	private static TreeConfiguration.TreeConfigurationBuilder createForceTree() {
		return createStraightBlobTree(FORCE_LOG, FORCE_LEAVES, 4, 2, 0, 2).ignoreVines();
	}

	public static void initialize() {

	}
}
