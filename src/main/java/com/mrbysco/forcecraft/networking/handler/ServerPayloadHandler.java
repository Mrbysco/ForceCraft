package com.mrbysco.forcecraft.networking.handler;

import com.mrbysco.forcecraft.item.tool.EnderData;
import com.mrbysco.forcecraft.networking.message.ChangeEnderPosPayload;
import com.mrbysco.forcecraft.networking.message.PackChangePayload;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {
	public static final ServerPayloadHandler INSTANCE = new ServerPayloadHandler();

	public static ServerPayloadHandler getInstance() {
		return INSTANCE;
	}

	public void handleEnderPos(final ChangeEnderPosPayload changeData, final IPayloadContext context) {
		context.enqueueWork(() -> {
					if (context.player() instanceof ServerPlayer player) {
						ItemStack stack = player.getItemInHand(changeData.hand());

						if (stack.is(ForceRegistry.FORCE_ROD) && stack.has(ForceComponents.ENDER)) {
							stack.set(ForceComponents.ENDER_DATA, new EnderData(changeData.pos(), changeData.customName()));
						}
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.disconnect(Component.translatable("forcecraft.networking.ender_pos_change.failed", e.getMessage()));
					return null;
				});
	}

	public void handlePackChange(final PackChangePayload changeData, final IPayloadContext context) {
		context.enqueueWork(() -> {
					if (context.player() instanceof ServerPlayer player) {
						ItemStack stack = player.getItemInHand(changeData.hand());

						if (stack.getItem() == ForceRegistry.FORCE_PACK.get() || stack.getItem() == ForceRegistry.FORCE_BELT.get()) {
							stack.set(ForceComponents.PACK_COLOR, changeData.color());

							String customName = changeData.customName();
							if (customName.isEmpty()) {
								stack.remove(DataComponents.CUSTOM_NAME);
							} else {
								if (!stack.getHoverName().getString().equals(customName)) {
									stack.set(DataComponents.CUSTOM_NAME, Component.literal(customName).withStyle(ChatFormatting.YELLOW));
								}
							}
						}
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.disconnect(Component.translatable("forcecraft.networking.pack_change.failed", e.getMessage()));
					return null;
				});
	}
}
