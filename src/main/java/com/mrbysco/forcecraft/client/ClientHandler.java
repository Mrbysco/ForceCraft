package com.mrbysco.forcecraft.client;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.extension.ForceArmorItemExtension;
import com.mrbysco.forcecraft.client.model.BombModel;
import com.mrbysco.forcecraft.client.model.EngineModel;
import com.mrbysco.forcecraft.client.renderer.BombRenderer;
import com.mrbysco.forcecraft.client.renderer.EngineBER;
import com.mrbysco.forcecraft.client.renderer.IceArrowRenderer;
import com.mrbysco.forcecraft.client.screen.ForceEngineScreen;
import com.mrbysco.forcecraft.client.screen.ForceFurnaceScreen;
import com.mrbysco.forcecraft.client.screen.InfuserScreen;
import com.mrbysco.forcecraft.client.screen.belt.ForceBeltScreen;
import com.mrbysco.forcecraft.client.screen.pack.ForcePackScreen;
import com.mrbysco.forcecraft.client.tint.RodColor;
import com.mrbysco.forcecraft.registry.ForceEntities;
import com.mrbysco.forcecraft.registry.ForceFluids;
import com.mrbysco.forcecraft.registry.ForceMenus;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(Dist.CLIENT)
public class ClientHandler {
	public static final ModelLayerLocation ENGINE = new ModelLayerLocation(Reference.modLoc("engine"), "main");
	public static final ModelLayerLocation BOMB = new ModelLayerLocation(Reference.modLoc("bomb"), "main");

	@SubscribeEvent
	public static void onRegisterMenu(final RegisterMenuScreensEvent event) {
		event.register(ForceMenus.FORCE_ENGINE.get(), ForceEngineScreen::new);
		event.register(ForceMenus.INFUSER.get(), InfuserScreen::new);
		event.register(ForceMenus.FORCE_FURNACE.get(), ForceFurnaceScreen::new);
		event.register(ForceMenus.FORCE_BELT.get(), ForceBeltScreen::new);
		event.register(ForceMenus.FORCE_PACK.get(), ForcePackScreen::new);
	}

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ENGINE, EngineModel::createBodyLayer);
		event.registerLayerDefinition(BOMB, BombModel::createBodyLayer);
	}

	@SubscribeEvent
	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ForceEntities.BOMB.get(), BombRenderer::new);
		event.registerEntityRenderer(ForceEntities.FORCE_ARROW.get(), IceArrowRenderer::new);

		event.registerBlockEntityRenderer(ForceRegistry.FORCE_ENGINE_BLOCK_ENTITY.get(), EngineBER::new);
	}

	@SubscribeEvent
	public static void registerItemColors(final RegisterColorHandlersEvent.ItemTintSources event) {
		event.register(Reference.modLoc("rod_color"), RodColor.MAP_CODEC);
	}

	private static final Identifier FORCE_FLUID_SOURCE = Reference.modLoc("block/force_fluid_source");
	private static final Identifier FORCE_FLUID_FLOWING = Reference.modLoc("block/force_fluid_flowing");

	@SubscribeEvent
	public static void registerFluidModels(final RegisterFluidModelsEvent event) {
		Fluid stillFluid = ForceFluids.FORCE_FLUID_SOURCE.value();
		Fluid flowingFluid = ForceFluids.FORCE_FLUID_FLOWING.value();
		event.register(new FluidModel.Unbaked(
				new Material(FORCE_FLUID_SOURCE),
				new Material(FORCE_FLUID_FLOWING),
				null,
				null), stillFluid, flowingFluid
		);
	}

	@SubscribeEvent
	public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
		event.registerItem(new ForceArmorItemExtension(),
				ForceRegistry.FORCE_BOOTS, ForceRegistry.FORCE_LEGS,
				ForceRegistry.FORCE_CHEST, ForceRegistry.FORCE_HELMET
		);
//		event.registerMobEffect(new MagnetEffectExtension(), ForceEffects.MAGNET);
	}
}
