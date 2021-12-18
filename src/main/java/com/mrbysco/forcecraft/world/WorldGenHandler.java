package com.mrbysco.forcecraft.world;

import com.mrbysco.forcecraft.config.ConfigHandler;
import com.mrbysco.forcecraft.world.feature.ForceFeatureConfigs;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldGenHandler {
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void biomeLoadingEvent(BiomeLoadingEvent event) {
		BiomeGenerationSettingsBuilder builder = event.getGeneration();
		Biome.BiomeCategory category = event.getCategory();
		if(category != BiomeCategory.THEEND && category != BiomeCategory.NETHER) {
			if(ConfigHandler.COMMON.generateForceOre.get()) {
				builder.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(() -> ForceFeatureConfigs.ORE_FORCE);
			}
			if(ConfigHandler.COMMON.generateForceTree.get() && category == BiomeCategory.FOREST) {
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ForceFeatureConfigs.FORCE_TREE_VEGETATION);
			}
		}
	}
}
