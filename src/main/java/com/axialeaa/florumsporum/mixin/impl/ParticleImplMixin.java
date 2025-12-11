package com.axialeaa.florumsporum.mixin.impl;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Particle.class)
public abstract class ParticleImplMixin {

    @Shadow @Final protected ClientLevel level;
    @Shadow
    public abstract AABB getBoundingBox();

    @Shadow
    public abstract void remove();

    @WrapMethod(method = "move(DDD)V")
    public void moveImpl(double dx, double dy, double dz, Operation<Void> original) {
        original.call(dx, dy, dz);
    }

}
