package com.mrbysco.forcecraft;

import com.mrbysco.forcecraft.capabilities.CapabilityAttachHandler;
import com.mrbysco.forcecraft.capabilities.CapabilityHandler;
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
import com.mrbysco.forcecraft.registry.ForceEffects;
import com.mrbysco.forcecraft.registry.ForceEntities;
import com.mrbysco.forcecraft.registry.ForceFluids;
import com.mrbysco.forcecraft.registry.ForceLootModifiers;
import com.mrbysco.forcecraft.registry.ForceMenus;
import com.mrbysco.forcecraft.registry.ForceModifiers;
import com.mrbysco.forcecraft.registry.ForceRecipeSerializers;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceSounds;
import com.mrbysco.forcecraft.world.feature.ForceFeatures;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mod(Reference.MOD_ID)
public class ForceCraft {

	public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);

	public static final DamageSource BLEEDING_DAMAGE = new DamageSource(Reference.MOD_ID + ".bleeding").setMagic().bypassArmor();
	public static final DamageSource LIQUID_FORCE_DAMAGE = new DamageSource(Reference.MOD_ID + ".liquid_force").setMagic().bypassArmor();

	private static CreativeModeTab creativeTab;

	public ForceCraft() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.commonSpec);
		eventBus.register(ConfigHandler.class);

		eventBus.register(new ForceConditions());
		eventBus.addListener(this::setup);

		ForceFluids.registerFluids();

		ForceRegistry.BLOCKS.register(eventBus);
		ForceRegistry.BLOCK_ENTITY_TYPES.register(eventBus);
		ForceRegistry.ITEMS.register(eventBus);
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

		MinecraftForge.EVENT_BUS.register(new HeartHandler());
		MinecraftForge.EVENT_BUS.register(new ForceDeathHandler());
		MinecraftForge.EVENT_BUS.register(new ForceCommands());
		MinecraftForge.EVENT_BUS.register(new CapabilityAttachHandler());
		MinecraftForge.EVENT_BUS.register(new BaneHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerCapHandler());
		MinecraftForge.EVENT_BUS.register(new LootingHandler());
		MinecraftForge.EVENT_BUS.register(new LootTableHandler());
		MinecraftForge.EVENT_BUS.register(new ToolModifierHandler());
		MinecraftForge.EVENT_BUS.addListener(NonBurnableItemEntity.EventHandler::onExpire); //Expire event of NonBurnableItemEntity

		MinecraftForge.EVENT_BUS.addListener(CapabilityHandler::register);

		eventBus.addListener(this::registerCreativeTabs);
		eventBus.addListener(ForceEntities::registerEntityAttributes);
		eventBus.addListener(ForceEntities::registerSpawnPlacement);

		ForgeMod.enableMilkFluid(); //Enable milk from forge

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			eventBus.addListener(ClientHandler::onClientSetup);
			eventBus.addListener(ClientHandler::registerKeymapping);
			eventBus.addListener(ClientHandler::registerEntityRenders);
			eventBus.addListener(ClientHandler::registerLayerDefinitions);
			eventBus.addListener(ClientHandler::registerItemColors);
			MinecraftForge.EVENT_BUS.addListener(KeybindHandler::onClientTick);
		});
	}

	private void setup(final FMLCommonSetupEvent event) {
		PacketHandler.init();
		event.enqueueWork(() -> {
			DispenserBlock.registerBehavior(ForceRegistry.FORCE_SHEARS.get(), new ShearsDispenseItemBehavior());
		});
	}

	private void registerCreativeTabs(final CreativeModeTabEvent.Register event) {
		creativeTab = event.registerCreativeModeTab(new ResourceLocation(Reference.MOD_ID, "tab"), builder ->
				builder.icon(() -> new ItemStack(ForceRegistry.FORCE_GEM.get()))
						.title(Component.translatable("itemGroup.forcecraft"))
						.displayItems((features, output, hasPermissions) -> {
							List<ItemStack> stacks = ForceRegistry.ITEMS.getEntries().stream().map(reg -> new ItemStack(reg.get())).toList();
							output.acceptAll(stacks);
						}));
	}
}

