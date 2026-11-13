package com.mrbysco.forcecraft.datagen;

import com.mrbysco.forcecraft.datagen.client.ForceLanguageProvider;
import com.mrbysco.forcecraft.datagen.client.ForceModelProvider;
import com.mrbysco.forcecraft.datagen.client.ForceSoundProvider;
import com.mrbysco.forcecraft.datagen.server.ForceAdvancementsProvider;
import com.mrbysco.forcecraft.datagen.server.ForceBlockTagsProvider;
import com.mrbysco.forcecraft.datagen.server.ForceDamageTypeTagProvider;
import com.mrbysco.forcecraft.datagen.server.ForceDataMapProvider;
import com.mrbysco.forcecraft.datagen.server.ForceEntityTypeTagsProvider;
import com.mrbysco.forcecraft.datagen.server.ForceFluidTagsProvider;
import com.mrbysco.forcecraft.datagen.server.ForceItemTagsProvider;
import com.mrbysco.forcecraft.datagen.server.ForceLootModifierProvider;
import com.mrbysco.forcecraft.datagen.server.ForceLootProvider;
import com.mrbysco.forcecraft.datagen.server.ForceRecipeProvider;
import com.mrbysco.forcecraft.registry.ForceDamageTypes;
import com.mrbysco.forcecraft.world.feature.ForceBiomeModifiers;
import com.mrbysco.forcecraft.world.feature.ForceFeatures;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.event.GatherDataRegistryEntriesEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@EventBusSubscriber
public class ForceCraftDatagen {
	@SubscribeEvent
	static void onGatherRegistries(GatherDataRegistryEntriesEvent event) {
		event.recipe(ForceRecipeProvider::new);
		event.lootTable(
				new LootTableProvider.SubProviderEntry(ForceLootProvider.ForceBlockLoot::new, LootContextParamSets.BLOCK),
				new LootTableProvider.SubProviderEntry(ForceLootProvider.ForceEntityLoot::new, LootContextParamSets.ENTITY)
		);
		event.advancement(ForceAdvancementsProvider.ForceAdvancementsSubProvider::new);
		event.add(Registries.DAMAGE_TYPE, ForceDamageTypes::bootstrap);
		event.add(Registries.FEATURE, ForceFeatures::bootstrap);
		event.add(Registries.PLACED_FEATURE, ForceFeatures::placedBootstrap);
		event.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ForceBiomeModifiers::modifierBootstrap);
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		event.createProvider(ForceLanguageProvider::new);
		event.createProvider(ForceModelProvider::new);
		event.createProvider(ForceSoundProvider::new);

		event.createProvider(ForceFluidTagsProvider::new);
		event.createProvider(ForceBlockTagsProvider::new);
		event.createProvider(ForceItemTagsProvider::new);
		event.createProvider(ForceEntityTypeTagsProvider::new);
		event.createProvider(ForceDamageTypeTagProvider::new);
		event.createProvider(ForceDataMapProvider::new);
		event.createProvider(ForceLootModifierProvider::new);
	}
}
