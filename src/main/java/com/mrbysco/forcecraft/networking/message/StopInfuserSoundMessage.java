package com.mrbysco.forcecraft.networking.message;

import com.mrbysco.forcecraft.registry.ForceSounds;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class StopInfuserSoundMessage {
	public StopInfuserSoundMessage(){

	}

	public void encode(FriendlyByteBuf buf) {

	}

	public static StopInfuserSoundMessage decode(final FriendlyByteBuf packetBuffer) {
		return new StopInfuserSoundMessage();
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isClient()) {
				net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
				mc.getSoundManager().stop(ForceSounds.INFUSER_WORKING.getId(), SoundSource.BLOCKS);
				mc.getSoundManager().stop(ForceSounds.INFUSER_SPECIAL.getId(), SoundSource.BLOCKS);
			}
		});
		ctx.setPacketHandled(true);
	}
}
