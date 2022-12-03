package com.mrbysco.forcecraft.datagen;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.datagen.assets.ForceBlockModels;
import com.mrbysco.forcecraft.datagen.assets.ForceBlockStates;
import com.mrbysco.forcecraft.datagen.assets.ForceItemModels;
import com.mrbysco.forcecraft.datagen.assets.ForceLanguage;
import com.mrbysco.forcecraft.datagen.data.ForceLoot;
import com.mrbysco.forcecraft.datagen.data.ForceLootModifiers;
import com.mrbysco.forcecraft.datagen.data.ForceRecipes;
import com.mrbysco.forcecraft.datagen.data.tags.ForceBlockTags;
import com.mrbysco.forcecraft.datagen.data.tags.ForceItemTags;
import com.mrbysco.forcecraft.datagen.patchouli.PatchouliProvider;
import com.mrbysco.forcecraft.modifier.AddConfigFeatureBiomeModifier;
import com.mrbysco.forcecraft.registry.ForceEntities;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.world.feature.ForceFeatureConfigs;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForceDataGen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		final RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.builtinCopy());
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(event.includeServer(), new ForceLoot(generator));
			generator.addProvider(event.includeServer(), new ForceRecipes(generator));
			generator.addProvider(event.includeServer(), new PatchouliProvider(generator));
			BlockTagsProvider provider;
			generator.addProvider(event.includeServer(), provider = new ForceBlockTags(generator, existingFileHelper));
			generator.addProvider(event.includeServer(), new ForceItemTags(generator, provider, existingFileHelper));
			generator.addProvider(event.includeServer(), new ForceLootModifiers(generator));

			generator.addProvider(event.includeServer(), JsonCodecProvider.forDatapackRegistry(
					generator, existingFileHelper, Reference.MOD_ID, ops, Registry.PLACED_FEATURE_REGISTRY, getConfiguredFeatures(ops)));

			generator.addProvider(event.includeServer(), JsonCodecProvider.forDatapackRegistry(
					generator, existingFileHelper, Reference.MOD_ID, ops, ForgeRegistries.Keys.BIOME_MODIFIERS, getBiomeModifiers(ops)));
		}
		if (event.includeClient()) {
			generator.addProvider(event.includeClient(), new ForceLanguage(generator));
			generator.addProvider(event.includeClient(), new ForceBlockModels(generator, existingFileHelper));
			generator.addProvider(event.includeClient(), new ForceBlockStates(generator, existingFileHelper));
			generator.addProvider(event.includeClient(), new ForceItemModels(generator, existingFileHelper));
		}
	}

	public static Map<ResourceLocation, PlacedFeature> getConfiguredFeatures(RegistryOps<JsonElement> ops) {
		final ResourceKey<ConfiguredFeature<?, ?>> forceOreFeatureKey = ForceFeatureConfigs.ORE_FORCE.unwrapKey().get().cast(Registry.CONFIGURED_FEATURE_REGISTRY).get();
		final Holder<ConfiguredFeature<?, ?>> forceOreFeatureHolder = ops.registry(Registry.CONFIGURED_FEATURE_REGISTRY).get().getOrCreateHolderOrThrow(forceOreFeatureKey);
		final PlacedFeature forceOreFeature = new PlacedFeature(
				forceOreFeatureHolder,
				commonOrePlacement(5, HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(48))));
		final ResourceKey<ConfiguredFeature<?, ?>> forceOreBuriedFeatureKey = ForceFeatureConfigs.ORE_FORCE_BURIED.unwrapKey().get().cast(Registry.CONFIGURED_FEATURE_REGISTRY).get();
		final Holder<ConfiguredFeature<?, ?>> forceOreBuriedFeatureHolder = ops.registry(Registry.CONFIGURED_FEATURE_REGISTRY).get().getOrCreateHolderOrThrow(forceOreBuriedFeatureKey);
		final PlacedFeature forceOreBuriedFeature = new PlacedFeature(
				forceOreBuriedFeatureHolder,
				commonOrePlacement(3, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64))));

		final ResourceKey<ConfiguredFeature<?, ?>> forceTreeFeatureKey = ForceFeatureConfigs.FORCE_TREE.unwrapKey().get().cast(Registry.CONFIGURED_FEATURE_REGISTRY).get();
		final Holder<ConfiguredFeature<?, ?>> forceTreeFeatureHolder = ops.registry(Registry.CONFIGURED_FEATURE_REGISTRY).get().getOrCreateHolderOrThrow(forceTreeFeatureKey);
		final PlacedFeature forceTreeFeature = new PlacedFeature(
				forceTreeFeatureHolder,
				VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(3), ForceRegistry.FORCE_SAPLING.get()));

		return Map.of(
				new ResourceLocation(Reference.MOD_ID, "force_ore"), forceOreFeature,
				new ResourceLocation(Reference.MOD_ID, "force_ore_buried"), forceOreBuriedFeature,
				new ResourceLocation(Reference.MOD_ID, "force_tree"), forceTreeFeature
		);
	}

	private static List<PlacementModifier> orePlacement(PlacementModifier modifier1, PlacementModifier modifier2) {
		return List.of(modifier1, InSquarePlacement.spread(), modifier2, BiomeFilter.biome());
	}

	private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier modifier) {
		return orePlacement(CountPlacement.of(count), modifier);
	}

	public static Map<ResourceLocation, BiomeModifier> getBiomeModifiers(RegistryOps<JsonElement> ops) {
		Map<ResourceLocation, BiomeModifier> map = new HashMap<>();

		final HolderSet.Named<Biome> overworldTag = new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).get(), BiomeTags.IS_OVERWORLD);
		final BiomeModifier addForceOre = new AddConfigFeatureBiomeModifier(
				overworldTag,
				HolderSet.direct(ops.registry(Registry.PLACED_FEATURE_REGISTRY).get().getOrCreateHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY,
						new ResourceLocation(Reference.MOD_ID, "force_ore")))),
				GenerationStep.Decoration.UNDERGROUND_ORES, "force_ore");
		final BiomeModifier addForceOreBuried = new AddConfigFeatureBiomeModifier(
				overworldTag,
				HolderSet.direct(ops.registry(Registry.PLACED_FEATURE_REGISTRY).get().getOrCreateHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY,
						new ResourceLocation(Reference.MOD_ID, "force_ore_buried")))),
				GenerationStep.Decoration.UNDERGROUND_ORES, "force_ore");
		final BiomeModifier addForceTree = new AddConfigFeatureBiomeModifier(
				overworldTag,
				HolderSet.direct(ops.registry(Registry.PLACED_FEATURE_REGISTRY).get().getOrCreateHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY,
						new ResourceLocation(Reference.MOD_ID, "force_tree")))),
				GenerationStep.Decoration.VEGETAL_DECORATION, "force_tree");

		final BiomeModifier addChuChu = new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
				overworldTag,
				List.of(
						new MobSpawnSettings.SpawnerData(ForceEntities.RED_CHU_CHU.get(), 100, 1, 1),
						new MobSpawnSettings.SpawnerData(ForceEntities.GREEN_CHU_CHU.get(), 100, 1, 1),
						new MobSpawnSettings.SpawnerData(ForceEntities.BLUE_CHU_CHU.get(), 100, 1, 1),
						new MobSpawnSettings.SpawnerData(ForceEntities.GOLD_CHU_CHU.get(), 100, 1, 1)
				)
		);

		final HolderSet.Named<Biome> swampTag = new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).get(), Tags.Biomes.IS_SWAMP);
		final BiomeModifier addSwampChuChu = new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
				swampTag,
				List.of(
						new MobSpawnSettings.SpawnerData(ForceEntities.RED_CHU_CHU.get(), 1, 1, 1),
						new MobSpawnSettings.SpawnerData(ForceEntities.GREEN_CHU_CHU.get(), 1, 1, 1),
						new MobSpawnSettings.SpawnerData(ForceEntities.BLUE_CHU_CHU.get(), 1, 1, 1),
						new MobSpawnSettings.SpawnerData(ForceEntities.GOLD_CHU_CHU.get(), 1, 1, 1)
				)
		);

		final BiomeModifier addCreeperTot = ForgeBiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
				overworldTag, new MobSpawnSettings.SpawnerData(ForceEntities.CREEPER_TOT.get(), 25, 1, 1));
		final BiomeModifier addEnderTot = ForgeBiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
				overworldTag, new MobSpawnSettings.SpawnerData(ForceEntities.ENDER_TOT.get(), 5, 1, 1));
		final BiomeModifier addFairy = ForgeBiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
				overworldTag, new MobSpawnSettings.SpawnerData(ForceEntities.FAIRY.get(), 4, 1, 2));

		map.put(new ResourceLocation(Reference.MOD_ID, "add_force_ore"), addForceOre);
		map.put(new ResourceLocation(Reference.MOD_ID, "add_force_ore_buried"), addForceOreBuried);
		map.put(new ResourceLocation(Reference.MOD_ID, "add_force_tree"), addForceTree);
		map.put(new ResourceLocation(Reference.MOD_ID, "add_chu_chu"), addChuChu);
		map.put(new ResourceLocation(Reference.MOD_ID, "add_swamp_chu_chu"), addSwampChuChu);
		map.put(new ResourceLocation(Reference.MOD_ID, "add_creeper_tot"), addCreeperTot);
		map.put(new ResourceLocation(Reference.MOD_ID, "add_ender_tot"), addEnderTot);
		map.put(new ResourceLocation(Reference.MOD_ID, "add_fairy"), addFairy);

		return map;
	}
}