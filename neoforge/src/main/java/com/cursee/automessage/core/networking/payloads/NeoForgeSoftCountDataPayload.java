package com.cursee.automessage.core.networking.payloads;

import com.cursee.automessage.AutoMessageNeoForge;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record NeoForgeSoftCountDataPayload(long[] soft_counts) implements CustomPacketPayload {

    public static final Type<NeoForgeSoftCountDataPayload> ID = new Type<>(AutoMessageNeoForge.SOFT_COUNTS);

    public static final StreamCodec<RegistryFriendlyByteBuf, NeoForgeSoftCountDataPayload> CODEC = StreamCodec.ofMember(NeoForgeSoftCountDataPayload::write, NeoForgeSoftCountDataPayload::read);

    private static NeoForgeSoftCountDataPayload read(RegistryFriendlyByteBuf buf) {
        long[] soft_counts = buf.readLongArray();

        return new NeoForgeSoftCountDataPayload(soft_counts);
    }

    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeLongArray(soft_counts);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

//    public static void applyS2C(NeoForgeSoftCountDataPayload packet, ClientPlayNetworking.Context context) {
//        context.client().execute(() -> {
//            AutoMessageFabricClient.playerData.soft_counts = packet.soft_counts();
//        });
//    }
//    public static void applyC2S(NeoForgeSoftCountDataPayload packet, ClientPlayNetworking.Context context) {}
}
