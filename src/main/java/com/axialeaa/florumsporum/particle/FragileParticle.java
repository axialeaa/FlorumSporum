package com.axialeaa.florumsporum.particle;

import net.minecraft.client.particle.Particle;

public interface FragileParticle {

    void florum_sporum$setDiscardOnCollision(boolean discardOnCollision);

    @SuppressWarnings("CastToIncompatibleInterface")
    static <T extends Particle> FragileParticle cast(T particle) {
        return (FragileParticle) particle;
    }

}
