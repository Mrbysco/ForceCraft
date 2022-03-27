package com.mrbysco.forcecraft.networking.message;

import com.mrbysco.forcecraft.menu.infuser.InfuserMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class InfuserMessage {

	public boolean isButtonPressed;

	public InfuserMessage(boolean buttonPressed) {
		this.isButtonPressed = buttonPressed;
	}

	public void encode(ByteBuf buf) {
		buf.writeBoolean(isButtonPressed);
	}

	public static InfuserMessage decode(final ByteBuf packetBuffer) {
		return new InfuserMessage(packetBuffer.readBoolean());
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer() && ctx.getSender() != null) {
				AbstractContainerMenu container = ctx.getSender().containerMenu;
				if (container instanceof InfuserMenu ctr) {
					if (isButtonPressed) {
						ctr.getTile().startWork();
					}
				}
			}
		});
		ctx.setPacketHandled(true);
	}
}
