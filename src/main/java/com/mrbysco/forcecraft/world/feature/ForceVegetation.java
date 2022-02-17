package com.mrbysco.forcecraft.world.feature;

import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;

import java.util.List;

public class ForceVegetation {
	public static final ConfiguredFeature<RandomFeatureConfiguration, ?> TREES_FORCE = FeatureUtils.register("trees_force",
			Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(
					List.of(new WeightedPlacedFeature(ForcePlacements.FORCE_TREE_BEES_002, 0.2F)), ForcePlacements.FORCE_TREE_CHECKED)));

	public static void initialize() {

	}
}
