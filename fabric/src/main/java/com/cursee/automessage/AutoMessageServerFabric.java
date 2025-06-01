package com.cursee.automessage;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class AutoMessageServerFabric {

    public AutoMessageServerFabric(MinecraftServer server) {
        AutoMessageServer.init(server);

        ServerEntityEvents.ENTITY_LOAD.register((entity, serverLevel) -> {
            if (!(entity instanceof ServerPlayer player) || serverLevel == null) return;

            AutoMessageServer.onFirstJoinLevel(player, serverLevel);
            AutoMessageServer.onJoinLevel(player, serverLevel);
        });
    }
}
