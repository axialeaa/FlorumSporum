package com.axialeaa.florumsporum.mixin.particle;

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

    @Shadow public abstract AABB getBoundingBox();
    @Shadow public abstract void remove();

    @Shadow @Final protected ClientLevel level;

    @WrapMethod(method = "move(DDD)V")
    public void moveImpl(double xa, double ya, double za, Operation<Void> original) {
        original.call(xa, ya, za);
    }

}
