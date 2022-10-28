package com.mrbysco.forcecraft.world.feature;

import com.google.common.collect.ImmutableList;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.Features.Placements;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.TwoLayerFeature;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.DecoratedPlacement;
import net.minecraft.world.gen.treedecorator.BeehiveTreeDecorator;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;

public class ForceFeatureConfigs {
	private static final BeehiveTreeDecorator MANY_BEEHIVES = new BeehiveTreeDecorator(0.05F);
	private static final BlockState FORCE_LOG = ForceRegistry.FORCE_LOG.get().defaultBlockState();
	private static final BlockState FORCE_LEAVES = ForceRegistry.FORCE_LEAVES.get().defaultBlockState();
	protected static final BlockState FORCE_ORE = ForceRegistry.POWER_ORE.get().defaultBlockState();

	public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> FORCE_TREE_CONFIG = register("force_tree",
			ForceFeatures.FORCE_TREE.get().configured((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(ForceFeatureConfigs.FORCE_LOG),
					new SimpleBlockStateProvider(ForceFeatureConfigs.FORCE_LEAVES), new BlobFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(0), 3),
					new StraightTrunkPlacer(4, 2, 0), new TwoLayerFeature(1, 0, 1))).ignoreVines().build()));

	public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> FORCE_TREE_WITH_MORE_BEEHIVES_CONFIG = register("force_tree_with_bees", ForceFeatures.FORCE_TREE.get()
			.configured((FORCE_TREE_CONFIG.config().withDecorators(ImmutableList.of(MANY_BEEHIVES)))));

	public static ConfiguredFeature<?, ?> ORE_FORCE = register("ore_force",
			Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, FORCE_ORE, 6))
					.range(64).squared().count(5));

	public static final ConfiguredFeature<?, ?> FORCE_TREE_VEGETATION = register("force_tree_vegetation",
			FORCE_TREE_CONFIG.decorated(Placements.HEIGHTMAP_SQUARE).decorated(DecoratedPlacement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(1, 0.1F, 1))));

	public static void initialize() {

	}

	private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String key, ConfiguredFeature<FC, ?> feature) {
		return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(Reference.MOD_ID, key), feature);
	}
}
