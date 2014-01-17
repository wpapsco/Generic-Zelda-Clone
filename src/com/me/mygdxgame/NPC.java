package com.me.mygdxgame;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by William on 1/17/14.
 */
public abstract class NPC extends WorldObject {
    private GameScreen screen;
    private float idleDistance;

    public NPC(Body body, boolean isFlammable, GameScreen screen, float idleDistance) {
        super(body, isFlammable);
        this.screen = screen;
        this.idleDistance = idleDistance;
    }

    public abstract void idle();

    @Override
    public void update() {
        super.update();
        for (Player player : screen.getPlayers()) {
            if (MathThing.getDistance(player.getPosition(), this.getPosition()) > idleDistance) {
                idle();
            }
        }
    }
}

