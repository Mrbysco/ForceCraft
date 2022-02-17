package com.mrbysco.forcecraft;

import com.mrbysco.forcecraft.capablilities.CapabilityAttachHandler;
import com.mrbysco.forcecraft.capablilities.CapabilityHandler;
import com.mrbysco.forcecraft.client.ClientHandler;
import com.mrbysco.forcecraft.client.KeybindHandler;
import com.mrbysco.forcecraft.command.ForceCommands;
import com.mrbysco.forcecraft.config.ConfigHandler;
import com.mrbysco.forcecraft.handlers.BaneHandler;
import com.mrbysco.forcecraft.handlers.ForceDeathHandler;
import com.mrbysco.forcecraft.handlers.HeartHandler;
import com.mrbysco.forcecraft.handlers.LootTableHandler;
import com.mrbysco.forcecraft.handlers.LootingHandler;
import com.mrbysco.forcecraft.handlers.PlayerCapHandler;
import com.mrbysco.forcecraft.handlers.ToolModifierHandler;
import com.mrbysco.forcecraft.items.nonburnable.NonBurnableItemEntity;
import com.mrbysco.forcecraft.networking.PacketHandler;
import com.mrbysco.forcecraft.recipe.ForceRecipes;
import com.mrbysco.forcecraft.recipe.condition.ForceConditions;
import com.mrbysco.forcecraft.registry.ForceContainers;
import com.mrbysco.forcecraft.registry.ForceEffects;
import com.mrbysco.forcecraft.registry.ForceEntities;
import com.mrbysco.forcecraft.registry.ForceFluids;
import com.mrbysco.forcecraft.registry.ForceLootModifiers;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceSounds;
import com.mrbysco.forcecraft.registry.ForceTags;
import com.mrbysco.forcecraft.world.WorldGenHandler;
import com.mrbysco.forcecraft.world.feature.ForceFeatureConfigs;
import com.mrbysco.forcecraft.world.feature.ForceFeatures;
import com.mrbysco.forcecraft.world.feature.ForcePlacements;
import com.mrbysco.forcecraft.world.feature.ForceVegetation;
import com.mrbysco.forcecraft.world.feature.ForceVegetationPlacements;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MOD_ID)
public class ForceCraft {

    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);

    public static final DamageSource BLEEDING_DAMAGE = new DamageSource(Reference.MOD_ID + ".bleeding").setMagic().bypassArmor();
    public static final DamageSource LIQUID_FORCE_DAMAGE = new DamageSource(Reference.MOD_ID + ".liquid_force").setMagic().bypassArmor();

    public static final CreativeModeTab creativeTab = (new CreativeModeTab(Reference.MOD_ID) {
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(ForceRegistry.FORCE_GEM.get());
        }
    });

    public ForceCraft() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.commonSpec);
        eventBus.register(ConfigHandler.class);

        eventBus.register(new ForceConditions());
        eventBus.addListener(this::setup);

        ForceFluids.registerFluids();

        ForceRegistry.BLOCKS.register(eventBus);
        ForceRegistry.BLOCK_ENTITIES.register(eventBus);
        ForceRegistry.ITEMS.register(eventBus);
        ForceSounds.SOUND_EVENTS.register(eventBus);
        ForceFluids.FLUIDS.register(eventBus);
        ForceEntities.ENTITIES.register(eventBus);
        ForceEffects.EFFECTS.register(eventBus);
        ForceFeatures.FEATURES.register(eventBus);
        ForceContainers.CONTAINERS.register(eventBus);
        ForceLootModifiers.GLM.register(eventBus);
        ForceRecipes.RECIPE_SERIALIZERS.register(eventBus);

        MinecraftForge.EVENT_BUS.register(new HeartHandler());
        MinecraftForge.EVENT_BUS.register(new ForceDeathHandler());
        MinecraftForge.EVENT_BUS.register(new ForceCommands());
        MinecraftForge.EVENT_BUS.register(new CapabilityAttachHandler());
        MinecraftForge.EVENT_BUS.register(new BaneHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerCapHandler());
        MinecraftForge.EVENT_BUS.register(new LootingHandler());
        MinecraftForge.EVENT_BUS.register(new LootTableHandler());
        MinecraftForge.EVENT_BUS.register(new ToolModifierHandler());
        MinecraftForge.EVENT_BUS.register(new WorldGenHandler());
        MinecraftForge.EVENT_BUS.addListener(NonBurnableItemEntity.EventHandler::onExpire); //Expire event of NonBurnableItemEntity

        MinecraftForge.EVENT_BUS.addListener(CapabilityHandler::register);

        MinecraftForge.EVENT_BUS.addListener(ForceEntities::addSpawns);
        eventBus.addListener(ForceEntities::registerEntityAttributes);

        ForgeMod.enableMilkFluid(); //Enable milk from forge

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            eventBus.addListener(ClientHandler::onClientSetup);
            eventBus.addListener(ClientHandler::registerEntityRenders);
            eventBus.addListener(ClientHandler::registerLayerDefinitions);
            eventBus.addListener(ClientHandler::registerItemColors);
            MinecraftForge.EVENT_BUS.addListener(KeybindHandler::onClientTick);
        });
    }

    private void setup(final FMLCommonSetupEvent event) {
        ForceTags.initialize();
        PacketHandler.init();
        ForceEntities.registerSpawnPlacement();
        ForceFeatureConfigs.initialize();
        ForcePlacements.initialize();
        ForceVegetation.initialize();
        ForceVegetationPlacements.initialize();
    }
}

