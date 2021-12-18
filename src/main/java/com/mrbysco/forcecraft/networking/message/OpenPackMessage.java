package com.mrbysco.forcecraft.networking.message;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.container.ForcePackContainer;
import com.mrbysco.forcecraft.items.ForcePackItem;
import com.mrbysco.forcecraft.util.FindingUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;

import javax.annotation.Nullable;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class OpenPackMessage {
	public OpenPackMessage(){

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
					ItemStack beltStack = FindingUtil.findInstanceStack(player, stackPredicate);
					if(!beltStack.isEmpty()) {
						player.openMenu(new MenuProvider() {
							@Override
							public Component getDisplayName() {
								return beltStack.hasCustomHoverName() ? ((BaseComponent)beltStack.getHoverName()).withStyle(ChatFormatting.BLACK) : new TranslatableComponent(Reference.MOD_ID + ".container.pack");
							}

							@Nullable
							@Override
							public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
								return new ForcePackContainer(id, inventory);
							}
						});
					}
				}
			}
		});
		ctx.setPacketHandled(true);
	}
}
