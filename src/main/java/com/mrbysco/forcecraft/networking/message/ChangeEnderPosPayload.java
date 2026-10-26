package com.mrbysco.forcecraft.networking.message;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;

public record ChangeEnderPosPayload(InteractionHand hand, GlobalPos pos, String customName) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, ChangeEnderPosPayload> CODEC = StreamCodec.composite(
			InteractionHand.STREAM_CODEC,
			o -> o.hand,
			GlobalPos.STREAM_CODEC,
			o -> o.pos,
			ByteBufCodecs.STRING_UTF8,
			o -> o.customName,
			ChangeEnderPosPayload::new);
	public static final Type<ChangeEnderPosPayload> ID = new Type<>(Reference.modLoc("set_ender_coords"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
