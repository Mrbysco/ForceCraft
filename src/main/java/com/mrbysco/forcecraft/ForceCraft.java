package com.mrbysco.forcecraft;

import com.mojang.logging.LogUtils;
import com.mrbysco.forcecraft.config.ForceConfig;
import com.mrbysco.forcecraft.recipe.condition.ForceConditions;
import com.mrbysco.forcecraft.registry.ForceAttachments;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceEffects;
import com.mrbysco.forcecraft.registry.ForceEntities;
import com.mrbysco.forcecraft.registry.ForceFluids;
import com.mrbysco.forcecraft.registry.ForceMenus;
import com.mrbysco.forcecraft.registry.ForceModifiers;
import com.mrbysco.forcecraft.registry.ForceRecipes;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceSounds;
import com.mrbysco.forcecraft.registry.ForceUpgradeTypes;
import com.mrbysco.forcecraft.util.SpecialUsers;
import com.mrbysco.forcecraft.world.feature.ForceFeatures;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.slf4j.Logger;

@Mod(Reference.MOD_ID)
public class ForceCraft {
	public static final Logger LOGGER = LogUtils.getLogger();

	public ForceCraft(IEventBus eventBus, Dist dist, ModContainer container) {
		container.registerConfig(ModConfig.Type.COMMON, ForceConfig.commonSpec);

		ForceUpgradeTypes.TYPES.register(eventBus);
		ForceComponents.COMPONENT_TYPE.register(eventBus);
		ForceFluids.FLUIDS.register(eventBus);
		ForceFluids.FLUID_TYPES.register(eventBus);
		ForceRegistry.BLOCKS.register(eventBus);
		ForceRegistry.ITEMS.register(eventBus);
		ForceRegistry.BLOCK_ENTITY_TYPES.register(eventBus);
		ForceRegistry.CREATIVE_MODE_TABS.register(eventBus);
		ForceAttachments.ATTACHMENT_TYPES.register(eventBus);
		ForceEffects.EFFECTS.register(eventBus);
		ForceSounds.SOUND_EVENTS.register(eventBus);
		ForceEntities.ENTITY_TYPES.register(eventBus);
		ForceMenus.MENU_TYPES.register(eventBus);
		ForceModifiers.BIOME_MODIFIER_SERIALIZERS.register(eventBus);
		ForceFeatures.FEATURES.register(eventBus);
		ForceRecipes.RECIPE_TYPES.register(eventBus);
		ForceRecipes.RECIPE_SERIALIZERS.register(eventBus);
		ForceConditions.CONDITION_CODECS.register(eventBus);

		eventBus.addListener(this::init);

		NeoForgeMod.enableMilkFluid(); //Enable milk from forge

		if (dist.isClient()) {
			container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		}
	}

	public void init(FMLCommonSetupEvent event) {
		FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;
		pot.addPlant(ForceRegistry.FORCE_SAPLING.getId(), ForceRegistry.POTTED_FORCE_SAPLING);
		SpecialUsers.loadExplodyUsers();
	}
}
