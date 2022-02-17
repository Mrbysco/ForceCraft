package com.mrbysco.forcecraft.datagen;

import com.mrbysco.forcecraft.datagen.assets.ForceBlockStates;
import com.mrbysco.forcecraft.datagen.assets.ForceItemModels;
import com.mrbysco.forcecraft.datagen.assets.ForceLanguage;
import com.mrbysco.forcecraft.datagen.data.ForceLoot;
import com.mrbysco.forcecraft.datagen.data.ForceRecipes;
import com.mrbysco.forcecraft.datagen.data.tags.ForceBlockTags;
import com.mrbysco.forcecraft.datagen.data.tags.ForceItemTags;
import com.mrbysco.forcecraft.datagen.patchouli.PatchouliProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForceDataGen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(new ForceLoot(generator));
			generator.addProvider(new ForceRecipes(generator));
			generator.addProvider(new PatchouliProvider(generator));
			BlockTagsProvider provider;
			generator.addProvider(provider = new ForceBlockTags(generator, helper));
			generator.addProvider(new ForceItemTags(generator, provider, helper));
		}
		if (event.includeClient()) {
			generator.addProvider(new ForceLanguage(generator));
			generator.addProvider(new ForceBlockStates(generator, helper));
			generator.addProvider(new ForceItemModels(generator, helper));
		}
	}
}