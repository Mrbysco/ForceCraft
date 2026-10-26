package com.mrbysco.forcecraft.networking.message;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.DyeColor;

public record PackChangePayload(InteractionHand hand, String customName,
                                DyeColor color) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, PackChangePayload> CODEC = StreamCodec.composite(
			InteractionHand.STREAM_CODEC,
			o -> o.hand,
			ByteBufCodecs.STRING_UTF8,
			o -> o.customName,
			DyeColor.STREAM_CODEC,
			o -> o.color,
			PackChangePayload::new);
	public static final Type<PackChangePayload> ID = new Type<>(Reference.modLoc("pack_change"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
