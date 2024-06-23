package com.cursee.automessage;

import com.cursee.automessage.core.Config;
import com.cursee.automessage.core.capability.AutoMessageDataStorage;
import com.cursee.automessage.core.capability.AutoMessageProperties;
import com.cursee.automessage.core.networking.Networking;
import com.cursee.monolib.core.sailing.Sailing;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@Mod(Constants.MOD_ID)
public class AutoMessageNeoForge {

    public static final ResourceLocation INITIAL_SYNC = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "initial_sync");
    public static final ResourceLocation PLAYTIME = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "playtime");
    public static final ResourceLocation SOFT_COUNTS = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "soft_counts");
    public static final ResourceLocation HARD_COUNTS = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hard_counts");

    public static AutoMessageNeoForge INSTANCE;

    public AutoMessageNeoForge(IEventBus bus) {

        AutoMessage.init();
        Sailing.register(Constants.MOD_NAME, Constants.MOD_ID, Constants.MOD_VERSION, Constants.MC_VERSION_RAW, Constants.PUBLISHER_AUTHOR, Constants.PRIMARY_CURSEFORGE_MODRINTH);

        INSTANCE = this;
        Config.initialize();

        AutoMessageDataStorage.ATTACHMENT_TYPES.register(bus);
        bus.addListener(Networking::register);
        NeoForge.EVENT_BUS.addListener(AutoMessageDataStorage::onPlayerClone);
        NeoForge.EVENT_BUS.addListener(AutoMessageDataStorage::onJoinWorld);
    }

    @EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static class NeoEventBusListeners {
        @SubscribeEvent
        public static void onStartTick(ServerTickEvent.Pre event){

            for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {


                System.out.println(player.hasData(AutoMessageDataStorage.PROPERTIES));


                boolean flag_SkipZeroTick = event.getServer().getTickCount() != 0;
                boolean flag_OperatingOncePerSecond = event.getServer().getTickCount() % (20) == 0;

                if (flag_SkipZeroTick && flag_OperatingOncePerSecond) {
                    if (player.hasData(AutoMessageDataStorage.PROPERTIES)) {

                        AutoMessageProperties messaging = player.getData(AutoMessageDataStorage.PROPERTIES);

                        messaging.incrementPlaytime();
                        // props.playtime++;


                        for (int i = 0; i < Config.messages.size(); i++) {
                            int messageIndex = i;

                            boolean flag_PlaytimeHittingInterval = messaging.getPlaytime() % Config.intervals.get(messageIndex) == 0;
                            boolean flag_SoftLessThanLimit = messaging.getSoftCounts()[messageIndex] < Config.soft_limits.get(messageIndex);
                            boolean flag_SoftUnlimited = Config.soft_limits.get(messageIndex) == 0;
                            boolean flag_HardLessThanLimit = messaging.getHardCounts()[messageIndex] < Config.hard_limits.get(messageIndex);
                            boolean flag_HardUnlimited = Config.hard_limits.get(messageIndex) == 0;
                            boolean flag_Enabled = Config.enabled;

                            if (flag_PlaytimeHittingInterval && (flag_SoftLessThanLimit || flag_SoftUnlimited) && (flag_HardLessThanLimit || flag_HardUnlimited) && flag_Enabled) {

                                player.sendSystemMessage
                                        (
                                                Component.literal(Config.messages.get(messageIndex)).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, String.valueOf(Config.links.get(messageIndex)))))
                                        );

                                messaging.incrementSoftCountAtIndex(messageIndex);
                                messaging.incrementHardCountAtIndex(messageIndex);
                            }
                        }
                    }
                }
            }
        }
    }

}
