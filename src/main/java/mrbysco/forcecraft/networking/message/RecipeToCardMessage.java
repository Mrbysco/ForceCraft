package mrbysco.forcecraft.networking.message;

import mrbysco.forcecraft.container.ItemCardContainer;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RecipeToCardMessage {
	private List<ItemStack> stacks;

	public RecipeToCardMessage(List<ItemStack> stacks){
		this.stacks = stacks;
	}

	public void encode(PacketBuffer buf) {
		buf.writeInt(stacks.size());
		for (ItemStack output : stacks) {
			buf.writeItemStack(output);
		}
	}

	public static RecipeToCardMessage decode(final PacketBuffer packetBuffer) {
		int size = packetBuffer.readInt();
		List<ItemStack> outputs = new ArrayList<>(size);
		for (int i = 0 ; i < size ; i++) {
			outputs.add(packetBuffer.readItemStack());
		}

		return new RecipeToCardMessage(outputs);
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			PlayerEntity player = ctx.getSender();
			// Handle tablet version
			ItemStack mainhand = player.getHeldItemMainhand();
			if (!mainhand.isEmpty() && mainhand.getItem() == ForceRegistry.ITEM_CARD.get()) {
				if (player.openContainer instanceof ItemCardContainer) {
					ItemCardContainer itemCardContainer = (ItemCardContainer) player.openContainer;
					itemCardContainer.setMatrixContents(player, stacks);
				}
			}
		});
		ctx.setPacketHandled(true);
	}
}
