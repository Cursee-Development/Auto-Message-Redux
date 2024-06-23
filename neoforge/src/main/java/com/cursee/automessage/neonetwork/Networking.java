package com.cursee.automessage.neonetwork;

import com.cursee.automessage.Constants;
import com.cursee.automessage.neonetwork.messages.MessageHandler;
import com.cursee.automessage.neonetwork.messages.MessageSyncAutoMessageSettings;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
//import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
//import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public class Networking {
	
	public static void register(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(Constants.MOD_ID);
		
//		registrar.playBidirectional(MessageSyncAutoMessageSettings.ID, MessageSyncAutoMessageSettings::new, MessageHandler::handle);
		registrar.playBidirectional(MessageSyncAutoMessageSettings.TYPE, MessageSyncAutoMessageSettings.CODEC, MessageHandler::handle2);
	}
	
	public static <T extends IMessage> void sendTo(ServerPlayer player, T message) {
		PacketDistributor.sendToPlayer(player, message);
//		PacketDistributor.PLAYER.with(player).send(message);
	}
}
