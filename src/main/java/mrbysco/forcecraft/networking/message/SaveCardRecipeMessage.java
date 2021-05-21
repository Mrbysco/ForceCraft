package mrbysco.forcecraft.networking.message;

import mrbysco.forcecraft.container.ItemCardContainer;
import mrbysco.forcecraft.items.ItemCardItem;
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
				World world = player.world;
				ItemStack stack = getCardStack(player);
				if (!stack.isEmpty()) {
					if (player.openContainer instanceof ItemCardContainer) {
						ItemCardContainer itemCardContainer = (ItemCardContainer) player.openContainer;
						CraftingInventory craftMatrix = itemCardContainer.getCraftMatrix();
						CraftResultInventory craftResult = itemCardContainer.getCraftResult();
						Optional<ICraftingRecipe> iRecipe = player.server.getRecipeManager().getRecipe(IRecipeType.CRAFTING, craftMatrix, world);
						iRecipe.ifPresent((recipe) -> {
							CompoundNBT nbt = stack.getOrCreateTag();
							CompoundNBT recipeContents = new CompoundNBT();
							for(int i = 0; i < craftMatrix.getSizeInventory(); i++) {
								recipeContents.put("slot_" + i, craftMatrix.getStackInSlot(i).write(new CompoundNBT()));
							}
							recipeContents.put("result", craftResult.getStackInSlot(0).write(new CompoundNBT()));
							nbt.put("RecipeContents", recipeContents);
							stack.setTag(nbt);
						});
					}
				}
				player.sendMessage(new StringTextComponent("Recipe saved").mergeStyle(TextFormatting.YELLOW), Util.DUMMY_UUID);
			}
		});
		ctx.setPacketHandled(true);
	}

	private static ItemStack getCardStack(PlayerEntity player) {
		if(player.getHeldItemMainhand().getItem() instanceof ItemCardItem) {
			return player.getHeldItemMainhand();
		} else if(player.getHeldItemOffhand().getItem() instanceof ItemCardItem) {
			return player.getHeldItemOffhand();
		}
		return ItemStack.EMPTY;
	}
}
