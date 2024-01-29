package com.mrbysco.forcecraft.client;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.networking.message.OpenInventoryPayload;
import com.mrbysco.forcecraft.networking.message.QuickUseBeltPayload;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;


public class KeybindHandler {
	public static KeyMapping KEY_OPEN_HOTBAR_PACK = new KeyMapping(getKey("open_hotbar_pack"), GLFW.GLFW_KEY_X, getKey("category"));
	public static KeyMapping KEY_OPEN_HOTBAR_BELT = new KeyMapping(getKey("open_hotbar_belt"), GLFW.GLFW_KEY_Z, getKey("category"));

	public static KeyMapping KEY_QUICK_USE_1 = new KeyMapping(getKey("quick_use_1"), GLFW.GLFW_KEY_KP_1, getKey("quick_use"));
	public static KeyMapping KEY_QUICK_USE_2 = new KeyMapping(getKey("quick_use_2"), GLFW.GLFW_KEY_KP_2, getKey("quick_use"));
	public static KeyMapping KEY_QUICK_USE_3 = new KeyMapping(getKey("quick_use_3"), GLFW.GLFW_KEY_KP_3, getKey("quick_use"));
	public static KeyMapping KEY_QUICK_USE_4 = new KeyMapping(getKey("quick_use_4"), GLFW.GLFW_KEY_KP_4, getKey("quick_use"));
	public static KeyMapping KEY_QUICK_USE_5 = new KeyMapping(getKey("quick_use_5"), GLFW.GLFW_KEY_KP_5, getKey("quick_use"));
	public static KeyMapping KEY_QUICK_USE_6 = new KeyMapping(getKey("quick_use_6"), GLFW.GLFW_KEY_KP_6, getKey("quick_use"));
	public static KeyMapping KEY_QUICK_USE_7 = new KeyMapping(getKey("quick_use_7"), GLFW.GLFW_KEY_KP_7, getKey("quick_use"));
	public static KeyMapping KEY_QUICK_USE_8 = new KeyMapping(getKey("quick_use_8"), GLFW.GLFW_KEY_KP_8, getKey("quick_use"));

	private static String getKey(String name) {
		return String.join(".", "key", Reference.MOD_ID, name);
	}

	public static void onClientTick(TickEvent.ClientTickEvent event) {
		if (KEY_OPEN_HOTBAR_PACK.consumeClick()) {
			PacketDistributor.SERVER.noArg().send(new OpenInventoryPayload(1));
		}

		if (KEY_OPEN_HOTBAR_BELT.consumeClick()) {
			PacketDistributor.SERVER.noArg().send(new OpenInventoryPayload(0));
		}

		if (KEY_QUICK_USE_1.consumeClick()) {
			PacketDistributor.SERVER.noArg().send(new QuickUseBeltPayload(0));
		}
		if (KEY_QUICK_USE_2.consumeClick()) {
			PacketDistributor.SERVER.noArg().send(new QuickUseBeltPayload(1));
		}
		if (KEY_QUICK_USE_3.consumeClick()) {
			PacketDistributor.SERVER.noArg().send(new QuickUseBeltPayload(2));
		}
		if (KEY_QUICK_USE_4.consumeClick()) {
			PacketDistributor.SERVER.noArg().send(new QuickUseBeltPayload(3));
		}
		if (KEY_QUICK_USE_5.consumeClick()) {
			PacketDistributor.SERVER.noArg().send(new QuickUseBeltPayload(4));
		}
		if (KEY_QUICK_USE_6.consumeClick()) {
			PacketDistributor.SERVER.noArg().send(new QuickUseBeltPayload(5));
		}
		if (KEY_QUICK_USE_7.consumeClick()) {
			PacketDistributor.SERVER.noArg().send(new QuickUseBeltPayload(6));
		}
		if (KEY_QUICK_USE_8.consumeClick()) {
			PacketDistributor.SERVER.noArg().send(new QuickUseBeltPayload(7));
		}
	}
}
