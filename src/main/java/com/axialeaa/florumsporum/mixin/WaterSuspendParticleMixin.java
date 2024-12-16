package com.axialeaa.florumsporum.mixin;

import com.axialeaa.florumsporum.mixin.accessor.ParticleAccessor;
import com.axialeaa.florumsporum.mixin.impl.ParticleImplMixin;
import com.axialeaa.florumsporum.util.FragileParticle;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.WaterSuspendParticle;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WaterSuspendParticle.class)
public abstract class WaterSuspendParticleMixin extends ParticleImplMixin implements FragileParticle {

    @Unique private boolean discardOnCollision = false;
    @Unique private final Particle asParticle = (Particle) (Object) this;

    @Override
    public void moveImpl(double dx, double dy, double dz, Operation<Void> original) {
        if (!this.discardOnCollision)
            return;

        Box box = this.asParticle.getBoundingBox();

        if (((ParticleAccessor) this.asParticle).getWorld().canCollide(null, box))
            this.asParticle.markDead();

        super.moveImpl(dx, dy, dz, original);
    }

    @Override
    public void setDiscardOnCollision(boolean discardOnCollision) {
        this.discardOnCollision = discardOnCollision;
    }

}
