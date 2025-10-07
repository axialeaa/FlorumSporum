package com.axialeaa.florumsporum.mixin.particle;

import com.axialeaa.florumsporum.mixin.impl.ParticleImplMixin;
import com.axialeaa.florumsporum.particle.FragileParticle;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.particle.WaterSuspendParticle;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WaterSuspendParticle.class)
public abstract class WaterSuspendParticleMixin extends ParticleImplMixin implements FragileParticle {

    @Unique private boolean discardOnCollision = false;

    @Override
    public void moveImpl(double dx, double dy, double dz, Operation<Void> original) {
        super.moveImpl(dx, dy, dz, original);

        if (!this.discardOnCollision)
            return;

        Box box = this.getBoundingBox();

        if (this.world.canCollide(null, box))
            this.markDead();
    }

    @Override
    public void setDiscardOnCollision(boolean discardOnCollision) {
        this.discardOnCollision = discardOnCollision;
    }

}
