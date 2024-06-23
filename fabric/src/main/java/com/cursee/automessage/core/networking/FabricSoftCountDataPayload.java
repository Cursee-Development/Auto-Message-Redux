package com.cursee.automessage.core.networking;

import com.cursee.automessage.AutoMessageFabric;
import com.cursee.automessage.AutoMessageFabricClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record FabricSoftCountDataPayload(long[] soft_counts) implements CustomPacketPayload {

    public static final Type<FabricSoftCountDataPayload> ID = new Type<>(AutoMessageFabric.SOFT_COUNTS);

    public static final StreamCodec<RegistryFriendlyByteBuf, FabricSoftCountDataPayload> CODEC = StreamCodec.ofMember(FabricSoftCountDataPayload::write, FabricSoftCountDataPayload::read);

    private static FabricSoftCountDataPayload read(RegistryFriendlyByteBuf buf) {
        long[] soft_counts = buf.readLongArray();

        return new FabricSoftCountDataPayload(soft_counts);
    }

    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeLongArray(soft_counts);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    public static void applyS2C(FabricSoftCountDataPayload packet, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            AutoMessageFabricClient.playerData.soft_counts = packet.soft_counts();
        });
    }
    public static void applyC2S(FabricSoftCountDataPayload packet, ClientPlayNetworking.Context context) {}
}
