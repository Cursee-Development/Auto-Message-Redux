package com.cursee.automessage.neonetwork.messages;

import com.cursee.automessage.neonetwork.IMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
//import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class MessageHandler implements IPayloadHandler<MessageSyncAutoMessageSettings> {
	
//	public static <T extends CustomPacketPayload> void handle(T payload, PlayPayloadContext ctx) {
//		if (ctx.flow().getReceptionSide() == LogicalSide.SERVER) {
//			ctx.workHandler().submitAsync(() -> {
//				handleServer(payload, ctx);
//			});
//		} else {
//			ctx.workHandler().submitAsync(() -> {
//				//separate class to avoid loading client code on server.
//				ClientMessageHandler.handleClient(payload, ctx);
//			});
//		}
//	}
//
//	public static <T extends CustomPacketPayload> void handleServer(T payload, PlayPayloadContext ctx) {
//		MinecraftServer server = ctx.level().get().getServer();
//		if (payload instanceof IMessage message)
//			message.onServerReceived(server, (ServerPlayer) ctx.player().get());
//	}

	@Override
	public void handle(MessageSyncAutoMessageSettings messageSyncAutoMessageSettings, IPayloadContext iPayloadContext) {

		// do nothing lol
	}

	public static void handle2(MessageSyncAutoMessageSettings messageSyncAutoMessageSettings, IPayloadContext iPayloadContext) {
		// do nothing lol2
	}

//	public static class ClientMessageHandler {
//
//		public static <T extends CustomPacketPayload> void handleClient(T payload, PlayPayloadContext ctx) {
//			Minecraft minecraft = Minecraft.getInstance();
//			if (payload instanceof IMessage message)
//				message.onClientReceived(minecraft, minecraft.player);
//		}
//	}
}
