package com.mrbysco.forcecraft.networking.message;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.items.ForcePackItem;
import com.mrbysco.forcecraft.menu.ForcePackMenu;
import com.mrbysco.forcecraft.storage.PackStorage;
import com.mrbysco.forcecraft.storage.StorageManager;
import com.mrbysco.forcecraft.util.FindingUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.NetworkHooks;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class OpenPackMessage {
	public OpenPackMessage() {

	}

	public void encode(FriendlyByteBuf buf) {

	}

	public static OpenPackMessage decode(final FriendlyByteBuf packetBuffer) {
		return new OpenPackMessage();
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer() && ctx.getSender() != null) {
				ServerPlayer player = ctx.getSender();
				Predicate<ItemStack> stackPredicate = (stack) -> stack.getItem() instanceof ForcePackItem;
				if (FindingUtil.hasSingleStackInHotbar(player, stackPredicate)) {
					ItemStack packStack = FindingUtil.findInstanceStack(player, stackPredicate);
					if (!packStack.isEmpty()) {
						Optional<PackStorage> data = StorageManager.getPack(packStack);
						data.ifPresent(pack ->
								NetworkHooks.openScreen(player, new SimpleMenuProvider((id, pInv, pEntity) -> new ForcePackMenu(id, pInv, pack.getInventory()),
										packStack.hasCustomHoverName() ? ((MutableComponent) packStack.getHoverName()).withStyle(ChatFormatting.BLACK) :
												Component.translatable(Reference.MOD_ID + ".container.pack")), buf -> buf.writeInt(pack.getInventory().getUpgrades())));
					}
				}
			}
		});
		ctx.setPacketHandled(true);
	}
}
