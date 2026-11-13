package com.mrbysco.forcecraft.block.tree;

import com.mrbysco.forcecraft.world.feature.ForceFeatures;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.grower.TreeGrower;

public class ForceTreeGrower {
	public static final TreeGrower FORCE = new TreeGrower(
			"forcecraft:force",
			WeightedList.of(ForceFeatures.FORCE_TREE),
			WeightedList.of(),
			WeightedList.of(ForceFeatures.FORCE_TREE_WITH_MORE_BEEHIVES_CONFIG),
			ForceFeatures.FORCE_TREE
	);
}