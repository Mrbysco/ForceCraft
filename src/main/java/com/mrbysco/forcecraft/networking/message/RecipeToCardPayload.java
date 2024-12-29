package com.mrbysco.forcecraft.networking.message;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record RecipeToCardPayload(List<ItemStack> stacks) implements CustomPacketPayload {
	public static final StreamCodec<RegistryFriendlyByteBuf, RecipeToCardPayload> CODEC = CustomPacketPayload.codec(
			RecipeToCardPayload::write,
			RecipeToCardPayload::new);
	public static final Type<RecipeToCardPayload> ID = new Type<>(Reference.modLoc("recipe_to_card"));

	public RecipeToCardPayload(final RegistryFriendlyByteBuf packetBuffer) {
		this(new ArrayList<>());
		int size = packetBuffer.readInt();
		for (int i = 0; i < size; i++) {
			stacks.add(ItemStack.OPTIONAL_STREAM_CODEC.decode(packetBuffer));
		}
	}

	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeInt(stacks.size());
		for (ItemStack output : stacks) {
			ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, output);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
