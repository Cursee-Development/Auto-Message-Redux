package com.cursee.automessage.mixin;

import com.cursee.automessage.AutoMessageClient;
import com.cursee.automessage.core.message.ClientMessageService;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// respawn
@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Inject(method = "respawn", at = @At("TAIL"))
    private void automessage$restoreFrom(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        AutoMessageClient.onRespawn(player);
    }
}
