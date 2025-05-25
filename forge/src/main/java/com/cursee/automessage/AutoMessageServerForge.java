package com.cursee.automessage;

import net.minecraftforge.event.server.ServerAboutToStartEvent;

public class AutoMessageServerForge {

    public AutoMessageServerForge(final ServerAboutToStartEvent event) {
        AutoMessageServer.init();
    }
}
