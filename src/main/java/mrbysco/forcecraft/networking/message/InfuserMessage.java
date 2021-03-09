package mrbysco.forcecraft.networking.message;

import io.netty.buffer.ByteBuf;
import mrbysco.forcecraft.container.InfuserContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class InfuserMessage {

    public boolean isButtonPressed = false;
    public int fluidAmount;
    private BlockPos pos;

    private InfuserMessage(ByteBuf buf) {
        isButtonPressed = buf.readBoolean();
        fluidAmount = buf.readInt();
    }
    public InfuserMessage(boolean buttonPressed, int fluidAmount){
        this.isButtonPressed = buttonPressed;
        this.fluidAmount = fluidAmount;
    }

    public InfuserMessage(boolean buttonPressed){
        this.isButtonPressed = buttonPressed;
    }

    public InfuserMessage(int fluidAmount){
        this.fluidAmount = fluidAmount;
    }

    public void encode(ByteBuf buf) {
        buf.writeBoolean(isButtonPressed);
        buf.writeInt(fluidAmount);
    }

    public static InfuserMessage decode(final ByteBuf packetBuffer) {
        return new InfuserMessage(packetBuffer.readBoolean(), packetBuffer.readInt());
    }

    public void handle(Supplier<Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            if (ctx.getDirection().getReceptionSide().isServer() && ctx.getSender() != null) {
                Container container = ctx.getSender().openContainer;
                if(isButtonPressed){
                    if(container instanceof InfuserContainer){
                        ((InfuserContainer) container).setButtonPressed(true);
                    }
                }
                if(container instanceof InfuserContainer){
                    this.fluidAmount = ((InfuserContainer) container).getFluidAmount();
                    ((InfuserContainer) container).setFluidAmount(fluidAmount);
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}
