package com.axialeaa.florumsporum.particle;

/**
 * An interface with a single method used for setting the instance variable responsible for killing a particle when it collides with a block.
 */
public interface FragileParticle {

    void setDiscardOnCollision(boolean discardOnCollision);

}
