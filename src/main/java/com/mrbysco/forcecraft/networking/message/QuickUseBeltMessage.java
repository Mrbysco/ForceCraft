package com.mrbysco.forcecraft.networking.message;

import com.mrbysco.forcecraft.items.ForceBeltItem;
import com.mrbysco.forcecraft.storage.BeltStorage;
import com.mrbysco.forcecraft.storage.StorageManager;
import com.mrbysco.forcecraft.util.FindingUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class QuickUseBeltMessage {
	public int slot;

	public QuickUseBeltMessage(int slot) {
		this.slot = slot;
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(slot);
	}

	public static QuickUseBeltMessage decode(final FriendlyByteBuf packetBuffer) {
		return new QuickUseBeltMessage(packetBuffer.readInt());
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer() && ctx.getSender() != null) {
				ServerPlayer player = ctx.getSender();
				Predicate<ItemStack> stackPredicate = (stack) -> stack.getItem() instanceof ForceBeltItem;
				if (FindingUtil.hasSingleStackInHotbar(player, stackPredicate)) {
					ItemStack beltStack = FindingUtil.findInstanceStack(player, stackPredicate);

					if (!beltStack.isEmpty()) {
						Optional<BeltStorage> data = StorageManager.getBelt(beltStack);
						data.ifPresent(belt -> {
							IItemHandler handler = belt.getInventory();
							ItemStack stack = handler.getStackInSlot(slot);
							Level level = player.level();
							if (!stack.isEmpty()) {
								stack.finishUsingItem(level, player);
								level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), stack.getDrinkingSound(), player.getSoundSource(), 0.5F, player.level().random.nextFloat() * 0.1F + 0.9F);
							}
						});
					}
				}
			}
		});
		ctx.setPacketHandled(true);
	}
}
