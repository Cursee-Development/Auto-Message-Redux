package com.cursee.automessage;

import com.cursee.automessage.core.Config;
import com.cursee.automessage.core.networking.FabricHardCountDataPayload;
import com.cursee.automessage.core.networking.FabricInitialSyncPayload;
import com.cursee.automessage.core.networking.FabricPlaytimeDataPayload;
import com.cursee.automessage.core.networking.FabricSoftCountDataPayload;
import com.cursee.automessage.core.util.PlayerData;
import com.cursee.automessage.core.util.StateSaverAndLoader;
import com.cursee.monolib.core.sailing.Sailing;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Arrays;

public class AutoMessageFabric implements ModInitializer {

    public static final ResourceLocation INITIAL_SYNC = new ResourceLocation(Constants.MOD_ID, "initial_sync");
    public static final ResourceLocation PLAYTIME = new ResourceLocation(Constants.MOD_ID, "playtime");
    public static final ResourceLocation SOFT_COUNTS = new ResourceLocation(Constants.MOD_ID, "soft_counts");
    public static final ResourceLocation HARD_COUNTS = new ResourceLocation(Constants.MOD_ID, "hard_counts");

    @Override
    public void onInitialize() {
        
        AutoMessage.init();
        Sailing.register(Constants.MOD_NAME, Constants.MOD_ID, Constants.MOD_VERSION, Constants.MC_VERSION_RAW, Constants.PUBLISHER_AUTHOR, Constants.PRIMARY_CURSEFORGE_MODRINTH);
        Config.initialize();

        PayloadTypeRegistry.playS2C().register(FabricInitialSyncPayload.ID, FabricInitialSyncPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(FabricPlaytimeDataPayload.ID, FabricPlaytimeDataPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(FabricSoftCountDataPayload.ID, FabricSoftCountDataPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(FabricHardCountDataPayload.ID, FabricHardCountDataPayload.CODEC);

        ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> {
            PlayerData playerState = StateSaverAndLoader.getPlayerState(handler.getPlayer());

            Arrays.fill(playerState.soft_counts, 0L);

            server.execute(() -> {
                ServerPlayNetworking.send(handler.getPlayer(), new FabricInitialSyncPayload(playerState.playtime, playerState.soft_counts, playerState.hard_counts));
            });
        }));

        ServerTickEvents.START_SERVER_TICK.register(server -> {

            final int ticks = server.getTickCount();

            final boolean skipZeroth = ticks != 0;
            final boolean oncePerSecond = ticks % 20 ==0;

            if (Config.enabled && skipZeroth && oncePerSecond) {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {

                    PlayerData playerData = StateSaverAndLoader.getPlayerState(player);
                    playerData.playtime += 1;

                    for (int i = 0; i < Config.messages.size(); i++) {
                        final int messageIndex = i;

                        int getPlaytime = playerData.playtime;
                        long[] softCounts = playerData.soft_counts;
                        long[] hardCounts = playerData.hard_counts;

                        long softCountAtMessageIndex = softCounts[messageIndex];
                        long hardCountAtMessageIndex = hardCounts[messageIndex];

                        final String possibleMessage = Config.messages.get(messageIndex);
                        final String possibleLink = Config.links.get(messageIndex);

                        boolean flag_HasMessage = !(possibleMessage.isEmpty() || possibleMessage.isBlank());
                        boolean flag_HasLink = !(possibleLink.isEmpty() || possibleLink.isBlank());

                        boolean flag_PlaytimeHittingInterval = getPlaytime % Math.toIntExact(Config.intervals.get(messageIndex)) == 0;
                        boolean flag_SoftLessThanLimit = softCountAtMessageIndex < Math.toIntExact(Config.soft_limits.get(messageIndex));
                        boolean flag_SoftUnlimited = Math.toIntExact(Config.soft_limits.get(messageIndex)) == 0;
                        boolean flag_HardLessThanLimit = hardCountAtMessageIndex < Math.toIntExact(Config.hard_limits.get(messageIndex));
                        boolean flag_HardUnlimited = Math.toIntExact(Config.hard_limits.get(messageIndex)) == 0;


                        if (flag_HasMessage && flag_PlaytimeHittingInterval && (flag_SoftLessThanLimit || flag_SoftUnlimited) && (flag_HardLessThanLimit || flag_HardUnlimited)) {

                            if (flag_HasLink) {
                                player.sendSystemMessage(Component.literal(possibleMessage).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, possibleLink))));
                            } else {
                                player.sendSystemMessage(Component.literal(possibleMessage));
                            }

                            playerData.incrementSoftCounterAtIndex(messageIndex);
                            playerData.incrementHardCounterAtIndex(messageIndex);
                        }
                    }
                    ServerPlayNetworking.send(player, new FabricPlaytimeDataPayload(playerData.playtime));
                    ServerPlayNetworking.send(player, new FabricSoftCountDataPayload(playerData.soft_counts));
                    ServerPlayNetworking.send(player, new FabricHardCountDataPayload(playerData.hard_counts));
                }
            }
        });
    }
}
