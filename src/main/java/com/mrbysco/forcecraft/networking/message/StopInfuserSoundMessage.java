package com.mrbysco.forcecraft.networking.message;

import com.mrbysco.forcecraft.registry.ForceSounds;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class StopInfuserSoundMessage {
	public StopInfuserSoundMessage(){

	}

	public void encode(PacketBuffer buf) {

	}

	public static StopInfuserSoundMessage decode(final PacketBuffer packetBuffer) {
		return new StopInfuserSoundMessage();
	}

	public void handle(Supplier<Context> context) {
		Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isClient()) {
				net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
				mc.getSoundManager().stop(ForceSounds.INFUSER_WORKING.getId(), SoundCategory.BLOCKS);
				mc.getSoundManager().stop(ForceSounds.INFUSER_SPECIAL.getId(), SoundCategory.BLOCKS);
			}
		});
		ctx.setPacketHandled(true);
	}
}
