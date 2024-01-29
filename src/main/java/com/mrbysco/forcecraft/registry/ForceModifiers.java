package com.mrbysco.forcecraft.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.modifier.AddConfigFeatureBiomeModifier;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ForceModifiers {
	public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Reference.MOD_ID);

	public static final Supplier<Codec<AddConfigFeatureBiomeModifier>> ADD_CONFIG_FEATURES_BIOME_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("add_config_features", () ->
			RecordCodecBuilder.create(builder -> builder.group(
					Biome.LIST_CODEC.fieldOf("biomes").forGetter(AddConfigFeatureBiomeModifier::biomes),
					PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(AddConfigFeatureBiomeModifier::features),
					Decoration.CODEC.fieldOf("step").forGetter(AddConfigFeatureBiomeModifier::step),
					Codec.STRING.fieldOf("enabledConfig").forGetter(AddConfigFeatureBiomeModifier::enabledConfig)
			).apply(builder, AddConfigFeatureBiomeModifier::new))
	);
}
