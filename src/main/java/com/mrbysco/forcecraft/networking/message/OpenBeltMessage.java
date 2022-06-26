package com.mrbysco.forcecraft.networking.message;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.items.ForceBeltItem;
import com.mrbysco.forcecraft.menu.ForceBeltMenu;
import com.mrbysco.forcecraft.storage.BeltStorage;
import com.mrbysco.forcecraft.storage.StorageManager;
import com.mrbysco.forcecraft.util.FindingUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.NetworkHooks;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class OpenBeltMessage {
	public OpenBeltMessage() {

	}

	public void encode(FriendlyByteBuf buf) {

	}

	public static OpenBeltMessage decode(final FriendlyByteBuf packetBuffer) {
		return new OpenBeltMessage();
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
						data.ifPresent(belt ->
								NetworkHooks.openGui(player, new SimpleMenuProvider((id, pInv, pEntity) -> new ForceBeltMenu(id, pInv, belt.getInventory()),
										beltStack.hasCustomHoverName() ? ((BaseComponent) beltStack.getHoverName()).withStyle(ChatFormatting.BLACK) : new TranslatableComponent(Reference.MOD_ID + ".container.belt"))));
					}
				}
			}
		});
		ctx.setPacketHandled(true);
	}
}
