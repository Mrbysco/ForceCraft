package com.mrbysco.forcecraft.networking.message;

import com.mrbysco.forcecraft.container.ItemCardContainer;
import com.mrbysco.forcecraft.items.ItemCardItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.Optional;
import java.util.function.Supplier;

public class SaveCardRecipeMessage {
	public SaveCardRecipeMessage(){

	}

	public void encode(PacketBuffer buf) {

	}

	public static SaveCardRecipeMessage decode(final PacketBuffer packetBuffer) {
		return new SaveCardRecipeMessage();
	}

	public void handle(Supplier<Context> context) {
		Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer() && ctx.getSender() != null) {
				ServerPlayerEntity player = ctx.getSender();
				World world = player.level;
				ItemStack stack = getCardStack(player);
				if (!stack.isEmpty()) {
					if (player.containerMenu instanceof ItemCardContainer) {
						ItemCardContainer itemCardContainer = (ItemCardContainer) player.containerMenu;
						CraftingInventory craftMatrix = itemCardContainer.getCraftMatrix();
						CraftResultInventory craftResult = itemCardContainer.getCraftResult();
						Optional<ICraftingRecipe> iRecipe = player.server.getRecipeManager().getRecipeFor(IRecipeType.CRAFTING, craftMatrix, world);
						iRecipe.ifPresent((recipe) -> {
							CompoundNBT nbt = stack.getOrCreateTag();
							CompoundNBT recipeContents = new CompoundNBT();
							for(int i = 0; i < craftMatrix.getContainerSize(); i++) {
								recipeContents.put("slot_" + i, craftMatrix.getItem(i).save(new CompoundNBT()));
							}
							recipeContents.put("result", craftResult.getItem(0).save(new CompoundNBT()));
							nbt.put("RecipeContents", recipeContents);
							stack.setTag(nbt);
						});
					}
				}
				player.sendMessage(new StringTextComponent("Recipe saved").withStyle(TextFormatting.YELLOW), Util.NIL_UUID);
			}
		});
		ctx.setPacketHandled(true);
	}

	private static ItemStack getCardStack(PlayerEntity player) {
		if(player.getMainHandItem().getItem() instanceof ItemCardItem) {
			return player.getMainHandItem();
		} else if(player.getOffhandItem().getItem() instanceof ItemCardItem) {
			return player.getOffhandItem();
		}
		return ItemStack.EMPTY;
	}
}
