package com.axialeaa.florumsporum.mixin.particle;

import com.axialeaa.florumsporum.mixin.impl.ParticleImplMixin;
import com.axialeaa.florumsporum.particle.FragileParticle;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.particle.SuspendedParticle;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SuspendedParticle.class)
public abstract class SuspendedParticleMixin extends ParticleImplMixin implements FragileParticle {

    @Unique private boolean discardOnCollision = false;

    @Override
    public void moveImpl(double dx, double dy, double dz, Operation<Void> original) {
        super.moveImpl(dx, dy, dz, original);

        if (!this.discardOnCollision)
            return;

        AABB box = this.getBoundingBox();

        if (this.level.collidesWithSuffocatingBlock(null, box))
            this.remove();
    }

    @Override
    public void florum_sporum$setDiscardOnCollision(boolean discardOnCollision) {
        this.discardOnCollision = discardOnCollision;
    }

}
