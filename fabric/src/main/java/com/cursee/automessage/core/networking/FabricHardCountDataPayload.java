package com.cursee.automessage.core.networking;

import com.cursee.automessage.AutoMessageFabric;
import com.cursee.automessage.AutoMessageFabricClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record FabricHardCountDataPayload(long[] hard_counts) implements CustomPacketPayload {

    public static final Type<FabricHardCountDataPayload> ID = new Type<>(AutoMessageFabric.HARD_COUNTS);

    public static final StreamCodec<RegistryFriendlyByteBuf, FabricHardCountDataPayload> CODEC = StreamCodec.ofMember(FabricHardCountDataPayload::write, FabricHardCountDataPayload::read);

    private static FabricHardCountDataPayload read(RegistryFriendlyByteBuf buf) {
        long[] hard_counts = buf.readLongArray();

        return new FabricHardCountDataPayload(hard_counts);
    }

    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeLongArray(hard_counts);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    public static void applyS2C(FabricHardCountDataPayload packet, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            AutoMessageFabricClient.playerData.hard_counts = packet.hard_counts();
        });
    }
    public static void applyC2S(FabricHardCountDataPayload packet, ClientPlayNetworking.Context context) {}
}
