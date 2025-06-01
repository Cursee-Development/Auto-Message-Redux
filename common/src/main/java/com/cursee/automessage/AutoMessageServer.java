package com.cursee.automessage;

import com.cursee.automessage.core.message.ClientMessageService;
import com.cursee.automessage.core.message.ServerMessageService;
import com.cursee.automessage.core.message.util.MessageType;
import com.cursee.automessage.core.util.JsonFileHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.io.File;

public class AutoMessageServer {

    static MinecraftServer SERVER;

    public static void init(MinecraftServer server) {

        SERVER = server;

        ServerMessageService.load();
    }

    public static void onFirstJoinLevel(ServerPlayer player, ServerLevel level) {
        if (level == null || !ServerMessageService.instance.general.enabled) return;

        ServerMessageService.instance.ON_FIRST_JOIN_MESSAGES.forEach(message -> {

            boolean hasTag = player.getTags().contains(message.identifier + ".firstJoin");

            if (message.repeats || !hasTag) {

                if (message.type == MessageType.CHAT) {
                    if (message.link != null && !message.link.isEmpty()) {
                        player.sendSystemMessage(
                                Component.literal(message.text.replace("%player%", player.getDisplayName().getString()).replace("%link%", message.link)).withStyle(style -> {
                                    return style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, message.link));
                                })
                        );
                    }
                    else player.sendSystemMessage(Component.literal(message.text.replace("%player%", player.getDisplayName().getString())));
                }
                else player.displayClientMessage(Component.literal(message.text.replace("%player%", player.getDisplayName().getString())), true);

                if (!hasTag) player.addTag(message.identifier + ".firstJoin");
            }
        });
    }

    public static void onJoinLevel(ServerPlayer player, ServerLevel level) {
        if (level == null || !ServerMessageService.instance.general.enabled) return;
        ServerMessageService.instance.ON_JOIN_LEVEL_MESSAGES.forEach(message -> {

            if (message.type == MessageType.CHAT) {
                if (message.link != null && !message.link.isEmpty()) {
                    player.sendSystemMessage(
                            Component.literal(message.text.replace("%player%", player.getDisplayName().getString()).replace("%link%", message.link)).withStyle(style -> {
                                return style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, message.link));
                            })
                    );
                }
                else player.sendSystemMessage(Component.literal(message.text.replace("%player%", player.getDisplayName().getString())));
            }
            else player.displayClientMessage(Component.literal(message.text.replace("%player%", player.getDisplayName().getString())), true);
        });
    }

    public static void onDeath(Player player) {
        ServerMessageService.instance.ON_DEATH_MESSAGES.forEach(message -> {

            if (message.type == MessageType.CHAT) {
                    if (message.link != null && !message.link.isEmpty()) {
                        player.sendSystemMessage(
                                Component.literal(message.text.replace("%player%", player.getDisplayName().getString()).replace("%link%", message.link)).withStyle(style -> {
                                    return style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, message.link));
                                })
                        );
                    }
                    else player.sendSystemMessage(Component.literal(message.text.replace("%player%", player.getDisplayName().getString())));
                }
            else player.displayClientMessage(Component.literal(message.text.replace("%player%", player.getDisplayName().getString())), true);
        });
    }

    public static void onRespawn(ServerPlayer player) {
        ServerMessageService.instance.ON_RESPAWN_MESSAGES.forEach(message -> {

            if (message.type == MessageType.CHAT) {
                    if (message.link != null && !message.link.isEmpty()) {
                        player.sendSystemMessage(
                                Component.literal(message.text.replace("%player%", player.getDisplayName().getString()).replace("%link%", message.link)).withStyle(style -> {
                                    return style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, message.link));
                                })
                        );
                    }
                    else player.sendSystemMessage(Component.literal(message.text.replace("%player%", player.getDisplayName().getString())));
                }
            else player.displayClientMessage(Component.literal(message.text.replace("%player%", player.getDisplayName().getString())), true);
        });
    }
}
