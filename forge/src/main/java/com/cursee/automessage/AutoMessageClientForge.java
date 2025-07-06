package com.cursee.automessage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;

import java.util.function.Consumer;

public class AutoMessageClientForge {

    public AutoMessageClientForge(final ServerAboutToStartEvent serverAboutToStartEvent) {
        AutoMessageClient.init(Minecraft.getInstance());

        MinecraftForge.EVENT_BUS.addListener((Consumer<EntityJoinLevelEvent>) event -> {

            Entity entity = event.getEntity();
            Level level = event.getLevel();
            if (!(entity instanceof LocalPlayer localPlayer) || !(level instanceof ClientLevel clientLevel)) return;
            AutoMessageClient.onFirstJoinLevel(localPlayer, clientLevel);
            AutoMessageClient.onJoinLevel(localPlayer, clientLevel);
        });
    }
}
