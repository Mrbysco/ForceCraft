package com.mrbysco.forcecraft.networking.message;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record QuickUseBeltPayload(int slot) implements CustomPacketPayload {
	public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "quick_use_belt");

	public QuickUseBeltPayload(final FriendlyByteBuf packetBuffer) {
		this(packetBuffer.readInt());
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeInt(slot);
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}
}
