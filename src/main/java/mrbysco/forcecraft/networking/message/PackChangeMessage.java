package mrbysco.forcecraft.networking.message;

import mrbysco.forcecraft.recipe.ForceRecipes;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class PackChangeMessage {
	public Hand hand;
	public String customName;
	public int color;

	public PackChangeMessage(Hand hand, String customName, int color){
		this.hand = hand;
		this.customName = customName;
		this.color = color;
	}

	public void encode(PacketBuffer buf) {
		buf.writeInt(hand == Hand.MAIN_HAND ? 0 : 1);
		buf.writeString(customName);
		buf.writeInt(color);
	}

	public static PackChangeMessage decode(final PacketBuffer packetBuffer) {
		return new PackChangeMessage(packetBuffer.readInt() == 0 ? Hand.MAIN_HAND : Hand.OFF_HAND, packetBuffer.readString(32767), packetBuffer.readInt());
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer() && ctx.getSender() != null) {
				ItemStack stack = ctx.getSender().getHeldItem(hand);

				if(stack.getItem() == ForceRegistry.FORCE_PACK.get() || stack.getItem() == ForceRegistry.FORCE_BELT.get()) {
					CompoundNBT tag = stack.getOrCreateTag();
					tag.putInt("Color", color);
					stack.setTag(tag);

					if(customName.isEmpty()) {
						stack.clearCustomName();
					} else {
						if(!stack.getDisplayName().getUnformattedComponentText().equals(customName)) {
							stack.setDisplayName(new StringTextComponent(customName).mergeStyle(TextFormatting.YELLOW));
						}
					}
				}
			}
		});
		ctx.setPacketHandled(true);
	}
}
