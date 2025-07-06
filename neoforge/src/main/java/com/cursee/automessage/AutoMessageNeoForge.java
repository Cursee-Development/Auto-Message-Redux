package com.cursee.automessage;


import com.cursee.automessage.core.registry.ModRegistryNeoForge;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(Constants.MOD_ID)
public class AutoMessageNeoForge {

    public static IEventBus EVENT_BUS;

    public AutoMessageNeoForge(final FMLModContainer container) {
        AutoMessage.init();
        EVENT_BUS = container.getEventBus();
        ModRegistryNeoForge.register(EVENT_BUS);
        if (FMLEnvironment.dist == Dist.CLIENT) new AutoMessageClientNeoForge(EVENT_BUS);
    }
}