package mrbysco.forcecraft.world.feature;

import mrbysco.forcecraft.Reference;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ForceFeatures {
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Reference.MOD_ID);

	public static final RegistryObject<Feature<BaseTreeFeatureConfig>> FORCE_TREE = FEATURES.register("force_tree", () -> new TreeFeature(BaseTreeFeatureConfig.CODEC));
}
