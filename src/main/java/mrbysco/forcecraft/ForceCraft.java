package mrbysco.forcecraft;

import mrbysco.forcecraft.capablilities.CapabilityAttachHandler;
import mrbysco.forcecraft.capablilities.CapabilityHandler;
import mrbysco.forcecraft.client.ClientHandler;
import mrbysco.forcecraft.client.KeybindHandler;
import mrbysco.forcecraft.command.ForceCommands;
import mrbysco.forcecraft.config.ConfigHandler;
import mrbysco.forcecraft.handlers.BaneHandler;
import mrbysco.forcecraft.handlers.HeartHandler;
import mrbysco.forcecraft.handlers.PlayerCapHandler;
import mrbysco.forcecraft.handlers.LootTableHandler;
import mrbysco.forcecraft.handlers.LootingHandler;
import mrbysco.forcecraft.handlers.ToolModifierHandler;
import mrbysco.forcecraft.items.nonburnable.NonBurnableItemEntity;
import mrbysco.forcecraft.networking.PacketHandler;
import mrbysco.forcecraft.recipe.ForceRecipes;
import mrbysco.forcecraft.recipe.condition.ForceConditions;
import mrbysco.forcecraft.registry.ForceContainers;
import mrbysco.forcecraft.registry.ForceEffects;
import mrbysco.forcecraft.registry.ForceEntities;
import mrbysco.forcecraft.registry.ForceFluids;
import mrbysco.forcecraft.registry.ForceLootModifiers;
import mrbysco.forcecraft.registry.ForceRegistry;
import mrbysco.forcecraft.registry.ForceSounds;
import mrbysco.forcecraft.world.WorldGenHandler;
import mrbysco.forcecraft.world.feature.ForceFeatures;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
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

    public static final DamageSource BLEEDING_DAMAGE = new DamageSource(Reference.MOD_ID + ".bleeding").setMagicDamage().setDamageBypassesArmor();

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

        eventBus.register(new ForceConditions());
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
        ForceRecipes.RECIPE_SERIALIZERS.register(eventBus);

        MinecraftForge.EVENT_BUS.register(new HeartHandler());
        MinecraftForge.EVENT_BUS.register(new ForceCommands());
        MinecraftForge.EVENT_BUS.register(new CapabilityAttachHandler());
        MinecraftForge.EVENT_BUS.register(new BaneHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerCapHandler());
        MinecraftForge.EVENT_BUS.register(new LootingHandler());
        MinecraftForge.EVENT_BUS.register(new LootTableHandler());
        MinecraftForge.EVENT_BUS.register(new ToolModifierHandler());
        MinecraftForge.EVENT_BUS.register(new WorldGenHandler());
        MinecraftForge.EVENT_BUS.addListener(NonBurnableItemEntity.EventHandler::onExpire); //Expire event of NonBurnableItemEntity

        MinecraftForge.EVENT_BUS.addListener(ForceEntities::addSpawns);
        eventBus.addListener(ForceEntities::registerEntityAttributes);

        ForgeMod.enableMilkFluid(); //Enable milk from forge

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            eventBus.addListener(ClientHandler::onClientSetup);
            eventBus.addListener(ClientHandler::registerItemColors);
            MinecraftForge.EVENT_BUS.addListener(KeybindHandler::onClientTick);
        });
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketHandler.init();
        CapabilityHandler.register();
        ForceEntities.registerSpawnPlacement();
    }
}

