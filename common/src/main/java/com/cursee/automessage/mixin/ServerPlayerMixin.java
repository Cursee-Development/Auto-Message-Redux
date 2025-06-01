package com.cursee.automessage.mixin;

import com.cursee.automessage.AutoMessageClient;
import com.cursee.automessage.AutoMessageServer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// restore
@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Inject(method = "restoreFrom", at = @At("TAIL"))
    private void automessage$restoreFrom(ServerPlayer that, boolean keepEverything, CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        AutoMessageServer.onRespawn(player);
    }
}
