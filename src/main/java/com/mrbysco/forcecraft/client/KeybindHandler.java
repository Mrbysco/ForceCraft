package com.mrbysco.forcecraft.client;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.networking.PacketHandler;
import com.mrbysco.forcecraft.networking.message.OpenBeltMessage;
import com.mrbysco.forcecraft.networking.message.OpenPackMessage;
import com.mrbysco.forcecraft.networking.message.QuickUseBeltMessage;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.event.TickEvent;
import org.lwjgl.glfw.GLFW;


public class KeybindHandler {
	public static KeyBinding KEY_OPEN_HOTBAR_PACK = new KeyBinding(getKey("open_hotbar_pack"), GLFW.GLFW_KEY_X, getKey("category"));
	public static KeyBinding KEY_OPEN_HOTBAR_BELT = new KeyBinding(getKey("open_hotbar_belt"), GLFW.GLFW_KEY_Z, getKey("category"));

	public static KeyBinding KEY_QUICK_USE_1 = new KeyBinding(getKey("quick_use_1"), GLFW.GLFW_KEY_KP_1, getKey("quick_use"));
	public static KeyBinding KEY_QUICK_USE_2 = new KeyBinding(getKey("quick_use_2"), GLFW.GLFW_KEY_KP_2, getKey("quick_use"));
	public static KeyBinding KEY_QUICK_USE_3 = new KeyBinding(getKey("quick_use_3"), GLFW.GLFW_KEY_KP_3, getKey("quick_use"));
	public static KeyBinding KEY_QUICK_USE_4 = new KeyBinding(getKey("quick_use_4"), GLFW.GLFW_KEY_KP_4, getKey("quick_use"));
	public static KeyBinding KEY_QUICK_USE_5 = new KeyBinding(getKey("quick_use_5"), GLFW.GLFW_KEY_KP_5, getKey("quick_use"));
	public static KeyBinding KEY_QUICK_USE_6 = new KeyBinding(getKey("quick_use_6"), GLFW.GLFW_KEY_KP_6, getKey("quick_use"));
	public static KeyBinding KEY_QUICK_USE_7 = new KeyBinding(getKey("quick_use_7"), GLFW.GLFW_KEY_KP_7, getKey("quick_use"));
	public static KeyBinding KEY_QUICK_USE_8 = new KeyBinding(getKey("quick_use_8"), GLFW.GLFW_KEY_KP_8, getKey("quick_use"));

	private static String getKey(String name) {
		return String.join(".", "key", Reference.MOD_ID, name);
	}

	public static void onClientTick(TickEvent.ClientTickEvent event) {
		if (KEY_OPEN_HOTBAR_PACK.isPressed()) {
			PacketHandler.CHANNEL.sendToServer(new OpenPackMessage());
		}

		if (KEY_OPEN_HOTBAR_BELT.isPressed()) {
			PacketHandler.CHANNEL.sendToServer(new OpenBeltMessage());
		}

		if (KEY_QUICK_USE_1.isPressed()) {
			PacketHandler.CHANNEL.sendToServer(new QuickUseBeltMessage(0));
		}
		if (KEY_QUICK_USE_2.isPressed()) {
			PacketHandler.CHANNEL.sendToServer(new QuickUseBeltMessage(1));
		}
		if (KEY_QUICK_USE_3.isPressed()) {
			PacketHandler.CHANNEL.sendToServer(new QuickUseBeltMessage(2));
		}
		if (KEY_QUICK_USE_4.isPressed()) {
			PacketHandler.CHANNEL.sendToServer(new QuickUseBeltMessage(3));
		}
		if (KEY_QUICK_USE_5.isPressed()) {
			PacketHandler.CHANNEL.sendToServer(new QuickUseBeltMessage(4));
		}
		if (KEY_QUICK_USE_6.isPressed()) {
			PacketHandler.CHANNEL.sendToServer(new QuickUseBeltMessage(5));
		}
		if (KEY_QUICK_USE_7.isPressed()) {
			PacketHandler.CHANNEL.sendToServer(new QuickUseBeltMessage(6));
		}
		if (KEY_QUICK_USE_8.isPressed()) {
			PacketHandler.CHANNEL.sendToServer(new QuickUseBeltMessage(7));
		}
	}
}
