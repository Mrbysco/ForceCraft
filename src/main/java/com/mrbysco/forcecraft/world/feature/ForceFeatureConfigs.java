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
	private static final BlockState FORCE_LOG = ForceRegistry.FORCE_LOG.get().getDefaultState();
	private static final BlockState FORCE_LEAVES = ForceRegistry.FORCE_LEAVES.get().getDefaultState();
	protected static final BlockState FORCE_ORE = ForceRegistry.POWER_ORE.get().getDefaultState();

	public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> FORCE_TREE_CONFIG = register("force_tree",
			ForceFeatures.FORCE_TREE.get().withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(ForceFeatureConfigs.FORCE_LOG),
					new SimpleBlockStateProvider(ForceFeatureConfigs.FORCE_LEAVES), new BlobFoliagePlacer(FeatureSpread.create(2), FeatureSpread.create(0), 3),
					new StraightTrunkPlacer(4, 2, 0), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build()));

	public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> FORCE_TREE_WITH_MORE_BEEHIVES_CONFIG = register("force_tree_with_bees", ForceFeatures.FORCE_TREE.get()
			.withConfiguration((FORCE_TREE_CONFIG.getConfig().copy(ImmutableList.of(MANY_BEEHIVES)))));

	public static ConfiguredFeature<?, ?> ORE_FORCE = register("ore_force",
			Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, FORCE_ORE, 6))
					.range(64).square().count(5));

	public static final ConfiguredFeature<?, ?> FORCE_TREE_VEGETATION = register("force_tree_vegetation",
			FORCE_TREE_CONFIG.withPlacement(Placements.HEIGHTMAP_PLACEMENT).withPlacement(DecoratedPlacement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(1, 0.1F, 1))));

	public static void initialize() {

	}

	private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String key, ConfiguredFeature<FC, ?> feature) {
		return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(Reference.MOD_ID, key), feature);
	}
}
