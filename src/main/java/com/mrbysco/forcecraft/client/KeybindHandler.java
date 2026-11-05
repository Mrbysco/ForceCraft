package com.mrbysco.forcecraft.client;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.networking.message.OpenForceMenuPayload;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(Dist.CLIENT)
public class KeybindHandler {
	public static KeyMapping.Category CATEGORY = new KeyMapping.Category(Reference.modLoc("category"));
	public static KeyMapping KEY_OPEN_HOTBAR_PACK = new KeyMapping(getKey("open_hotbar_pack"), GLFW.GLFW_KEY_X, CATEGORY);
	public static KeyMapping KEY_OPEN_HOTBAR_BELT = new KeyMapping(getKey("open_hotbar_belt"), GLFW.GLFW_KEY_Z, CATEGORY);
	public static KeyMapping KEY_FORCE = new KeyMapping(getKey("force"), GLFW.GLFW_KEY_V, CATEGORY);

	public static KeyMapping.Category QUICK_USE = new KeyMapping.Category(Reference.modLoc("quick_use"));
	public static KeyMapping KEY_QUICK_USE_1 = new KeyMapping(getKey("quick_use_1"), GLFW.GLFW_KEY_KP_1, QUICK_USE);
	public static KeyMapping KEY_QUICK_USE_2 = new KeyMapping(getKey("quick_use_2"), GLFW.GLFW_KEY_KP_2, QUICK_USE);
	public static KeyMapping KEY_QUICK_USE_3 = new KeyMapping(getKey("quick_use_3"), GLFW.GLFW_KEY_KP_3, QUICK_USE);
	public static KeyMapping KEY_QUICK_USE_4 = new KeyMapping(getKey("quick_use_4"), GLFW.GLFW_KEY_KP_4, QUICK_USE);
	public static KeyMapping KEY_QUICK_USE_5 = new KeyMapping(getKey("quick_use_5"), GLFW.GLFW_KEY_KP_5, QUICK_USE);
	public static KeyMapping KEY_QUICK_USE_6 = new KeyMapping(getKey("quick_use_6"), GLFW.GLFW_KEY_KP_6, QUICK_USE);
	public static KeyMapping KEY_QUICK_USE_7 = new KeyMapping(getKey("quick_use_7"), GLFW.GLFW_KEY_KP_7, QUICK_USE);
	public static KeyMapping KEY_QUICK_USE_8 = new KeyMapping(getKey("quick_use_8"), GLFW.GLFW_KEY_KP_8, QUICK_USE);

	private static String getKey(String name) {
		return String.join(".", "key", Reference.MOD_ID, name);
	}

	@SubscribeEvent
	public static void registerKeymapping(final RegisterKeyMappingsEvent event) {
		event.registerCategory(CATEGORY);
		event.register(KEY_OPEN_HOTBAR_PACK);
		event.register(KEY_OPEN_HOTBAR_BELT);
		event.register(KEY_FORCE);

		event.registerCategory(QUICK_USE);
		event.register(KEY_QUICK_USE_1);
		event.register(KEY_QUICK_USE_2);
		event.register(KEY_QUICK_USE_3);
		event.register(KEY_QUICK_USE_4);
		event.register(KEY_QUICK_USE_5);
		event.register(KEY_QUICK_USE_6);
		event.register(KEY_QUICK_USE_7);
		event.register(KEY_QUICK_USE_8);
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		Player player = Minecraft.getInstance().player;
		if (player == null) return;
		ItemStack stack = player.getItemInHand(player.getUsedItemHand());
		if (stack.is(ForceTags.Items.CAN_STORE_FORCE)) {
			if (KEY_FORCE.consumeClick()) {
				if (!stack.has(ForceComponents.FORCE_SLOT)) {
					stack.set(ForceComponents.FORCE_SLOT, ItemContainerContents.EMPTY);
				}

				// Send packet to server-side to open item menu
				ClientPacketDistributor.sendToServer(new OpenForceMenuPayload(player.getUsedItemHand()));
			}
		}
	}
}
