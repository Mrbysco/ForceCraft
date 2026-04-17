package com.mrbysco.forcecraft.datagen;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.datagen.assets.ForceBlockModelProvider;
import com.mrbysco.forcecraft.datagen.assets.ForceBlockStateProvider;
import com.mrbysco.forcecraft.datagen.assets.ForceItemModelProvider;
import com.mrbysco.forcecraft.datagen.assets.ForceLanguageProvider;
import com.mrbysco.forcecraft.datagen.data.ForceDamageTypeProvider;
import com.mrbysco.forcecraft.datagen.data.ForceLootModifierProvider;
import com.mrbysco.forcecraft.datagen.data.ForceLootProvider;
import com.mrbysco.forcecraft.datagen.data.ForceRecipeProvider;
import com.mrbysco.forcecraft.datagen.data.tags.ForceBlockTagProvider;
import com.mrbysco.forcecraft.datagen.data.tags.ForceDamageTypeTagProvider;
import com.mrbysco.forcecraft.datagen.data.tags.ForceEntityTagProvider;
import com.mrbysco.forcecraft.datagen.data.tags.ForceItemTagProvider;
import com.mrbysco.forcecraft.datagen.patchouli.PatchouliProvider;
import com.mrbysco.forcecraft.world.feature.ForceBiomeModifiers;
import com.mrbysco.forcecraft.world.feature.ForceFeatureKeys;
import net.minecraft.core.Cloner;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.VanillaRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public class ForceDataGen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = CompletableFuture.supplyAsync(() -> getProvider().full());

		generator.addProvider(true, new ForceLootProvider(packOutput, lookupProvider));
		generator.addProvider(true, new ForceRecipeProvider.Runner(packOutput, lookupProvider));
		generator.addProvider(true, new PatchouliProvider(packOutput, lookupProvider));
		BlockTagsProvider provider;
		generator.addProvider(true, provider = new ForceBlockTagProvider(packOutput, lookupProvider));
		generator.addProvider(true, new ForceItemTagProvider(packOutput, lookupProvider, provider));
		generator.addProvider(true, new ForceDamageTypeTagProvider(packOutput, lookupProvider));
		generator.addProvider(true, new ForceEntityTagProvider(packOutput, lookupProvider));
		generator.addProvider(true, new ForceLootModifierProvider(packOutput, lookupProvider));

		generator.addProvider(true, new DatapackBuiltinEntriesProvider(
				packOutput, CompletableFuture.supplyAsync(ForceDataGen::getProvider), Set.of(Reference.MOD_ID)));

		generator.addProvider(true, new ForceLanguageProvider(packOutput));
		generator.addProvider(true, new ForceBlockModelProvider(packOutput));
		generator.addProvider(true, new ForceBlockStateProvider(packOutput));
		generator.addProvider(true, new ForceItemModelProvider(packOutput));
		
	}

	private static RegistrySetBuilder.PatchedRegistries getProvider() {
		final RegistrySetBuilder registryBuilder = new RegistrySetBuilder();
		registryBuilder.add(Registries.DAMAGE_TYPE, ForceDamageTypeProvider::bootstrap);
		registryBuilder.add(Registries.CONFIGURED_FEATURE, ForceFeatureKeys::configuredBootstrap);
		registryBuilder.add(Registries.PLACED_FEATURE, ForceFeatureKeys::placedBootstrap);
		registryBuilder.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ForceBiomeModifiers::modifierBootstrap);
		// We need the BIOME registry to be present, so we can use a biome tag, doesn't matter that it's empty
		registryBuilder.add(Registries.BIOME, context -> {
		});
		RegistryAccess.Frozen regAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
		Cloner.Factory cloner$factory = new Cloner.Factory();
		net.neoforged.neoforge.registries.DataPackRegistriesHooks.getDataPackRegistriesWithDimensions().forEach(data -> data.runWithArguments(cloner$factory::addCodec));
		return registryBuilder.buildPatch(regAccess, VanillaRegistries.createLookup(), cloner$factory);
	}
}