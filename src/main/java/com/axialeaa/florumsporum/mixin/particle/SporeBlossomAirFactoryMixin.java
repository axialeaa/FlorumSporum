package com.axialeaa.florumsporum.mixin.particle;

import com.axialeaa.florumsporum.particle.FragileParticle;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SuspendedParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SuspendedParticle.SporeBlossomAirProvider.class)
public class SporeBlossomAirFactoryMixin {

    @Inject(method = "createParticle(Lnet/minecraft/core/particles/SimpleParticleType;Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/util/RandomSource;)Lnet/minecraft/client/particle/Particle;", at = @At("TAIL"))
    private void setDiscardOnCollision(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random, CallbackInfoReturnable<Particle> cir, @Local(name = "particle") SuspendedParticle particle) {
        FragileParticle.cast(particle).florum_sporum$setDiscardOnCollision(true);
    }

}
