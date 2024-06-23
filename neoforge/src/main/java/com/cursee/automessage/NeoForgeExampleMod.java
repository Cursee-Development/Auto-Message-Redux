package com.cursee.automessage;


import com.cursee.automessage.config.Config;
import com.cursee.automessage.neocommon.capability.AutoMessageProperties;
import com.cursee.automessage.neonetwork.Networking;
import com.cursee.automessage.neoregistry.AutoMessageDataStorage;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@Mod(Constants.MOD_ID)
public class NeoForgeExampleMod {
    
    public static NeoForgeExampleMod INSTANCE;

    public NeoForgeExampleMod(IEventBus bus) {
        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.
        
        // Use NeoForge to bootstrap the Common mod.
        Constants.LOG.info("Hello NeoForge world!");
        AutoMessage.init();
        
        INSTANCE = this;
//        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC, "automessage-common.toml");
        AutoMessageDataStorage.ATTACHMENT_TYPES.register(bus);
        // bus.addListener(AutoMessageCapabilities::onRegisterCapabilities);
        bus.addListener(Networking::register);
        NeoForge.EVENT_BUS.addListener(AutoMessageDataStorage::onPlayerClone);
        NeoForge.EVENT_BUS.addListener(AutoMessageDataStorage::onJoinWorld);
    }
    
    @EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
    public static class NeoEventBusListeners {
        @SubscribeEvent
        public static void onStartTick(ServerTickEvent.Pre event){
            for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {

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

                                player.sendSystemMessage(Component.literal(Config.messages.get(messageIndex)).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, String.valueOf(Config.links.get(messageIndex))))));

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