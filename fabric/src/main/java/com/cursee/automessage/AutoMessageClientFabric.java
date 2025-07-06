package com.cursee.automessage;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class AutoMessageClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        ClientEntityEvents.ENTITY_LOAD.register((entity, clientLevel) -> {
            if (!(entity instanceof LocalPlayer player) || clientLevel == null) return;

            AutoMessageClient.init(Minecraft.getInstance());

            AutoMessageClient.onFirstJoinLevel(player, clientLevel);
            AutoMessageClient.onJoinLevel(player, clientLevel);
        });
    }
}
