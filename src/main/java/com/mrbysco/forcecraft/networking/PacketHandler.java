package com.mrbysco.forcecraft.networking;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.networking.handler.ClientPayloadHandler;
import com.mrbysco.forcecraft.networking.handler.ServerPayloadHandler;
import com.mrbysco.forcecraft.networking.message.OpenInventoryPayload;
import com.mrbysco.forcecraft.networking.message.PackChangePayload;
import com.mrbysco.forcecraft.networking.message.QuickUseBeltPayload;
import com.mrbysco.forcecraft.networking.message.RecipeToCardPayload;
import com.mrbysco.forcecraft.networking.message.SaveCardRecipePayload;
import com.mrbysco.forcecraft.networking.message.StopInfuserSoundPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public class PacketHandler {

	public static void setupPackets(final RegisterPayloadHandlerEvent event) {
		final IPayloadRegistrar registrar = event.registrar(Reference.MOD_ID);

		registrar.play(StopInfuserSoundPayload.ID, StopInfuserSoundPayload::new, handler -> handler
				.client(ClientPayloadHandler.getInstance()::handleStopData));

		registrar.play(OpenInventoryPayload.ID, OpenInventoryPayload::new, handler -> handler
				.server(ServerPayloadHandler.getInstance()::handleOpen));
		registrar.play(QuickUseBeltPayload.ID, QuickUseBeltPayload::new, handler -> handler
				.server(ServerPayloadHandler.getInstance()::handleQuickUse));
		registrar.play(PackChangePayload.ID, PackChangePayload::new, handler -> handler
				.server(ServerPayloadHandler.getInstance()::handlePackChange));
		registrar.play(RecipeToCardPayload.ID, RecipeToCardPayload::new, handler -> handler
				.server(ServerPayloadHandler.getInstance()::handleCard));
		registrar.play(SaveCardRecipePayload.ID, SaveCardRecipePayload::new, handler -> handler
				.server(ServerPayloadHandler.getInstance()::handleSaveCard));
	}
}
