package com.mrbysco.forcecraft.client;

import com.mrbysco.forcecraft.blocks.infuser.InfuserScreen;
import com.mrbysco.forcecraft.capablilities.magnet.IMagnet;
import com.mrbysco.forcecraft.client.gui.belt.ForceBeltScreen;
import com.mrbysco.forcecraft.client.gui.card.ItemCardScreen;
import com.mrbysco.forcecraft.client.gui.engine.ForceEngineScreen;
import com.mrbysco.forcecraft.client.gui.furnace.ForceFurnaceScreen;
import com.mrbysco.forcecraft.client.gui.pack.ForcePackScreen;
import com.mrbysco.forcecraft.client.gui.spoils.SpoilsBagScreen;
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
import com.mrbysco.forcecraft.items.BaconatorItem;
import com.mrbysco.forcecraft.items.CustomSpawnEggItem;
import com.mrbysco.forcecraft.registry.ForceContainers;
import com.mrbysco.forcecraft.registry.ForceEntities;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.EndermanRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_MAGNET;

public class ClientHandler {
	public static void onClientSetup(final FMLClientSetupEvent event) {
		ClientRegistry.registerKeyBinding(KeybindHandler.KEY_OPEN_HOTBAR_PACK);
		ClientRegistry.registerKeyBinding(KeybindHandler.KEY_OPEN_HOTBAR_BELT);
		ClientRegistry.registerKeyBinding(KeybindHandler.KEY_QUICK_USE_1);
		ClientRegistry.registerKeyBinding(KeybindHandler.KEY_QUICK_USE_2);
		ClientRegistry.registerKeyBinding(KeybindHandler.KEY_QUICK_USE_3);
		ClientRegistry.registerKeyBinding(KeybindHandler.KEY_QUICK_USE_4);
		ClientRegistry.registerKeyBinding(KeybindHandler.KEY_QUICK_USE_5);
		ClientRegistry.registerKeyBinding(KeybindHandler.KEY_QUICK_USE_6);
		ClientRegistry.registerKeyBinding(KeybindHandler.KEY_QUICK_USE_7);
		ClientRegistry.registerKeyBinding(KeybindHandler.KEY_QUICK_USE_8);

		ScreenManager.registerFactory(ForceContainers.FORCE_FURNACE.get(), ForceFurnaceScreen::new);
		ScreenManager.registerFactory(ForceContainers.INFUSER.get(), InfuserScreen::new);
		ScreenManager.registerFactory(ForceContainers.FORCE_BELT.get(), ForceBeltScreen::new);
		ScreenManager.registerFactory(ForceContainers.FORCE_PACK.get(), ForcePackScreen::new);
		ScreenManager.registerFactory(ForceContainers.SPOILS_BAG.get(), SpoilsBagScreen::new);
		ScreenManager.registerFactory(ForceContainers.ITEM_CARD.get(), ItemCardScreen::new);
		ScreenManager.registerFactory(ForceContainers.FORCE_ENGINE.get(), ForceEngineScreen::new);

		RenderTypeLookup.setRenderLayer(ForceRegistry.POWER_ORE.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_SAPLING.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_RED_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_ORANGE_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_GREEN_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_BLUE_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_WHITE_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_BLACK_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_BROWN_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_LIGHT_BLUE_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_MAGENTA_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_PINK_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_LIGHT_GRAY_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_LIME_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_CYAN_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_PURPLE_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_GRAY_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.WALL_FORCE_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.WALL_FORCE_RED_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.WALL_FORCE_ORANGE_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.WALL_FORCE_GREEN_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.WALL_FORCE_BLUE_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.WALL_FORCE_WHITE_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.WALL_FORCE_BLACK_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.WALL_FORCE_BROWN_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.WALL_FORCE_LIGHT_BLUE_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.WALL_FORCE_MAGENTA_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.WALL_FORCE_PINK_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.WALL_FORCE_LIGHT_GRAY_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.WALL_FORCE_LIME_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.WALL_FORCE_CYAN_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.WALL_FORCE_PURPLE_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.WALL_FORCE_GRAY_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.TIME_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.WALL_TIME_TORCH.get(), RenderType.getCutout());

		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_LEAVES.get(), RenderType.getCutoutMipped());

		net.minecraft.client.renderer.ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		RenderingRegistry.registerEntityRenderingHandler(ForceEntities.NON_BURNABLE_ITEM.get(), renderManager -> new ItemRenderer(renderManager, itemRenderer));
		RenderingRegistry.registerEntityRenderingHandler(ForceEntities.COLD_CHICKEN.get(), ColdChickenRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ForceEntities.COLD_COW.get(), ColdCowRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ForceEntities.COLD_PIG.get(), ColdPigRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ForceEntities.RED_CHU_CHU.get(), RedChuChuRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ForceEntities.GREEN_CHU_CHU.get(), GreenChuChuRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ForceEntities.BLUE_CHU_CHU.get(), BlueChuChuRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ForceEntities.GOLD_CHU_CHU.get(), GoldChuChuRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ForceEntities.FAIRY.get(), FairyRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ForceEntities.CREEPER_TOT.get(), CreeperTotRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ForceEntities.ENDER_TOT.get(), EnderTotRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ForceEntities.ANGRY_ENDERMAN.get(), EndermanRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ForceEntities.FORCE_ARROW.get(), ForceArrowRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ForceEntities.FORCE_FLASK.get(), renderManager -> new SpriteRenderer<>(renderManager, Minecraft.getInstance().getItemRenderer()));

		ItemModelsProperties.registerProperty(ForceRegistry.MAGNET_GLOVE.get(), new ResourceLocation("active"), (stack, world, livingEntity) -> {
			IMagnet magnetCap = stack.getCapability(CAPABILITY_MAGNET).orElse(null);
			return magnetCap != null && magnetCap.isActivated() ? 1.0F : 0.0F;
		});

		ItemModelsProperties.registerProperty(ForceRegistry.ENTITY_FLASK.get(), new ResourceLocation("captured"), (stack, world, livingEntity) ->
				stack.getOrCreateTag().contains("StoredEntity") ? 1.0F : 0.0F);

		ItemModelsProperties.registerProperty(ForceRegistry.BACONATOR.get(), new ResourceLocation("filled"), (stack, world, livingEntity) ->
				stack.getOrCreateTag().contains(BaconatorItem.HAS_FOOD_TAG) ? 1.0F : 0.0F);

		ItemModelsProperties.registerProperty(ForceRegistry.FORCE_PACK.get(), new ResourceLocation("color"), (stack, world, livingEntity) ->
				stack.getOrCreateTag().contains("Color") ? (1.0F / 16) * stack.getOrCreateTag().getInt("Color") : 0.9375F);

		ItemModelsProperties.registerProperty(ForceRegistry.FORCE_BELT.get(), new ResourceLocation("color"), (stack, world, livingEntity) ->
				 stack.getOrCreateTag().contains("Color") ? (1.0F / 16) * stack.getOrCreateTag().getInt("Color") : 0.9375F);

		ItemModelsProperties.registerProperty(ForceRegistry.FORCE_BOW.get(), new ResourceLocation("pull"), (stack, world, livingEntity) -> {
			if (livingEntity == null) {
				return 0.0F;
			} else {
				return livingEntity.getActiveItemStack() != stack ? 0.0F : (float)(stack.getUseDuration() - livingEntity.getItemInUseCount()) / 20.0F;
			}
		});
		ItemModelsProperties.registerProperty(ForceRegistry.FORCE_BOW.get(), new ResourceLocation("pulling"), (stack, world, livingEntity) ->
				livingEntity != null && livingEntity.isHandActive() && livingEntity.getActiveItemStack() == stack ? 1.0F : 0.0F);
	}

	public static void registerItemColors(final ColorHandlerEvent.Item event) {
		ItemColors colors = event.getItemColors();

		for(RegistryObject<Item> registryObject : ForceRegistry.ITEMS.getEntries()) {
			if(registryObject.get() instanceof CustomSpawnEggItem) {
				CustomSpawnEggItem spawnEgg = (CustomSpawnEggItem) registryObject.get();
				colors.register((stack, tintIndex) -> spawnEgg.getColor(tintIndex), spawnEgg);
			}
		}

		colors.register((stack, tintIndex) -> {
			if (tintIndex == 0 || tintIndex == 1) {
				if(stack.hasTag() && stack.getTag().contains("StoredEntity", Constants.NBT.TAG_STRING)) {
					SpawnEggItem info = null;
					ResourceLocation id = new ResourceLocation(stack.getTag().getString("StoredEntity"));
					info = SpawnEggItem.getEgg(ForgeRegistries.ENTITIES.getValue(id));

					if(info != null) {
						return tintIndex == 0 ? info.getColor(0) : info.getColor(1);
					} else {
						return tintIndex == 0 ? 10489616 : tintIndex == 1 ? 951412 : 0xFFFFFF;
					}
				}
				return 0xFFFFFF;
			}
			return 0xFFFFFF;
		}, ForceRegistry.ENTITY_FLASK.get());
	}
}
