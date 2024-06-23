package com.cursee.automessage.core.networking.payloads;

import com.cursee.automessage.AutoMessageNeoForge;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record NeoForgeHardCountDataPayload(long[] hard_counts) implements CustomPacketPayload {

    public static final Type<NeoForgeHardCountDataPayload> ID = new Type<>(AutoMessageNeoForge.HARD_COUNTS);

    public static final StreamCodec<RegistryFriendlyByteBuf, NeoForgeHardCountDataPayload> CODEC = StreamCodec.ofMember(NeoForgeHardCountDataPayload::write, NeoForgeHardCountDataPayload::read);

    private static NeoForgeHardCountDataPayload read(RegistryFriendlyByteBuf buf) {
        long[] hard_counts = buf.readLongArray();

        return new NeoForgeHardCountDataPayload(hard_counts);
    }

    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeLongArray(hard_counts);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

//    public static void applyS2C(NeoForgeHardCountDataPayload packet, ClientPlayNetworking.Context context) {
//        context.client().execute(() -> {
//            AutoMessageFabricClient.playerData.hard_counts = packet.hard_counts();
//        });
//    }
//    public static void applyC2S(NeoForgeHardCountDataPayload packet, ClientPlayNetworking.Context context) {}
}
