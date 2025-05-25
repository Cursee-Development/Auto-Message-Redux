package com.cursee.automessage;

import net.minecraft.server.MinecraftServer;

public class AutoMessageServerFabric {

    public AutoMessageServerFabric(MinecraftServer server) {
        AutoMessageServer.init();
    }
}
