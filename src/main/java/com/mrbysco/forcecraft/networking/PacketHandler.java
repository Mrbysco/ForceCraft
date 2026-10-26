package com.mrbysco.forcecraft.networking;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.networking.handler.ServerPayloadHandler;
import com.mrbysco.forcecraft.networking.message.ChangeEnderPosPayload;
import com.mrbysco.forcecraft.networking.message.PackChangePayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber
public class PacketHandler {

	@SubscribeEvent
	public static void setupPackets(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(Reference.MOD_ID);

		registrar.playToServer(ChangeEnderPosPayload.ID, ChangeEnderPosPayload.CODEC, ServerPayloadHandler.getInstance()::handleEnderPos);
		registrar.playToServer(PackChangePayload.ID, PackChangePayload.CODEC, ServerPayloadHandler.getInstance()::handlePackChange);
	}
}
