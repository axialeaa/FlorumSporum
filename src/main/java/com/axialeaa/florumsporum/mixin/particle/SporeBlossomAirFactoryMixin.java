package com.axialeaa.florumsporum.mixin.particle;

import com.axialeaa.florumsporum.particle.FragileParticle;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.WaterSuspendParticle;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.particle.SimpleParticleType;

@Mixin(WaterSuspendParticle.SporeBlossomAirFactory.class)
public class SporeBlossomAirFactoryMixin {

    @Inject(method = "createParticle(Lnet/minecraft/particle/SimpleParticleType;Lnet/minecraft/client/world/ClientWorld;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("TAIL"))
    private void setDiscardOnCollision(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> cir, @Local WaterSuspendParticle waterSuspendParticle) {
        ((FragileParticle) waterSuspendParticle).setDiscardOnCollision(true);
    }

}
