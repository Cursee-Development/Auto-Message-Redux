package com.cursee.automessage.core.networking.payloads;

import com.cursee.automessage.AutoMessageNeoForge;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record NeoForgePlaytimeDataPayload(int playtime) implements CustomPacketPayload {

    public static final Type<NeoForgePlaytimeDataPayload> ID = new Type<>(AutoMessageNeoForge.PLAYTIME);

    public static final StreamCodec<RegistryFriendlyByteBuf, NeoForgePlaytimeDataPayload> CODEC = StreamCodec.ofMember(NeoForgePlaytimeDataPayload::write, NeoForgePlaytimeDataPayload::read);

    private static NeoForgePlaytimeDataPayload read(RegistryFriendlyByteBuf buf) {
        int playtime = buf.readInt();

        return new NeoForgePlaytimeDataPayload(playtime);
    }

    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(playtime);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

//    public static void applyS2C(NeoForgePlaytimeDataPayload packet, ClientPlayNetworking.Context context) {
//        context.client().execute(() -> {
//            AutoMessageFabricClient.playerData.playtime = packet.playtime();
//        });
//    }
//    public static void applyC2S(NeoForgePlaytimeDataPayload packet, ClientPlayNetworking.Context context) {}
}
