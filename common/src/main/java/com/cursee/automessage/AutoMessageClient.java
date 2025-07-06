package com.cursee.automessage;

import com.cursee.automessage.core.message.ClientMessageService;
import com.cursee.automessage.core.message.util.MessageType;
import com.cursee.automessage.core.util.JsonFileHelper;
import com.cursee.automessage.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.io.File;
import java.util.Objects;

public class AutoMessageClient {

    static Minecraft CLIENT;

    private static final String AUTOMESSAGE_DATA_DIR_FILEPATH = Services.PLATFORM.getGameDirectory() + File.separator + "automessage_data";
    private static final String CLIENT_SERVER_TAGS_FILEPATH = Services.PLATFORM.getGameDirectory() + File.separator + "automessage_data" + File.separator + "client_server_tags.json";
    public static void init(Minecraft client) {

        CLIENT = client;

        ClientMessageService.load();

        File autoMessageDataDirectory = new File(AUTOMESSAGE_DATA_DIR_FILEPATH);
        if (!autoMessageDataDirectory.isDirectory()) autoMessageDataDirectory.mkdirs();
        JsonFileHelper.addTag(CLIENT_SERVER_TAGS_FILEPATH, "World Name", "your_message_identifier.firstJoin");
        JsonFileHelper.addTag(CLIENT_SERVER_TAGS_FILEPATH, "127.0.0.1", "your_message_identifier.firstJoin");
    }

    // send when a player joins a level, checking against being tagged as joined already
    public static void onFirstJoinLevel(LocalPlayer player, ClientLevel level) {
        if (level == null || !ClientMessageService.instance.general.enabled) return;

        ClientMessageService.instance.ON_FIRST_JOIN_MESSAGES.forEach(message -> {

            String serverName = CLIENT.hasSingleplayerServer() ? Objects.requireNonNull(CLIENT.getSingleplayerServer()).getWorldData().getLevelName() : Objects.requireNonNull(CLIENT.getCurrentServer()).ip;

            // if pack intro has ever been sent before, ignore it.
            if (message.pack_intro && JsonFileHelper.anyKeyContainsTag(new File(CLIENT_SERVER_TAGS_FILEPATH), message.identifier + ".firstJoin")) return;

            if (JsonFileHelper.hasKey(CLIENT_SERVER_TAGS_FILEPATH, serverName)) Constants.LOG.info("Client had key {}, so tags that were already generated are being checked.", serverName);

            boolean hasTag = JsonFileHelper.hasTag(CLIENT_SERVER_TAGS_FILEPATH, serverName, message.identifier + ".firstJoin");
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

                if (!message.repeats && !hasTag) JsonFileHelper.addTag(CLIENT_SERVER_TAGS_FILEPATH, serverName, message.identifier + ".firstJoin");
            }
        });
    }

    // sent every time a player joins a level, disregarding repeats
    public static void onJoinLevel(LocalPlayer player, ClientLevel level) {
        if (level == null || !ClientMessageService.instance.general.enabled) return;
        ClientMessageService.instance.ON_JOIN_LEVEL_MESSAGES.forEach(message -> {

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
        ClientMessageService.instance.ON_DEATH_MESSAGES.forEach(message -> {

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

    public static void onRespawn(LocalPlayer player) {
        ClientMessageService.instance.ON_RESPAWN_MESSAGES.forEach(message -> {

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
