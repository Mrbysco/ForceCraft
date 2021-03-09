package mrbysco.forcecraft;

import mrbysco.forcecraft.capablilities.CapabilityAttachHandler;
import mrbysco.forcecraft.capablilities.CapabilityHandler;
import mrbysco.forcecraft.client.ClientHandler;
import mrbysco.forcecraft.config.ConfigHandler;
import mrbysco.forcecraft.handlers.BaneHandler;
import mrbysco.forcecraft.handlers.LivingUpdateHandler;
import mrbysco.forcecraft.handlers.LootTableHandler;
import mrbysco.forcecraft.handlers.LootingHandler;
import mrbysco.forcecraft.handlers.ToolModifierHandler;
import mrbysco.forcecraft.world.WorldGenHandler;
import mrbysco.forcecraft.items.nonburnable.NonBurnableItemEntity;
import mrbysco.forcecraft.networking.PacketHandler;
import mrbysco.forcecraft.registry.ForceContainers;
import mrbysco.forcecraft.registry.ForceEffects;
import mrbysco.forcecraft.registry.ForceEntities;
import mrbysco.forcecraft.registry.ForceFluids;
import mrbysco.forcecraft.registry.ForceLootModifiers;
import mrbysco.forcecraft.registry.ForceRegistry;
import mrbysco.forcecraft.registry.ForceSounds;
import mrbysco.forcecraft.world.feature.ForceFeatures;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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

    public static final ItemGroup creativeTab = (new ItemGroup(Reference.MOD_ID) {
        @OnlyIn(Dist.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(ForceRegistry.FORCE_GEM.get());
        }
    });

    public ForceCraft() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.commonSpec);
        eventBus.register(ConfigHandler.class);

        eventBus.addListener(this::setup);

        ForceFluids.registerFluids();

        ForceRegistry.BLOCKS.register(eventBus);
        ForceRegistry.TILES.register(eventBus);
        ForceRegistry.ITEMS.register(eventBus);
        ForceSounds.SOUND_EVENTS.register(eventBus);
        ForceFluids.FLUIDS.register(eventBus);
        ForceEntities.ENTITIES.register(eventBus);
        ForceEffects.EFFECTS.register(eventBus);
        ForceFeatures.FEATURES.register(eventBus);
        ForceContainers.CONTAINERS.register(eventBus);
        ForceLootModifiers.GLM.register(eventBus);

        MinecraftForge.EVENT_BUS.register(new CapabilityAttachHandler());
        MinecraftForge.EVENT_BUS.register(new BaneHandler());
        MinecraftForge.EVENT_BUS.register(new LivingUpdateHandler());
        MinecraftForge.EVENT_BUS.register(new LootingHandler());
        MinecraftForge.EVENT_BUS.register(new LootTableHandler());
        MinecraftForge.EVENT_BUS.register(new ToolModifierHandler());
        MinecraftForge.EVENT_BUS.register(new WorldGenHandler());

        MinecraftForge.EVENT_BUS.addListener(NonBurnableItemEntity.EventHandler::onExpire); //Expire event of NonBurnableItemEntity

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            eventBus.addListener(ClientHandler::onClientSetup);
        });
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketHandler.init();
        CapabilityHandler.register();
    }
}

