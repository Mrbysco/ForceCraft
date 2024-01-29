package com.mrbysco.forcecraft.networking.message;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record RecipeToCardPayload(List<ItemStack> stacks) implements CustomPacketPayload {
	public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "recipe_to_card");

	public RecipeToCardPayload(final FriendlyByteBuf packetBuffer) {
		this(new ArrayList<>());
		int size = packetBuffer.readInt();
		for (int i = 0; i < size; i++) {
			stacks.add(packetBuffer.readItem());
		}
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeInt(stacks.size());
		for (ItemStack output : stacks) {
			buf.writeItem(output);
		}
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}
}
