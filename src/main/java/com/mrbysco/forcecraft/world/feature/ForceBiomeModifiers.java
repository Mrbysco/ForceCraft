package com.mrbysco.forcecraft.world.feature;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.modifier.AddConfigFeatureBiomeModifier;
import com.mrbysco.forcecraft.registry.ForceEntities;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ForceBiomeModifiers {

	protected static final ResourceKey<BiomeModifier> ADD_FORCE_ORE_MODIFIER = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS,
			new ResourceLocation(Reference.MOD_ID, "add_force_ore"));
	protected static final ResourceKey<BiomeModifier> ADD_FORCE_ORE_BURIED_MODIFIER = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS,
			new ResourceLocation(Reference.MOD_ID, "add_force_ore_buried"));
	protected static final ResourceKey<BiomeModifier> ADD_FORCE_TREE = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS,
			new ResourceLocation(Reference.MOD_ID, "add_force_tree"));
	protected static final ResourceKey<BiomeModifier> ADD_CHU_CHU_MODIFIER = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS,
			new ResourceLocation(Reference.MOD_ID, "add_chu_chu"));
	protected static final ResourceKey<BiomeModifier> ADD_SWAMP_CHU_CHU_MODIFIER = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS,
			new ResourceLocation(Reference.MOD_ID, "add_swamp_chu_chu"));
	protected static final ResourceKey<BiomeModifier> ADD_CREEPER_TOT_MODIFIER = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS,
			new ResourceLocation(Reference.MOD_ID, "add_creeper_tot"));
	protected static final ResourceKey<BiomeModifier> ADD_ENDER_TOT_MODIFIER = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS,
			new ResourceLocation(Reference.MOD_ID, "add_ender_tot"));
	protected static final ResourceKey<BiomeModifier> ADD_FAIRY_MODIFIER = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS,
			new ResourceLocation(Reference.MOD_ID, "add_fairy"));

	public static void modifierBootstrap(BootstapContext<BiomeModifier> context) {
		HolderGetter<Biome> biomeGetter = context.lookup(Registries.BIOME);
		HolderGetter<PlacedFeature> placedGetter = context.lookup(Registries.PLACED_FEATURE);

		HolderSet.Named<Biome> overworldHolder = biomeGetter.getOrThrow(BiomeTags.IS_OVERWORLD);

		HolderSet.Named<Biome> forestHolder = biomeGetter.getOrThrow(BiomeTags.IS_FOREST);
		HolderSet.Named<Biome> swampHolder = biomeGetter.getOrThrow(Tags.Biomes.IS_SWAMP);

		context.register(ADD_FORCE_ORE_MODIFIER, new AddConfigFeatureBiomeModifier(
				overworldHolder,
				HolderSet.direct(placedGetter.getOrThrow(ForceFeatureKeys.PLACED_ORE_FORCE)),
				GenerationStep.Decoration.UNDERGROUND_ORES, "add_force_ore_modifier"
		));
		context.register(ADD_FORCE_ORE_BURIED_MODIFIER, new AddConfigFeatureBiomeModifier(
				overworldHolder,
				HolderSet.direct(placedGetter.getOrThrow(ForceFeatureKeys.PLACED_ORE_FORCE_BURIED)),
				GenerationStep.Decoration.UNDERGROUND_ORES, "add_force_ore_buried_modifier"
		));
		context.register(ADD_FORCE_TREE, new AddConfigFeatureBiomeModifier(
				forestHolder,
				HolderSet.direct(placedGetter.getOrThrow(ForceFeatureKeys.PLACED_FORCE_TREE)),
				GenerationStep.Decoration.VEGETAL_DECORATION, "add_force_tree"
		));

		context.register(ADD_CHU_CHU_MODIFIER, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
				overworldHolder,
				List.of(
						new MobSpawnSettings.SpawnerData(ForceEntities.RED_CHU_CHU.get(), 100, 1, 1),
						new MobSpawnSettings.SpawnerData(ForceEntities.GREEN_CHU_CHU.get(), 100, 1, 1),
						new MobSpawnSettings.SpawnerData(ForceEntities.BLUE_CHU_CHU.get(), 100, 1, 1),
						new MobSpawnSettings.SpawnerData(ForceEntities.GOLD_CHU_CHU.get(), 100, 1, 1)
				)
		));

		context.register(ADD_SWAMP_CHU_CHU_MODIFIER, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
				swampHolder,
				List.of(
						new MobSpawnSettings.SpawnerData(ForceEntities.RED_CHU_CHU.get(), 1, 1, 1),
						new MobSpawnSettings.SpawnerData(ForceEntities.GREEN_CHU_CHU.get(), 1, 1, 1),
						new MobSpawnSettings.SpawnerData(ForceEntities.BLUE_CHU_CHU.get(), 1, 1, 1),
						new MobSpawnSettings.SpawnerData(ForceEntities.GOLD_CHU_CHU.get(), 1, 1, 1)
				)
		));

		context.register(ADD_CREEPER_TOT_MODIFIER, ForgeBiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
				overworldHolder,
				new MobSpawnSettings.SpawnerData(ForceEntities.CREEPER_TOT.get(), 25, 1, 1))
		);
		context.register(ADD_ENDER_TOT_MODIFIER, ForgeBiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
				overworldHolder,
				new MobSpawnSettings.SpawnerData(ForceEntities.ENDER_TOT.get(), 5, 1, 1))
		);
		context.register(ADD_FAIRY_MODIFIER, ForgeBiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
				overworldHolder,
				new MobSpawnSettings.SpawnerData(ForceEntities.FAIRY.get(), 4, 1, 2))
		);
	}
}
