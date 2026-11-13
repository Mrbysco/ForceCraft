package com.mrbysco.forcecraft.world.feature;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.modifier.AddConfigFeatureBiomeModifier;
import com.mrbysco.forcecraft.registry.ForceEntities;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.valueproviders.VeryBiasedToBottomInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ForceBiomeModifiers {

	protected static final ResourceKey<BiomeModifier> ADD_FORCE_ORE_MODIFIER = createKey("add_force_ore");
	protected static final ResourceKey<BiomeModifier> ADD_FORCE_ORE_BURIED_MODIFIER = createKey("add_force_ore_buried");
	protected static final ResourceKey<BiomeModifier> ADD_NETHER_FORCE_ORE = createKey("add_nether_force_ore");
	protected static final ResourceKey<BiomeModifier> ADD_FORCE_TREE = createKey("add_force_tree");
	protected static final ResourceKey<BiomeModifier> ADD_CREEPER_TOT_MODIFIER = createKey("add_creeper_tot");
	protected static final ResourceKey<BiomeModifier> ADD_ENDER_TOT_MODIFIER = createKey("add_ender_tot");

	private static ResourceKey<BiomeModifier> createKey(String name) {
		return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Reference.modLoc(name));
	}

	public static void modifierBootstrap(BootstrapContext<BiomeModifier> context) {
		HolderGetter<Biome> biomeGetter = context.lookup(Registries.BIOME);
		HolderGetter<PlacedFeature> placedGetter = context.lookup(Registries.PLACED_FEATURE);

		HolderSet.Named<Biome> overworldHolder = biomeGetter.getOrThrow(BiomeTags.IS_OVERWORLD);
		HolderSet.Named<Biome> netherHolder = biomeGetter.getOrThrow(BiomeTags.IS_NETHER);
		HolderSet.Named<Biome> forestHolder = biomeGetter.getOrThrow(BiomeTags.IS_FOREST);

		context.register(ADD_FORCE_ORE_MODIFIER, new AddConfigFeatureBiomeModifier(
				overworldHolder,
				HolderSet.direct(placedGetter.getOrThrow(ForceFeatures.PLACED_ORE_FORCE)),
				GenerationStep.Decoration.UNDERGROUND_ORES, "force_ore"
		));
		context.register(ADD_FORCE_ORE_BURIED_MODIFIER, new AddConfigFeatureBiomeModifier(
				overworldHolder,
				HolderSet.direct(placedGetter.getOrThrow(ForceFeatures.PLACED_ORE_FORCE_BURIED)),
				GenerationStep.Decoration.UNDERGROUND_ORES, "force_ore"
		));
		context.register(ADD_NETHER_FORCE_ORE, new AddConfigFeatureBiomeModifier(
				netherHolder,
				HolderSet.direct(placedGetter.getOrThrow(ForceFeatures.PLACED_ORE_NETHER_FORCE)),
				GenerationStep.Decoration.UNDERGROUND_ORES, "force_ore_nether"
		));
		context.register(ADD_FORCE_TREE, new AddConfigFeatureBiomeModifier(
				forestHolder,
				HolderSet.direct(placedGetter.getOrThrow(ForceFeatures.PLACED_FORCE_TREE)),
				GenerationStep.Decoration.VEGETAL_DECORATION, "force_tree"
		));

		// Entities
		context.register(ADD_CREEPER_TOT_MODIFIER, BiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
				overworldHolder,
				new Weighted<>(new MobSpawnSettings.SpawnerData(ForceEntities.CREEPER_TOT.get(), VeryBiasedToBottomInt.of(1, 1)), 25))
		);
		context.register(ADD_ENDER_TOT_MODIFIER, BiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
				overworldHolder,
				new Weighted<>(new MobSpawnSettings.SpawnerData(ForceEntities.ENDER_TOT.get(), VeryBiasedToBottomInt.of(1, 1)), 5))
		);
	}
}
