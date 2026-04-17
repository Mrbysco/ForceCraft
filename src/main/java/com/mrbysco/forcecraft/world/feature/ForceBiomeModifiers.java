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
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ForceBiomeModifiers {

	protected static final ResourceKey<BiomeModifier> ADD_FORCE_ORE_MODIFIER = createKey("add_force_ore");
	protected static final ResourceKey<BiomeModifier> ADD_FORCE_ORE_BURIED_MODIFIER = createKey("add_force_ore_buried");
	protected static final ResourceKey<BiomeModifier> ADD_FORCE_TREE = createKey("add_force_tree");
	protected static final ResourceKey<BiomeModifier> ADD_CHU_CHU_MODIFIER = createKey("add_chu_chu");
	protected static final ResourceKey<BiomeModifier> ADD_SWAMP_CHU_CHU_MODIFIER = createKey("add_swamp_chu_chu");
	protected static final ResourceKey<BiomeModifier> ADD_CREEPER_TOT_MODIFIER = createKey("add_creeper_tot");
	protected static final ResourceKey<BiomeModifier> ADD_ENDER_TOT_MODIFIER = createKey("add_ender_tot");
	protected static final ResourceKey<BiomeModifier> ADD_FAIRY_MODIFIER = createKey("add_fairy");

	private static ResourceKey<BiomeModifier> createKey(String name) {
		return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Reference.modLoc(name));
	}

	public static void modifierBootstrap(BootstrapContext<BiomeModifier> context) {
		HolderGetter<Biome> biomeGetter = context.lookup(Registries.BIOME);
		HolderGetter<PlacedFeature> placedGetter = context.lookup(Registries.PLACED_FEATURE);

		HolderSet.Named<Biome> overworldHolder = biomeGetter.getOrThrow(BiomeTags.IS_OVERWORLD);

		HolderSet.Named<Biome> forestHolder = biomeGetter.getOrThrow(BiomeTags.IS_FOREST);
		HolderSet.Named<Biome> swampHolder = biomeGetter.getOrThrow(Tags.Biomes.IS_SWAMP);

		context.register(ADD_FORCE_ORE_MODIFIER, new AddConfigFeatureBiomeModifier(
				overworldHolder,
				HolderSet.direct(placedGetter.getOrThrow(ForceFeatureKeys.PLACED_ORE_FORCE)),
				GenerationStep.Decoration.UNDERGROUND_ORES, "force_ore"
		));
		context.register(ADD_FORCE_ORE_BURIED_MODIFIER, new AddConfigFeatureBiomeModifier(
				overworldHolder,
				HolderSet.direct(placedGetter.getOrThrow(ForceFeatureKeys.PLACED_ORE_FORCE_BURIED)),
				GenerationStep.Decoration.UNDERGROUND_ORES, "force_ore"
		));
		context.register(ADD_FORCE_TREE, new AddConfigFeatureBiomeModifier(
				forestHolder,
				HolderSet.direct(placedGetter.getOrThrow(ForceFeatureKeys.PLACED_FORCE_TREE)),
				GenerationStep.Decoration.VEGETAL_DECORATION, "force_tree"
		));

		context.register(ADD_CHU_CHU_MODIFIER, new BiomeModifiers.AddSpawnsBiomeModifier(
				overworldHolder,
				WeightedList.of(
						new Weighted<>(new MobSpawnSettings.SpawnerData(ForceEntities.RED_CHU_CHU.get(), 1, 1), 100),
						new Weighted<>(new MobSpawnSettings.SpawnerData(ForceEntities.GREEN_CHU_CHU.get(), 1, 1), 100),
						new Weighted<>(new MobSpawnSettings.SpawnerData(ForceEntities.BLUE_CHU_CHU.get(), 1, 1), 100),
						new Weighted<>(new MobSpawnSettings.SpawnerData(ForceEntities.GOLD_CHU_CHU.get(), 1, 1), 100)
				)
		));

		context.register(ADD_SWAMP_CHU_CHU_MODIFIER, new BiomeModifiers.AddSpawnsBiomeModifier(
				swampHolder,
				WeightedList.of(
						new Weighted<>(new MobSpawnSettings.SpawnerData(ForceEntities.RED_CHU_CHU.get(), 1, 1), 1),
						new Weighted<>(new MobSpawnSettings.SpawnerData(ForceEntities.GREEN_CHU_CHU.get(), 1, 1), 1),
						new Weighted<>(new MobSpawnSettings.SpawnerData(ForceEntities.BLUE_CHU_CHU.get(), 1, 1), 1),
						new Weighted<>(new MobSpawnSettings.SpawnerData(ForceEntities.GOLD_CHU_CHU.get(), 1, 1), 1)
				)
		));

		context.register(ADD_CREEPER_TOT_MODIFIER, BiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
				overworldHolder,
				new Weighted<>(new MobSpawnSettings.SpawnerData(ForceEntities.CREEPER_TOT.get(), 1, 1), 25))
		);
		context.register(ADD_ENDER_TOT_MODIFIER, BiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
				overworldHolder,
				new Weighted<>(new MobSpawnSettings.SpawnerData(ForceEntities.ENDER_TOT.get(), 1, 1), 5))
		);
		context.register(ADD_FAIRY_MODIFIER, BiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
				overworldHolder,
				new Weighted<>(new MobSpawnSettings.SpawnerData(ForceEntities.FAIRY.get(), 1, 2), 4))
		);
	}
}
