package com.mrbysco.forcecraft.networking.message;

import com.mrbysco.forcecraft.items.ForceBeltItem;
import com.mrbysco.forcecraft.util.FindingUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class QuickUseBeltMessage {
	public int slot;

	public QuickUseBeltMessage(int slot) {
		this.slot = slot;
	}

	public void encode(PacketBuffer buf) {
		buf.writeInt(slot);
	}

	public static QuickUseBeltMessage decode(final PacketBuffer packetBuffer) {
		return new QuickUseBeltMessage(packetBuffer.readInt());
	}

	public void handle(Supplier<Context> context) {
		Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer() && ctx.getSender() != null) {
				ServerPlayerEntity player = ctx.getSender();
				Predicate<ItemStack> stackPredicate = (stack) -> stack.getItem() instanceof ForceBeltItem;
				if (FindingUtil.hasSingleStackInHotbar(player, stackPredicate)) {
					ItemStack beltStack = FindingUtil.findInstanceStack(player, stackPredicate);
					if (!beltStack.isEmpty()) {
						beltStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent((handler) -> {
							ItemStack stack = handler.getStackInSlot(slot);
							World world = player.level;
							if (!stack.isEmpty()) {
								stack.finishUsingItem(world, player);
								world.playSound((PlayerEntity) null, player.getX(), player.getY(), player.getZ(), stack.getDrinkingSound(), player.getSoundSource(), 0.5F, player.level.random.nextFloat() * 0.1F + 0.9F);
							}
						});
					}
				}
			}
		});
		ctx.setPacketHandled(true);
	}
}
