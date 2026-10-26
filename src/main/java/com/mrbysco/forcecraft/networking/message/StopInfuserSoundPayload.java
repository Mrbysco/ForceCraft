package com.mrbysco.forcecraft.networking.message;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record StopInfuserSoundPayload() implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, StopInfuserSoundPayload> CODEC = CustomPacketPayload.codec(
			StopInfuserSoundPayload::write,
			StopInfuserSoundPayload::new);
	public static final Type<StopInfuserSoundPayload> ID = new Type<>(Reference.modLoc("stop_infuser_sound"));

	public StopInfuserSoundPayload(final FriendlyByteBuf packetBuffer) {
		this();
	}

	public void write(FriendlyByteBuf buf) {

	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
