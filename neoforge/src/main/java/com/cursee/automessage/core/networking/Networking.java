package com.cursee.automessage.core.networking;

//import net.jason13.automessage.CommonConstants;
//import net.jason13.automessage.neonetwork.messages.MessageHandler;
//import net.jason13.automessage.neonetwork.messages.MessageSyncAutoMessageSettings;
import com.cursee.automessage.Constants;
import com.cursee.automessage.core.networking.messages.MessageHandler;
import com.cursee.automessage.core.networking.messages.MessageSyncAutoMessageSettings;
import com.cursee.automessage.core.networking.payloads.NeoForgeInitialSyncPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
//import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
//import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public class Networking {

	public static void register(RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(Constants.MOD_ID);
		registrar.playBidirectional(NeoForgeInitialSyncPayload.ID, NeoForgeInitialSyncPayload.CODEC, NeoForgeInitialSyncPayload::handle);
	}

	public static void sendTo(ServerPlayer player, MessageSyncAutoMessageSettings messageSyncAutoMessageSettings) {
	}

//	public static void register(final RegisterPayloadHandlerEvent event) {
//		final IPayloadRegistrar registrar = event.registrar(Constants.MOD_ID);
//
//		registrar.play(MessageSyncAutoMessageSettings.ID, MessageSyncAutoMessageSettings::new, MessageHandler::handle);
//	}
//
//	public static <T extends IMessage> void sendTo(ServerPlayer player, T message) {
//		PacketDistributor.PLAYER.with(player).send(message);
//	}
}
