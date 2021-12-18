package com.mrbysco.forcecraft.networking.message;

import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class PackChangeMessage {
	public InteractionHand hand;
	public String customName;
	public int color;

	public PackChangeMessage(InteractionHand hand, String customName, int color){
		this.hand = hand;
		this.customName = customName;
		this.color = color;
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(hand == InteractionHand.MAIN_HAND ? 0 : 1);
		buf.writeUtf(customName);
		buf.writeInt(color);
	}

	public static PackChangeMessage decode(final FriendlyByteBuf packetBuffer) {
		return new PackChangeMessage(packetBuffer.readInt() == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, packetBuffer.readUtf(32767), packetBuffer.readInt());
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer() && ctx.getSender() != null) {
				ItemStack stack = ctx.getSender().getItemInHand(hand);

				if(stack.getItem() == ForceRegistry.FORCE_PACK.get() || stack.getItem() == ForceRegistry.FORCE_BELT.get()) {
					CompoundTag tag = stack.getOrCreateTag();
					tag.putInt("Color", color);
					stack.setTag(tag);

					if(customName.isEmpty()) {
						stack.resetHoverName();
					} else {
						if(!stack.getHoverName().getContents().equals(customName)) {
							stack.setHoverName(new TextComponent(customName).withStyle(ChatFormatting.YELLOW));
						}
					}
				}
			}
		});
		ctx.setPacketHandled(true);
	}
}
