package mrbysco.forcecraft.client;

import mrbysco.forcecraft.capablilities.magnet.IMagnet;
import mrbysco.forcecraft.client.gui.belt.ForceBeltScreen;
import mrbysco.forcecraft.client.gui.furnace.ForceFurnaceScreen;
import mrbysco.forcecraft.client.gui.pack.ForcePackScreen;
import mrbysco.forcecraft.client.renderer.ColdChickenRenderer;
import mrbysco.forcecraft.client.renderer.ColdCowRenderer;
import mrbysco.forcecraft.client.renderer.ColdPigRenderer;
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

import mrbysco.forcecraft.blocks.infuser.InfuserScreen;

public class ClientHandler {
	public static void onClientSetup(final FMLClientSetupEvent event) {
		ScreenManager.registerFactory(ForceContainers.FORCE_FURNACE.get(), ForceFurnaceScreen::new);
		ScreenManager.registerFactory(ForceContainers.INFUSER.get(), InfuserScreen::new);
		ScreenManager.registerFactory(ForceContainers.FORCE_BELT.get(), ForceBeltScreen::new);
		ScreenManager.registerFactory(ForceContainers.FORCE_PACK.get(), ForcePackScreen::new);

		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_SAPLING.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.WALL_FORCE_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.TIME_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.WALL_TIME_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ForceRegistry.FORCE_LEAVES.get(), RenderType.getCutoutMipped());

		net.minecraft.client.renderer.ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		RenderingRegistry.registerEntityRenderingHandler(ForceEntities.NON_BURNABLE_ITEM.get(), renderManager -> new ItemRenderer(renderManager, itemRenderer));
		RenderingRegistry.registerEntityRenderingHandler(ForceEntities.COLD_CHICKEN.get(), ColdChickenRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ForceEntities.COLD_COW.get(), ColdCowRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ForceEntities.COLD_PIG.get(), ColdPigRenderer::new);

		ItemModelsProperties.registerProperty(ForceRegistry.MAGNET_GLOVE.get(), new ResourceLocation("active"), (stack, world, livingEntity) -> {
			IMagnet magnetCap = stack.getCapability(CAPABILITY_MAGNET).orElse(null);
			return magnetCap != null && magnetCap.isActivated() ? 1.0F : 0.0F;
		});

		ItemModelsProperties.registerProperty(ForceRegistry.FORCE_PACK.get(), new ResourceLocation("color"), (stack, world, livingEntity) ->
				stack.getTag() != null && stack.getTag().contains("Color") ? (1.0F / 16) * stack.getTag().getInt("Color") : 0.9375F);
	}
}
