package com.axialeaa.florumsporum.mixin.particle;

import com.axialeaa.florumsporum.particle.FragileParticle;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.WaterSuspendParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.particle.SimpleParticleType;

@Mixin(WaterSuspendParticle.SporeBlossomAirFactory.class)
public class SporeBlossomAirFactoryMixin {

    @Inject(method = "createParticle(Lnet/minecraft/particle/SimpleParticleType;Lnet/minecraft/client/world/ClientWorld;DDDDDDLnet/minecraft/util/math/random/Random;)Lnet/minecraft/client/particle/Particle;", at = @At("TAIL"))
    private void setDiscardOnCollision(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, Random random, CallbackInfoReturnable<Particle> cir, @Local WaterSuspendParticle waterSuspendParticle) {
        assert waterSuspendParticle instanceof FragileParticle;
        ((FragileParticle) waterSuspendParticle).setDiscardOnCollision(true);
    }

}
