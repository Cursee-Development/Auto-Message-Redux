package com.cursee.automessage;

import com.cursee.automessage.core.Config;
import com.cursee.automessage.core.capability.MessagingCapability;
import com.cursee.automessage.core.capability.MessagingProvider;
import com.cursee.monolib.core.sailing.Sailing;
import net.jason13.monolib.methods.BlockMethods;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class AutoMessageForge {
    
    public AutoMessageForge() {
    
        AutoMessage.init();
        Sailing.register(Constants.MOD_NAME, Constants.MOD_ID, Constants.MOD_VERSION, Constants.MC_VERSION_RAW, Constants.PUBLISHER_AUTHOR, Constants.PRIMARY_CURSEFORGE_MODRINTH);
        Config.initialize();

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::onRegisterCapabilitiesEvent);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onRegisterCapabilitiesEvent(RegisterCapabilitiesEvent event) {
        event.register(MessagingCapability.class);
    }

    @SubscribeEvent
    public void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof Player)
        {
            if (!event.getObject().getCapability(MessagingProvider.PLAYER_MESSAGING_CAPABILITY).isPresent())
            {
                event.addCapability(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "properties"), new MessagingProvider());
            }
        }
    }

    @SubscribeEvent
    public void onStartTick(TickEvent.ServerTickEvent event) {

        boolean flag_StartOfTick = event.phase == TickEvent.Phase.START;
        boolean flag_SkipZeroTick = event.getServer().getTickCount() != 0;
        boolean flag_OperatingOncePerSecond = event.getServer().getTickCount() % (20) == 0;

        if (flag_StartOfTick && flag_SkipZeroTick && flag_OperatingOncePerSecond) {
            for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
                player.getCapability(MessagingProvider.PLAYER_MESSAGING_CAPABILITY).ifPresent(messaging -> {
                    messaging.incrementPlaytime();

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
                });
            }
        }

    }

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID)
    public static class ModEvents {
        @SubscribeEvent
        public static void onPlayerCloned(PlayerEvent.Clone event) {

            Player original = event.getOriginal();

            if(event.isWasDeath()) {

                original.reviveCaps();

                original.getCapability(MessagingProvider.PLAYER_MESSAGING_CAPABILITY).ifPresent(oldStore -> {
                    event.getEntity().getCapability(MessagingProvider.PLAYER_MESSAGING_CAPABILITY).ifPresent(newStore -> {
                        newStore.copyFrom(oldStore);
                    });
                });

                original.invalidateCaps();
            }
        }
    }
}
