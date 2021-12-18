package com.mrbysco.forcecraft.world.feature;

import com.google.common.collect.ImmutableList;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.util.UniformInt;
import net.minecraft.data.worldgen.Features.Decorators;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.placement.FrequencyWithExtraChanceDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.DecoratedDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

public class ForceFeatureConfigs {
	private static final BeehiveDecorator MANY_BEEHIVES = new BeehiveDecorator(0.05F);
	private static final BlockState FORCE_LOG = ForceRegistry.FORCE_LOG.get().defaultBlockState();
	private static final BlockState FORCE_LEAVES = ForceRegistry.FORCE_LEAVES.get().defaultBlockState();
	protected static final BlockState FORCE_ORE = ForceRegistry.POWER_ORE.get().defaultBlockState();

	public static final ConfiguredFeature<TreeConfiguration, ?> FORCE_TREE_CONFIG = register("force_tree",
			ForceFeatures.FORCE_TREE.get().configured((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(ForceFeatureConfigs.FORCE_LOG),
					new SimpleStateProvider(ForceFeatureConfigs.FORCE_LEAVES), new BlobFoliagePlacer(UniformInt.fixed(2), UniformInt.fixed(0), 3),
					new StraightTrunkPlacer(4, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));

	public static final ConfiguredFeature<TreeConfiguration, ?> FORCE_TREE_WITH_MORE_BEEHIVES_CONFIG = register("force_tree_with_bees", ForceFeatures.FORCE_TREE.get()
			.configured((FORCE_TREE_CONFIG.config().withDecorators(ImmutableList.of(MANY_BEEHIVES)))));

	public static ConfiguredFeature<?, ?> ORE_FORCE = register("ore_force",
			Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, FORCE_ORE, 6))
					.range(64).squared().count(5));

	public static final ConfiguredFeature<?, ?> FORCE_TREE_VEGETATION = register("force_tree_vegetation",
			FORCE_TREE_CONFIG.decorated(Decorators.HEIGHTMAP_SQUARE).decorated(DecoratedDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(1, 0.1F, 1))));

	public static void initialize() {

	}

	private static <FC extends FeatureConfiguration> ConfiguredFeature<FC, ?> register(String key, ConfiguredFeature<FC, ?> feature) {
		return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(Reference.MOD_ID, key), feature);
	}
}
