package com.cursee.automessage.core.networking.payloads;

import com.cursee.automessage.AutoMessageNeoForge;
import com.cursee.automessage.core.capability.AutoMessageDataStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record NeoForgeInitialSyncPayload(int playtime, long[] soft_counts, long[] hard_counts) implements CustomPacketPayload {


    public static CompoundTag tag;

    public static final Type<NeoForgeInitialSyncPayload> ID = new Type<>(AutoMessageNeoForge.INITIAL_SYNC);

//    public static final StreamCodec<RegistryFriendlyByteBuf, NeoForgeInitialSyncPayload> CODEC = StreamCodec.composite(ByteBufCodecs.INT, NeoForgeInitialSyncPayload::test1, ByteBufCodecs.BYTE_ARRAY, NeoForgeInitialSyncPayload::test2, ByteBufCodecs.BYTE_ARRAY, NeoForgeInitialSyncPayload::test3, NeoForgeInitialSyncPayload::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, NeoForgeInitialSyncPayload> CODEC = StreamCodec.ofMember(NeoForgeInitialSyncPayload::write, NeoForgeInitialSyncPayload::read);

    private static NeoForgeInitialSyncPayload read(RegistryFriendlyByteBuf buf) {
        int playtime = buf.readInt();
        long[] soft_counts = buf.readLongArray();
        long[] hard_counts = buf.readLongArray();

        return new NeoForgeInitialSyncPayload(playtime, soft_counts, hard_counts);
    }

    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(playtime());
        buf.writeLongArray(soft_counts());
        buf.writeLongArray(hard_counts());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    public void handle(IPayloadContext iPayloadContext) {

        iPayloadContext.player().getData(AutoMessageDataStorage.PROPERTIES).deserializeNBT(this.tag);
    }

//    public static void applyS2C(NeoForgeInitialSyncPayload packet, ClientPlayNetworking.Context context) {
//        context.client().execute(() -> {
//            AutoMessageFabricClient.playerData.playtime = packet.playtime();
//            AutoMessageFabricClient.playerData.soft_counts = packet.soft_counts();
//            AutoMessageFabricClient.playerData.hard_counts = packet.hard_counts();
//        });
//    }
//    public static void applyC2S(NeoForgeInitialSyncPayload packet, ClientPlayNetworking.Context context) {}
}
