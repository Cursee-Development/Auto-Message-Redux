package com.cursee.automessage.core.networking;

import com.cursee.automessage.AutoMessageFabric;
import com.cursee.automessage.AutoMessageFabricClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record FabricInitialSyncPayload(int playtime, long[] soft_counts, long[] hard_counts) implements CustomPacketPayload {

    public static final Type<FabricInitialSyncPayload> ID = new Type<>(AutoMessageFabric.INITIAL_SYNC);

//    public static final StreamCodec<RegistryFriendlyByteBuf, FabricInitialSyncPayload> CODEC = StreamCodec.composite(ByteBufCodecs.INT, FabricInitialSyncPayload::test1, ByteBufCodecs.BYTE_ARRAY, FabricInitialSyncPayload::test2, ByteBufCodecs.BYTE_ARRAY, FabricInitialSyncPayload::test3, FabricInitialSyncPayload::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, FabricInitialSyncPayload> CODEC = StreamCodec.ofMember(FabricInitialSyncPayload::write, FabricInitialSyncPayload::read);

    private static FabricInitialSyncPayload read(RegistryFriendlyByteBuf buf) {
        int playtime = buf.readInt();
        long[] soft_counts = buf.readLongArray();
        long[] hard_counts = buf.readLongArray();

        return new FabricInitialSyncPayload(playtime, soft_counts, hard_counts);
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

    public static void applyS2C(FabricInitialSyncPayload packet, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            AutoMessageFabricClient.playerData.playtime = packet.playtime();
            AutoMessageFabricClient.playerData.soft_counts = packet.soft_counts();
            AutoMessageFabricClient.playerData.hard_counts = packet.hard_counts();
        });
    }
    public static void applyC2S(FabricInitialSyncPayload packet, ClientPlayNetworking.Context context) {}
}
