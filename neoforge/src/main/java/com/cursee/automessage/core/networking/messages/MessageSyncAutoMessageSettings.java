package com.cursee.automessage.core.networking.messages;

//import net.jason13.automessage.CommonConstants;
//import net.jason13.automessage.neocommon.capability.AutoMessageProperties;
//import net.jason13.automessage.neonetwork.IMessage;
//import net.jason13.automessage.neoregistry.AutoMessageDataStorage;
import com.cursee.automessage.Constants;
import com.cursee.automessage.core.capability.AutoMessageDataStorage;
import com.cursee.automessage.core.capability.AutoMessageProperties;
import com.cursee.automessage.core.networking.IMessage;
import com.cursee.automessage.core.networking.payloads.NeoForgeInitialSyncPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.GustParticle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class MessageSyncAutoMessageSettings implements IMessage {
	public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sync_familiar_settings");
	
	public CompoundTag tag;
	
	public MessageSyncAutoMessageSettings(FriendlyByteBuf buf) {
		this.decode(buf);
	}
	
	public MessageSyncAutoMessageSettings(AutoMessageProperties cap) {
//		this.tag = cap.serializeNBT();
	}
	
	@Override
	public void onClientReceived(Minecraft minecraft, Player player) {
//		player.getData(AutoMessageDataStorage.PROPERTIES).deserializeNBT(this.tag);
	}
	
	@Override
	public void encode(FriendlyByteBuf byteBuf) {
		byteBuf.writeNbt(this.tag);
	}
	
	@Override
	public void decode(FriendlyByteBuf byteBuf) {
		this.tag = byteBuf.readNbt();
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return NeoForgeInitialSyncPayload.ID;
	}

//	@Override
//	public ResourceLocation id() {
//		return ID;
//	}
}
