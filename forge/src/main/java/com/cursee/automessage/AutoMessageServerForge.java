package com.cursee.automessage;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;

import java.util.function.Consumer;

public class AutoMessageServerForge {

    public AutoMessageServerForge(final ServerAboutToStartEvent serverAboutToStartEvent) {
        AutoMessageServer.init(serverAboutToStartEvent.getServer());

        MinecraftForge.EVENT_BUS.addListener((Consumer<EntityJoinLevelEvent>) event -> {
            if (!(event.getEntity() instanceof ServerPlayer serverPlayer) || !(event.getLevel() instanceof ServerLevel serverLevel)) return;

            AutoMessageServer.onFirstJoinLevel(serverPlayer, serverLevel);
            AutoMessageServer.onJoinLevel(serverPlayer, serverLevel);
        });
    }
}
