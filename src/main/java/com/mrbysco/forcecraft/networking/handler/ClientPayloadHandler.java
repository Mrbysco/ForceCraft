package com.mrbysco.forcecraft.networking.handler;

import com.mrbysco.forcecraft.networking.message.StopInfuserSoundPayload;
import com.mrbysco.forcecraft.registry.ForceSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class ClientPayloadHandler {
	private static final ClientPayloadHandler INSTANCE = new ClientPayloadHandler();

	public static ClientPayloadHandler getInstance() {
		return INSTANCE;
	}

	public void handleStopData(final StopInfuserSoundPayload data, final PlayPayloadContext context) {
		context.workHandler().submitAsync(() -> {
					net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
					mc.getSoundManager().stop(ForceSounds.INFUSER_WORKING.getId(), SoundSource.BLOCKS);
					mc.getSoundManager().stop(ForceSounds.INFUSER_SPECIAL.getId(), SoundSource.BLOCKS);
				})
				.exceptionally(e -> {
					// Handle exception
					context.packetHandler().disconnect(Component.translatable("forcecraft.networking.stop_infuser_sound.failed", e.getMessage()));
					return null;
				});
	}
}
