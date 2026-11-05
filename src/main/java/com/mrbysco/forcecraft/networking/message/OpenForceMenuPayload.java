package com.mrbysco.forcecraft.networking.message;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;

public record OpenForceMenuPayload(InteractionHand hand) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, OpenForceMenuPayload> CODEC = StreamCodec.composite(
			InteractionHand.STREAM_CODEC,
			o -> o.hand,
			OpenForceMenuPayload::new);
	public static final Type<OpenForceMenuPayload> ID = new Type<>(Reference.modLoc("open_force_menu"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}