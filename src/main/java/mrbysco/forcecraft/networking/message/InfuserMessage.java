package mrbysco.forcecraft.networking.message;

import io.netty.buffer.ByteBuf;
import mrbysco.forcecraft.blocks.infuser.InfuserContainer;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

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
            Container container = ctx.getSender().openContainer;
            if(container instanceof InfuserContainer) {
                InfuserContainer ctr = (InfuserContainer) container;
                if(isButtonPressed) {
                    ctr.getTile().startWork();
                }
            }
        }
        });
        ctx.setPacketHandled(true);
    }
}
