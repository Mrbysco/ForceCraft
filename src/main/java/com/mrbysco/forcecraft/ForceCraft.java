package com.mrbysco.forcecraft;

import com.mrbysco.forcecraft.attachment.CapabilityHandler;
import com.mrbysco.forcecraft.client.ClientHandler;
import com.mrbysco.forcecraft.client.KeybindHandler;
import com.mrbysco.forcecraft.command.ForceCommands;
import com.mrbysco.forcecraft.config.ConfigHandler;
import com.mrbysco.forcecraft.handlers.BaneHandler;
import com.mrbysco.forcecraft.handlers.ForceDeathHandler;
import com.mrbysco.forcecraft.handlers.GrindstoneHandler;
import com.mrbysco.forcecraft.handlers.HeartHandler;
import com.mrbysco.forcecraft.handlers.LootTableHandler;
import com.mrbysco.forcecraft.handlers.LootingHandler;
import com.mrbysco.forcecraft.handlers.PlayerCapHandler;
import com.mrbysco.forcecraft.handlers.ToolModifierHandler;
import com.mrbysco.forcecraft.items.nonburnable.NonBurnableItemEntity;
import com.mrbysco.forcecraft.networking.PacketHandler;
import com.mrbysco.forcecraft.recipe.condition.ForceConditions;
import com.mrbysco.forcecraft.registry.ForceEffects;
import com.mrbysco.forcecraft.registry.ForceEntities;
import com.mrbysco.forcecraft.registry.ForceFluids;
import com.mrbysco.forcecraft.registry.ForceLootModifiers;
import com.mrbysco.forcecraft.registry.ForceMenus;
import com.mrbysco.forcecraft.registry.ForceModifiers;
import com.mrbysco.forcecraft.registry.ForceRecipeSerializers;
import com.mrbysco.forcecraft.registry.ForceRecipes;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceSounds;
import com.mrbysco.forcecraft.world.feature.ForceFeatures;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MOD_ID)
public class ForceCraft {

	public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);

	public ForceCraft(IEventBus eventBus) {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.commonSpec);
		eventBus.register(ConfigHandler.class);

		eventBus.addListener(CapabilityHandler::registerCapabilities);
		eventBus.addListener(PacketHandler::setupPackets);
		eventBus.addListener(this::setup);

		ForceFluids.registerFluids();

		ForceRegistry.BLOCKS.register(eventBus);
		ForceRegistry.BLOCK_ENTITY_TYPES.register(eventBus);
		ForceRegistry.ITEMS.register(eventBus);
		ForceRegistry.CREATIVE_MODE_TABS.register(eventBus);
		ForceSounds.SOUND_EVENTS.register(eventBus);
		ForceFluids.FLUID_TYPES.register(eventBus);
		ForceFluids.FLUIDS.register(eventBus);
		ForceEntities.ENTITY_TYPES.register(eventBus);
		ForceEffects.EFFECTS.register(eventBus);
		ForceFeatures.FEATURES.register(eventBus);
		ForceMenus.MENU_TYPES.register(eventBus);
		ForceLootModifiers.GLM.register(eventBus);
		ForceRecipes.RECIPE_TYPES.register(eventBus);
		ForceRecipeSerializers.RECIPE_SERIALIZERS.register(eventBus);
		ForceModifiers.BIOME_MODIFIER_SERIALIZERS.register(eventBus);
		ForceConditions.CONDITION_CODECS.register(eventBus);
		CapabilityHandler.ATTACHMENT_TYPES.register(eventBus);

		NeoForge.EVENT_BUS.register(new HeartHandler());
		NeoForge.EVENT_BUS.register(new ForceDeathHandler());
		NeoForge.EVENT_BUS.register(new ForceCommands());
		NeoForge.EVENT_BUS.register(new BaneHandler());
		NeoForge.EVENT_BUS.register(new PlayerCapHandler());
		NeoForge.EVENT_BUS.register(new LootingHandler());
		NeoForge.EVENT_BUS.register(new LootTableHandler());
		NeoForge.EVENT_BUS.register(new ToolModifierHandler());
		NeoForge.EVENT_BUS.register(new GrindstoneHandler());
		NeoForge.EVENT_BUS.addListener(NonBurnableItemEntity.EventHandler::onExpire); //Expire event of NonBurnableItemEntity

		eventBus.addListener(ForceEntities::registerEntityAttributes);
		eventBus.addListener(ForceEntities::registerSpawnPlacement);

		NeoForgeMod.enableMilkFluid(); //Enable milk from forge

		if (FMLEnvironment.dist.isClient()) {
			eventBus.addListener(ClientHandler::onClientSetup);
			eventBus.addListener(ClientHandler::onRegisterMenu);
			eventBus.addListener(ClientHandler::registerKeymapping);
			eventBus.addListener(ClientHandler::registerEntityRenders);
			eventBus.addListener(ClientHandler::registerLayerDefinitions);
			eventBus.addListener(ClientHandler::registerItemColors);
			NeoForge.EVENT_BUS.addListener(KeybindHandler::onClientTick);
		}
	}

	private void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			DispenserBlock.registerBehavior(ForceRegistry.FORCE_SHEARS.get(), new ShearsDispenseItemBehavior());
		});
	}
}

