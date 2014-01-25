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

    public abstract void follow(Player player);

    @Override
    public void update() {
        super.update();
        for (Player player : screen.getPlayers()) {
            float distance = MathThing.getDistance(player.getPosition(), this.getPosition());
            if (distance > idleDistance) {
                idle();
            } else {
                follow(player);
            }
        }
    }
}

