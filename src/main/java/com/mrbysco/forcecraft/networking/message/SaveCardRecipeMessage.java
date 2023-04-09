package com.mrbysco.forcecraft.networking.message;

import com.mrbysco.forcecraft.items.ItemCardItem;
import com.mrbysco.forcecraft.menu.ItemCardMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.Optional;
import java.util.function.Supplier;

public class SaveCardRecipeMessage {
	public SaveCardRecipeMessage() {

	}

	public void encode(FriendlyByteBuf buf) {

	}

	public static SaveCardRecipeMessage decode(final FriendlyByteBuf packetBuffer) {
		return new SaveCardRecipeMessage();
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer() && ctx.getSender() != null) {
				ServerPlayer player = ctx.getSender();
				Level level = player.level;
				ItemStack stack = getCardStack(player);
				if (!stack.isEmpty()) {
					if (player.containerMenu instanceof ItemCardMenu itemCardContainer) {
						CraftingContainer craftMatrix = itemCardContainer.getCraftMatrix();
						ResultContainer craftResult = itemCardContainer.getCraftResult();
						Optional<CraftingRecipe> iRecipe = player.server.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftMatrix, level);
						iRecipe.ifPresent((recipe) -> {
							CompoundTag nbt = stack.getOrCreateTag();
							CompoundTag recipeContents = new CompoundTag();
							for (int i = 0; i < craftMatrix.getContainerSize(); i++) {
								recipeContents.put("slot_" + i, craftMatrix.getItem(i).save(new CompoundTag()));
							}
							recipeContents.put("result", craftResult.getItem(0).save(new CompoundTag()));
							nbt.put("RecipeContents", recipeContents);
							stack.setTag(nbt);
						});
					}
				}
				player.sendSystemMessage(Component.literal("Recipe saved").withStyle(ChatFormatting.YELLOW));
			}
		});
		ctx.setPacketHandled(true);
	}

	private static ItemStack getCardStack(Player player) {
		if (player.getMainHandItem().getItem() instanceof ItemCardItem) {
			return player.getMainHandItem();
		} else if (player.getOffhandItem().getItem() instanceof ItemCardItem) {
			return player.getOffhandItem();
		}
		return ItemStack.EMPTY;
	}
}
