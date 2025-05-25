package com.cursee.automessage;

import com.cursee.automessage.core.registry.ModRegistryForge;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(Constants.MOD_ID)
public class AutoMessageForge {

    public static IEventBus EVENT_BUS;

    public AutoMessageForge(FMLJavaModLoadingContext context) {
        AutoMessage.init();
        EVENT_BUS = context.getModEventBus();
        ModRegistryForge.register(EVENT_BUS);
        if (FMLEnvironment.dist == Dist.CLIENT) new AutoMessageClientForge(EVENT_BUS);
        MinecraftForge.EVENT_BUS.addListener(AutoMessageServerForge::new);
    }

    @SuppressWarnings("removal")
    public AutoMessageForge() {
        this(FMLJavaModLoadingContext.get());
    }
}