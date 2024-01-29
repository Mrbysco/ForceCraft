package com.mrbysco.forcecraft.world.feature;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ForceFeatures {
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(BuiltInRegistries.FEATURE, Reference.MOD_ID);

	public static final Supplier<Feature<TreeConfiguration>> FORCE_TREE = FEATURES.register("force_tree", () -> new TreeFeature(TreeConfiguration.CODEC));
}