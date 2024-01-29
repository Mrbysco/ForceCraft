package com.mrbysco.forcecraft.blocks.tree;

import com.mrbysco.forcecraft.world.feature.ForceFeatureKeys;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class ForceTreeGrower {
	public static final TreeGrower FORCE = new TreeGrower("forcecraft:force", Optional.empty(), Optional.of(ForceFeatureKeys.FORCE_TREE), Optional.of(ForceFeatureKeys.FORCE_TREE_WITH_MORE_BEEHIVES_CONFIG));
}