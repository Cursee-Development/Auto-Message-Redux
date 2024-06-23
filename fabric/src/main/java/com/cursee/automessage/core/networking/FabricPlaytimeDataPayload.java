package com.cursee.automessage.core.networking;

import com.cursee.automessage.AutoMessage;
import com.cursee.automessage.AutoMessageFabric;
import com.cursee.automessage.AutoMessageFabricClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record FabricPlaytimeDataPayload(int playtime) implements CustomPacketPayload {

    public static final Type<FabricPlaytimeDataPayload> ID = new Type<>(AutoMessageFabric.PLAYTIME);

    public static final StreamCodec<RegistryFriendlyByteBuf, FabricPlaytimeDataPayload> CODEC = StreamCodec.ofMember(FabricPlaytimeDataPayload::write, FabricPlaytimeDataPayload::read);

    private static FabricPlaytimeDataPayload read(RegistryFriendlyByteBuf buf) {
        int playtime = buf.readInt();

        return new FabricPlaytimeDataPayload(playtime);
    }

    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(playtime);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    public static void applyS2C(FabricPlaytimeDataPayload packet, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            AutoMessageFabricClient.playerData.playtime = packet.playtime();
        });
    }
    public static void applyC2S(FabricPlaytimeDataPayload packet, ClientPlayNetworking.Context context) {}
}
