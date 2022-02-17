package com.mrbysco.forcecraft.world.feature;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForceFeatures {
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Reference.MOD_ID);

	public static final RegistryObject<Feature<TreeConfiguration>> FORCE_TREE = FEATURES.register("force_tree", () -> new TreeFeature(TreeConfiguration.CODEC));
}