package mrbysco.forcecraft.networking;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.networking.message.InfuserMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Reference.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int id = 0;

    public static void init(){
        CHANNEL.registerMessage(0, InfuserMessage.class, InfuserMessage::encode, InfuserMessage::decode, InfuserMessage::handle);
    }

    public static void sendPacket(Entity player, IPacket<?> packet) {
        if(player instanceof ServerPlayerEntity && ((ServerPlayerEntity) player).connection != null) {
            ((ServerPlayerEntity) player).connection.sendPacket(packet);
        }
    }
}
