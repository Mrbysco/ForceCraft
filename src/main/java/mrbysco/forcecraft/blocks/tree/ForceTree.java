package mrbysco.forcecraft.blocks.tree;

import mrbysco.forcecraft.world.feature.ForceFeatureConfigs;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import java.util.Random;

public class ForceTree extends Tree {
	/**
	 * Get a {@link net.minecraft.world.gen.feature.ConfiguredFeature} of tree
	 */
	@Nullable
	protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive) {
		return largeHive ? ForceFeatureConfigs.FORCE_TREE_WITH_MORE_BEEHIVES_CONFIG : ForceFeatureConfigs.FORCE_TREE_CONFIG;
	}
}