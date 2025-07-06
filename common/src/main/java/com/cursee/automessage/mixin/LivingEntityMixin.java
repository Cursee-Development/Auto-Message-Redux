package com.cursee.automessage.mixin;

import com.cursee.automessage.AutoMessageClient;
import com.cursee.automessage.AutoMessageServer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "die", at = @At("HEAD"))
    private void automessage$die(DamageSource cause, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (!(entity instanceof Player player)) return;

        if (player.level().isClientSide()) AutoMessageClient.onDeath(player);
        else AutoMessageServer.onDeath(player);
    }
}
