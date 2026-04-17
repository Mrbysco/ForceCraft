package com.mrbysco.forcecraft.client;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.extension.ForceArmorItemExtension;
import com.mrbysco.forcecraft.client.extension.MagnetEffectExtension;
import com.mrbysco.forcecraft.client.gui.belt.ForceBeltScreen;
import com.mrbysco.forcecraft.client.gui.card.ItemCardScreen;
import com.mrbysco.forcecraft.client.gui.engine.ForceEngineScreen;
import com.mrbysco.forcecraft.client.gui.furnace.ForceFurnaceScreen;
import com.mrbysco.forcecraft.client.gui.infuser.InfuserScreen;
import com.mrbysco.forcecraft.client.gui.pack.ForcePackScreen;
import com.mrbysco.forcecraft.client.gui.spoils.SpoilsBagScreen;
import com.mrbysco.forcecraft.client.model.CreeperTotModel;
import com.mrbysco.forcecraft.client.model.EnderTotModel;
import com.mrbysco.forcecraft.client.model.FairyModel;
import com.mrbysco.forcecraft.client.renderer.BlueChuChuRenderer;
import com.mrbysco.forcecraft.client.renderer.ColdChickenRenderer;
import com.mrbysco.forcecraft.client.renderer.ColdCowRenderer;
import com.mrbysco.forcecraft.client.renderer.ColdPigRenderer;
import com.mrbysco.forcecraft.client.renderer.CreeperTotRenderer;
import com.mrbysco.forcecraft.client.renderer.EnderTotRenderer;
import com.mrbysco.forcecraft.client.renderer.FairyRenderer;
import com.mrbysco.forcecraft.client.renderer.ForceArrowRenderer;
import com.mrbysco.forcecraft.client.renderer.GoldChuChuRenderer;
import com.mrbysco.forcecraft.client.renderer.GreenChuChuRenderer;
import com.mrbysco.forcecraft.client.renderer.RedChuChuRenderer;
import com.mrbysco.forcecraft.components.ForceComponents;
import com.mrbysco.forcecraft.components.flask.FlaskContent;
import com.mrbysco.forcecraft.registry.ForceEffects;
import com.mrbysco.forcecraft.registry.ForceEntities;
import com.mrbysco.forcecraft.registry.ForceFluids;
import com.mrbysco.forcecraft.registry.ForceMenus;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.entity.EndermanRenderer;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import static com.mrbysco.forcecraft.components.ForceComponents.MAGNET;

@EventBusSubscriber(Dist.CLIENT)
public class ClientHandler {
	public static final ModelLayerLocation CREEPER_TOT = new ModelLayerLocation(Reference.modLoc("creeper_tot"), "main");
	public static final ModelLayerLocation FAIRY = new ModelLayerLocation(Reference.modLoc("fairy"), "main");
	public static final ModelLayerLocation ENDERTOT = new ModelLayerLocation(Reference.modLoc("endertot"), "main");

	@SubscribeEvent
	public static void onClientSetup(final FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(ForceFluids.FORCE_FLUID_FLOWING.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ForceFluids.FORCE_FLUID_SOURCE.get(), RenderType.translucent());

		event.enqueueWork(() -> {
			ItemProperties.register(ForceRegistry.MAGNET_GLOVE.get(), Reference.modLoc("active"), (stack, level, livingEntity, i) ->
					stack.getOrDefault(MAGNET, false) ? 1.0F : 0.0F);

			ItemProperties.register(ForceRegistry.ENTITY_FLASK.get(), Reference.modLoc("captured"), (stack, level, livingEntity, i) ->
					stack.has(ForceComponents.FLASK_CONTENT) ? 1.0F : 0.0F);

			ItemProperties.register(ForceRegistry.BACONATOR.get(), Reference.modLoc("filled"), (stack, level, livingEntity, i) ->
					stack.has(ForceComponents.STORED_FOOD) ? 1.0F : 0.0F);

			ItemProperties.register(ForceRegistry.FORCE_PACK.get(), Reference.modLoc("color"), (stack, level, livingEntity, i) ->
					stack.has(ForceComponents.PACK_COLOR) ? (1.0F / 16) * stack.getOrDefault(ForceComponents.PACK_COLOR, 0) : 0.9375F);

			ItemProperties.register(ForceRegistry.FORCE_BELT.get(), Reference.modLoc("color"), (stack, level, livingEntity, i) ->
					stack.has(ForceComponents.PACK_COLOR) ? (1.0F / 16) * stack.getOrDefault(ForceComponents.PACK_COLOR, 0) : 0.9375F);

			ItemProperties.register(ForceRegistry.FORCE_BOW.get(), Reference.modLoc("pull"), (stack, level, livingEntity, i) -> {
				if (livingEntity == null) {
					return 0.0F;
				} else {
					return livingEntity.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration(livingEntity) - livingEntity.getUseItemRemainingTicks()) / 20.0F;
				}
			});
			ItemProperties.register(ForceRegistry.FORCE_BOW.get(), Reference.modLoc("pulling"), (stack, level, livingEntity, i) ->
					livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == stack ? 1.0F : 0.0F);
		});
	}

	@SubscribeEvent
	public static void onRegisterMenu(final RegisterMenuScreensEvent event) {
		event.register(ForceMenus.FORCE_FURNACE.get(), ForceFurnaceScreen::new);
		event.register(ForceMenus.INFUSER.get(), InfuserScreen::new);
		event.register(ForceMenus.FORCE_BELT.get(), ForceBeltScreen::new);
		event.register(ForceMenus.FORCE_PACK.get(), ForcePackScreen::new);
		event.register(ForceMenus.SPOILS_BAG.get(), SpoilsBagScreen::new);
		event.register(ForceMenus.ITEM_CARD.get(), ItemCardScreen::new);
		event.register(ForceMenus.FORCE_ENGINE.get(), ForceEngineScreen::new);
	}

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(CREEPER_TOT, CreeperTotModel::createBodyLayer);
		event.registerLayerDefinition(ENDERTOT, EnderTotModel::createBodyLayer);
		event.registerLayerDefinition(FAIRY, FairyModel::createBodyLayer);
	}

	@SubscribeEvent
	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ForceEntities.NON_BURNABLE_ITEM.get(), ItemEntityRenderer::new);
		event.registerEntityRenderer(ForceEntities.COLD_CHICKEN.get(), ColdChickenRenderer::new);
		event.registerEntityRenderer(ForceEntities.COLD_COW.get(), ColdCowRenderer::new);
		event.registerEntityRenderer(ForceEntities.COLD_PIG.get(), ColdPigRenderer::new);
		event.registerEntityRenderer(ForceEntities.RED_CHU_CHU.get(), RedChuChuRenderer::new);
		event.registerEntityRenderer(ForceEntities.GREEN_CHU_CHU.get(), GreenChuChuRenderer::new);
		event.registerEntityRenderer(ForceEntities.BLUE_CHU_CHU.get(), BlueChuChuRenderer::new);
		event.registerEntityRenderer(ForceEntities.GOLD_CHU_CHU.get(), GoldChuChuRenderer::new);
		event.registerEntityRenderer(ForceEntities.FAIRY.get(), FairyRenderer::new);
		event.registerEntityRenderer(ForceEntities.CREEPER_TOT.get(), CreeperTotRenderer::new);
		event.registerEntityRenderer(ForceEntities.ENDER_TOT.get(), EnderTotRenderer::new);
		event.registerEntityRenderer(ForceEntities.ANGRY_ENDERMAN.get(), EndermanRenderer::new);
		event.registerEntityRenderer(ForceEntities.FORCE_ARROW.get(), ForceArrowRenderer::new);
		event.registerEntityRenderer(ForceEntities.FORCE_FLASK.get(), ThrownItemRenderer::new);
	}

	@SubscribeEvent
	public static void registerItemColors(final RegisterColorHandlersEvent.Item event) {
		event.register((stack, tintIndex) -> {
			if (tintIndex == 0 || tintIndex == 1) {
				if (stack.has(ForceComponents.FLASK_CONTENT)) {
					FlaskContent content = stack.get(ForceComponents.FLASK_CONTENT);
					Identifier id = content != null ? content.storedType() : BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.EGG);
					SpawnEggItem info = SpawnEggItem.byId(BuiltInRegistries.ENTITY_TYPE.get(id));

					if (info != null) {
						return tintIndex == 0 ? ARGB.opaque(info.getColor(0)) : ARGB.opaque(info.getColor(1));
					} else {
						if (tintIndex == 0)
							return ARGB.opaque(10489616);
						return ARGB.opaque(951412);
					}
				}
				return 0xFFFFFFFF;
			}
			return 0xFFFFFFFF;
		}, ForceRegistry.ENTITY_FLASK.get());
	}

	@SubscribeEvent
	public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
		event.registerItem(new ForceArmorItemExtension(), ForceRegistry.FORCE_BOOTS, ForceRegistry.FORCE_LEGS, ForceRegistry.FORCE_CHEST, ForceRegistry.FORCE_HELMET);
		event.registerMobEffect(new MagnetEffectExtension(), ForceEffects.MAGNET);
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
}
