package com.mrbysco.forcecraft.datagen;

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
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForceDataGen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(event.includeServer(), new ForceLoot(generator));
			generator.addProvider(event.includeServer(), new ForceRecipes(generator));
			generator.addProvider(event.includeServer(), new PatchouliProvider(generator));
			BlockTagsProvider provider;
			generator.addProvider(event.includeServer(), provider = new ForceBlockTags(generator, helper));
			generator.addProvider(event.includeServer(), new ForceItemTags(generator, provider, helper));
			generator.addProvider(event.includeServer(), new ForceLootModifiers(generator));
		}
		if (event.includeClient()) {
			generator.addProvider(event.includeClient(), new ForceLanguage(generator));
			generator.addProvider(event.includeClient(), new ForceBlockModels(generator, helper));
			generator.addProvider(event.includeClient(), new ForceBlockStates(generator, helper));
			generator.addProvider(event.includeClient(), new ForceItemModels(generator, helper));
		}
	}
}