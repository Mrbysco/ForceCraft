package com.mrbysco.forcecraft.world;

import com.mrbysco.forcecraft.config.ConfigHandler;
import com.mrbysco.forcecraft.world.feature.ForceFeatureConfigs;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldGenHandler {
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void biomeLoadingEvent(BiomeLoadingEvent event) {
		BiomeGenerationSettingsBuilder builder = event.getGeneration();
		Biome.Category category = event.getCategory();
		if(category != Category.THEEND && category != Category.NETHER) {
			if(ConfigHandler.COMMON.generateForceOre.get()) {
				builder.getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).add(() -> ForceFeatureConfigs.ORE_FORCE);
			}
			if(ConfigHandler.COMMON.generateForceTree.get() && category == Category.FOREST) {
				builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ForceFeatureConfigs.FORCE_TREE_VEGETATION);
			}
		}
	}
}
