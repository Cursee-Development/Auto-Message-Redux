package com.cursee.automessage.core.networking.messages;

import com.cursee.automessage.core.networking.IMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class MessageHandler {
	
	public static <T extends CustomPacketPayload> void handle(T payload, IPayloadContext ctx) {
		if (ctx.flow().getReceptionSide() == LogicalSide.SERVER) {
//			ctx.workHandler().submitAsync(() -> {
//				handleServer(payload, ctx);
//			});
			ctx.enqueueWork(() -> {
				handleServer(payload, ctx);
			});
		} else {
//			ctx.workHandler().submitAsync(() -> {
//				//separate class to avoid loading client code on server.
//				ClientMessageHandler.handleClient(payload, ctx);
//			});
			ctx.enqueueWork(() -> {
				ClientMessageHandler.handleClient(payload, ctx);
			});
		}
	}
	
	public static <T extends CustomPacketPayload> void handleServer(T payload, IPayloadContext ctx) {
		MinecraftServer server = ctx.player().getServer();
		if (payload instanceof IMessage message)
			message.onServerReceived(server, (ServerPlayer) ctx.player());
	}
	
	public static class ClientMessageHandler {
		
		public static <T extends CustomPacketPayload> void handleClient(T payload, IPayloadContext ctx) {
			Minecraft minecraft = Minecraft.getInstance();
			if (payload instanceof IMessage message)
				message.onClientReceived(minecraft, minecraft.player);
		}
	}
}
