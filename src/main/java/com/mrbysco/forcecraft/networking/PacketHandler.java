package com.mrbysco.forcecraft.networking;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.networking.message.InfuserMessage;
import com.mrbysco.forcecraft.networking.message.OpenBeltMessage;
import com.mrbysco.forcecraft.networking.message.OpenPackMessage;
import com.mrbysco.forcecraft.networking.message.PackChangeMessage;
import com.mrbysco.forcecraft.networking.message.QuickUseBeltMessage;
import com.mrbysco.forcecraft.networking.message.RecipeToCardMessage;
import com.mrbysco.forcecraft.networking.message.SaveCardRecipeMessage;
import com.mrbysco.forcecraft.networking.message.StopInfuserSoundMessage;
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

	public static void init() {
		CHANNEL.registerMessage(id++, InfuserMessage.class, InfuserMessage::encode, InfuserMessage::decode, InfuserMessage::handle);
		CHANNEL.registerMessage(id++, PackChangeMessage.class, PackChangeMessage::encode, PackChangeMessage::decode, PackChangeMessage::handle);
		CHANNEL.registerMessage(id++, RecipeToCardMessage.class, RecipeToCardMessage::encode, RecipeToCardMessage::decode, RecipeToCardMessage::handle);
		CHANNEL.registerMessage(id++, SaveCardRecipeMessage.class, SaveCardRecipeMessage::encode, SaveCardRecipeMessage::decode, SaveCardRecipeMessage::handle);
		CHANNEL.registerMessage(id++, OpenBeltMessage.class, OpenBeltMessage::encode, OpenBeltMessage::decode, OpenBeltMessage::handle);
		CHANNEL.registerMessage(id++, OpenPackMessage.class, OpenPackMessage::encode, OpenPackMessage::decode, OpenPackMessage::handle);
		CHANNEL.registerMessage(id++, QuickUseBeltMessage.class, QuickUseBeltMessage::encode, QuickUseBeltMessage::decode, QuickUseBeltMessage::handle);
		CHANNEL.registerMessage(id++, StopInfuserSoundMessage.class, StopInfuserSoundMessage::encode, StopInfuserSoundMessage::decode, StopInfuserSoundMessage::handle);
	}

	public static void sendPacket(Entity player, IPacket<?> packet) {
		if (player instanceof ServerPlayerEntity && ((ServerPlayerEntity) player).connection != null) {
			((ServerPlayerEntity) player).connection.send(packet);
		}
	}
}
