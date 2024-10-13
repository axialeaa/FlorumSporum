package com.axialeaa.florumsporum.mixin;

import com.axialeaa.florumsporum.mixin.accessor.ParticleAccessor;
import com.axialeaa.florumsporum.mixin.impl.ParticleImplMixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.WaterSuspendParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

//? if >1.17.1
import com.google.common.collect.Iterables;

import /*$ particle_group_import >>*/ net.minecraft.particle.ParticleGroup ;

@Mixin(WaterSuspendParticle.class)
public class WaterSuspendParticleMixin extends ParticleImplMixin {

    @Unique private final Particle thisParticle = Particle.class.cast(this);

    @Override
    public void moveImpl(double dx, double dy, double dz, Operation<Void> original) {
        super.moveImpl(dx, dy, dz, original);

        Optional<ParticleGroup> optional = thisParticle.getGroup();

        if (optional.isEmpty() || optional.get() != ParticleGroup.SPORE_BLOSSOM_AIR)
            return;

        ClientWorld world = ((ParticleAccessor) thisParticle).getWorld();
        Box boundingBox = thisParticle.getBoundingBox();

        var collisions = world.getBlockCollisions(null, boundingBox);

        //? if >1.17.1 {
        if (!Iterables.isEmpty(collisions))
        //?} else {
        /*if (!collisions.toList().isEmpty())
        *///?}
            thisParticle.markDead();
    }

}
