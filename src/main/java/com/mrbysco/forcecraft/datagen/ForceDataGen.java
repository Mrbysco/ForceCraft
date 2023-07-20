package com.mrbysco.forcecraft.datagen;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.datagen.assets.ForceBlockModels;
import com.mrbysco.forcecraft.datagen.assets.ForceBlockStates;
import com.mrbysco.forcecraft.datagen.assets.ForceItemModels;
import com.mrbysco.forcecraft.datagen.assets.ForceLanguage;
import com.mrbysco.forcecraft.datagen.data.ForceLoot;
import com.mrbysco.forcecraft.datagen.data.ForceLootModifiers;
import com.mrbysco.forcecraft.datagen.data.ForceRecipeProvider;
import com.mrbysco.forcecraft.datagen.data.tags.ForceBlockTags;
import com.mrbysco.forcecraft.datagen.data.tags.ForceItemTags;
import com.mrbysco.forcecraft.datagen.patchouli.PatchouliProvider;
import com.mrbysco.forcecraft.world.feature.ForceBiomeModifiers;
import com.mrbysco.forcecraft.world.feature.ForceFeatureKeys;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForceDataGen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = CompletableFuture.supplyAsync(ForceDataGen::getProvider);
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(event.includeServer(), new ForceLoot(packOutput));
			generator.addProvider(event.includeServer(), new ForceRecipeProvider(packOutput));
			generator.addProvider(event.includeServer(), new PatchouliProvider(packOutput));
			BlockTagsProvider provider;
			generator.addProvider(event.includeServer(), provider = new ForceBlockTags(packOutput, lookupProvider, existingFileHelper));
			generator.addProvider(event.includeServer(), new ForceItemTags(packOutput, lookupProvider, provider, existingFileHelper));
			generator.addProvider(event.includeServer(), new ForceLootModifiers(packOutput));


			generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(
					packOutput, lookupProvider, Set.of(Reference.MOD_ID)));
		}
		if (event.includeClient()) {
			generator.addProvider(event.includeClient(), new ForceLanguage(packOutput));
			generator.addProvider(event.includeClient(), new ForceBlockModels(packOutput, existingFileHelper));
			generator.addProvider(event.includeClient(), new ForceBlockStates(packOutput, existingFileHelper));
			generator.addProvider(event.includeClient(), new ForceItemModels(packOutput, existingFileHelper));
		}
	}

	private static HolderLookup.Provider getProvider() {
		final RegistrySetBuilder registryBuilder = new RegistrySetBuilder();
		registryBuilder.add(Registries.CONFIGURED_FEATURE, ForceFeatureKeys::configuredBootstrap);
		registryBuilder.add(Registries.PLACED_FEATURE, ForceFeatureKeys::placedBootstrap);
		registryBuilder.add(ForgeRegistries.Keys.BIOME_MODIFIERS, ForceBiomeModifiers::modifierBootstrap);
		// We need the BIOME registry to be present so we can use a biome tag, doesn't matter that it's empty
		registryBuilder.add(Registries.BIOME, context -> {
		});
		RegistryAccess.Frozen regAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
		return registryBuilder.buildPatch(regAccess, VanillaRegistries.createLookup());
	}
}