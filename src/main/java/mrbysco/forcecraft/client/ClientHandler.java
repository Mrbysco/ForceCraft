package mrbysco.forcecraft.client;

import mrbysco.forcecraft.blocks.infuser.InfuserScreen;
import mrbysco.forcecraft.capablilities.magnet.IMagnet;
import mrbysco.forcecraft.client.gui.belt.ForceBeltScreen;
import mrbysco.forcecraft.client.gui.furnace.ForceFurnaceScreen;
import mrbysco.forcecraft.client.gui.pack.ForcePackScreen;
import mrbysco.forcecraft.client.gui.spoils.SpoilsBagScreen;
import mrbysco.forcecraft.client.renderer.ColdChickenRenderer;
import mrbysco.forcecraft.client.renderer.ColdCowRenderer;
import mrbysco.forcecraft.client.renderer.ColdPigRenderer;
import mrbysco.forcecraft.client.renderer.ForceArrowRenderer;
import mrbysco.forcecraft.registry.ForceContainers;
import mrbysco.forcecraft.registry.ForceEntities;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_MAGNET;

public class ClientHandler {
	public static void onClientSetup(final FMLClientSetupEvent event) {
		ScreenManager.registerFactory(ForceContainers.FORCE_FURNACE.get(), ForceFurnaceScreen::new);
		ScreenManager.registerFactory(ForceContainers.INFUSER.get(), InfuserScreen::new);
		ScreenManager.registerFactory(ForceContainers.FORCE_BELT.get(), ForceBeltScreen::new);
		ScreenManager.registerFactory(ForceContainers.FORCE_PACK.get(), ForcePackScreen::new);
		ScreenManager.registerFactory(ForceContainers.SPOILS_BAG.get(), SpoilsBagScreen::new);

		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_SAPLING.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_RED_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_YELLOW_TORCH.get(), RenderType.getCutout());
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
		RenderTypeLookup.setRenderLayer(ForceRegistry.WALL_FORCE_YELLOW_TORCH.get(), RenderType.getCutout());
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
		RenderingRegistry.registerEntityRenderingHandler(ForceEntities.FORCE_ARROW.get(), ForceArrowRenderer::new);

		ItemModelsProperties.registerProperty(ForceRegistry.MAGNET_GLOVE.get(), new ResourceLocation("active"), (stack, world, livingEntity) -> {
			IMagnet magnetCap = stack.getCapability(CAPABILITY_MAGNET).orElse(null);
			return magnetCap != null && magnetCap.isActivated() ? 1.0F : 0.0F;
		});

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
}
