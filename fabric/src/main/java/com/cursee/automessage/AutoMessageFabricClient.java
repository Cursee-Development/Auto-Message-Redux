package com.cursee.automessage;

import com.cursee.automessage.core.networking.FabricHardCountDataPayload;
import com.cursee.automessage.core.networking.FabricInitialSyncPayload;
import com.cursee.automessage.core.networking.FabricPlaytimeDataPayload;
import com.cursee.automessage.core.networking.FabricSoftCountDataPayload;
import com.cursee.automessage.core.util.PlayerData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.Arrays;

public class AutoMessageFabricClient implements ClientModInitializer {

    public static PlayerData playerData = new PlayerData();

    @Override
    public void onInitializeClient() {
//        ClientPlayNetworking.registerGlobalReceiver(FabricInitialSyncPayload.ID, FabricInitialSyncPayload::applyS2C);
//        ClientPlayNetworking.registerGlobalReceiver(FabricPlaytimeDataPayload.ID, FabricPlaytimeDataPayload::applyS2C);
//        ClientPlayNetworking.registerGlobalReceiver(FabricSoftCountDataPayload.ID, FabricSoftCountDataPayload::applyS2C);
//        ClientPlayNetworking.registerGlobalReceiver(FabricHardCountDataPayload.ID, FabricHardCountDataPayload::applyS2C);

        ClientPlayNetworking.registerGlobalReceiver(FabricInitialSyncPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                playerData.playtime = payload.playtime();
                playerData.soft_counts = payload.soft_counts();
                playerData.hard_counts = payload.hard_counts();

                System.out.println("client received initial sync with values playtime, soft_counts, hard_counts");
                System.out.println(payload.playtime());
                System.out.println(Arrays.toString(payload.soft_counts()));
                System.out.println(Arrays.toString(payload.hard_counts()));
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(FabricPlaytimeDataPayload.ID, (payload, context) -> {

            playerData.playtime = payload.playtime();

        });
        ClientPlayNetworking.registerGlobalReceiver(FabricSoftCountDataPayload.ID, (payload, context) -> {

            playerData.soft_counts = payload.soft_counts();

        });
        ClientPlayNetworking.registerGlobalReceiver(FabricHardCountDataPayload.ID, (payload, context) -> {

            playerData.hard_counts = payload.hard_counts();

        });
    }
}
