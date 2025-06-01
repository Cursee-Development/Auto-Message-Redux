package com.cursee.automessage;

import com.cursee.automessage.core.registry.ModRegistryFabric;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class AutoMessageFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        AutoMessage.init();
        ModRegistryFabric.register();
        ServerLifecycleEvents.SERVER_STARTING.register(AutoMessageServerFabric::new);
    }
}
