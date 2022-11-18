package com.mrbysco.forcecraft.modifier;

import com.mojang.serialization.Codec;
import com.mrbysco.forcecraft.registry.ForceModifiers;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo.BiomeInfo.Builder;

public record AddConfigFeatureBiomeModifier(HolderSet<Biome> biomes, HolderSet<PlacedFeature> features, Decoration step,
											String enabledConfig) implements BiomeModifier {

	@Override
	public void modify(Holder<Biome> biome, Phase phase, Builder builder) {
		if (phase == Phase.ADD && this.biomes.contains(biome) && ConfigEnum.getByName(enabledConfig).getValue()) {
			BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();
			this.features.forEach(holder -> generationSettings.addFeature(this.step, holder));
		}
	}

	@Override
	public Codec<? extends BiomeModifier> codec() {
		return ForceModifiers.ADD_CONFIG_FEATURES_BIOME_MODIFIER_TYPE.get();
	}
}
