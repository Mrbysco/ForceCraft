package com.mrbysco.forcecraft.blocks.tree;

import com.mrbysco.forcecraft.world.feature.ForceFeatureKeys;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;

public class ForceTree extends AbstractTreeGrower {
	/**
	 * Get a {@link net.minecraft.world.level.levelgen.feature.ConfiguredFeature} of tree
	 */
	@Nullable
	@Override
	protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource randomIn, boolean largeHive) {
		return largeHive ? ForceFeatureKeys.FORCE_TREE_WITH_MORE_BEEHIVES_CONFIG : ForceFeatureKeys.FORCE_TREE;
	}
}