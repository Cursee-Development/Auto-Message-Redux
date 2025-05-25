package com.cursee.automessage;

import com.cursee.automessage.core.registry.ModRegistryFabric;
import net.fabricmc.api.ModInitializer;

public class AutoMessageFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        AutoMessage.init();
        ModRegistryFabric.register();
    }
}
