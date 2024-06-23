package com.cursee.automessage.neonetwork.messages;

import com.cursee.automessage.Constants;
import com.cursee.automessage.neocommon.capability.AutoMessageProperties;
import com.cursee.automessage.neonetwork.IMessage;
import com.cursee.automessage.neoregistry.AutoMessageDataStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public record MessageSyncAutoMessageSettings(int playtime, long[] soft_counts, long[] hard_counts) implements IMessage {

	public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sync_familiar_settings");

	public static final Type<MessageSyncAutoMessageSettings> TYPE = new Type<>(ID);

	public static final StreamCodec<RegistryFriendlyByteBuf, MessageSyncAutoMessageSettings> CODEC = StreamCodec.ofMember(MessageSyncAutoMessageSettings::write, MessageSyncAutoMessageSettings::read);

	private void write(RegistryFriendlyByteBuf buf) {
		buf.writeInt(playtime);
		buf.writeLongArray(soft_counts);
		buf.writeLongArray(hard_counts);
	}

	private static MessageSyncAutoMessageSettings read(RegistryFriendlyByteBuf buf) {
		return new MessageSyncAutoMessageSettings(buf.readInt(), buf.readLongArray(), buf.readLongArray());
	}

//	public CompoundTag tag;
//
//	public MessageSyncAutoMessageSettings(FriendlyByteBuf buf) {
//		this.decode(buf);
//	}
//
//	public MessageSyncAutoMessageSettings(AutoMessageProperties cap) {
//		this.tag = cap.serializeNBT();
//	}
//
//	@Override
//	public void onClientReceived(Minecraft minecraft, Player player) {
//		player.getData(AutoMessageDataStorage.PROPERTIES).deserializeNBT(this.tag);
//	}
//
//	@Override
//	public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player) {
//		IMessage.super.onServerReceived(minecraftServer, player);
//	}
//
//	@Override
//	public void write(FriendlyByteBuf pBuffer) {
//		IMessage.super.write(pBuffer);
//	}
//
//	@Override
//	public void encode(FriendlyByteBuf byteBuf) {
//		byteBuf.writeNbt(this.tag);
//	}
//
//	@Override
//	public void decode(FriendlyByteBuf byteBuf) {
//		this.tag = byteBuf.readNbt();
//	}
	
//	@Override
//	public ResourceLocation id() {
//		return ID;
//	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
