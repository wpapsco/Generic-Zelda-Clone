package com.me.mygdxgame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by William on 1/25/14.
 */
public abstract class Enemy extends NPC {
    protected float speed;
    public Enemy(Body body, boolean isFlammable, GameScreen screen, float idleDistance, float speed) {
        super(body, isFlammable, screen, idleDistance);
        this.speed = speed;
    }

    @Override
    public void follow(Player player) {
        Vector2 newVel = new Vector2();
        newVel.x = player.getPosition().x - this.getPosition().x;
        newVel.y = player.getPosition().y - this.getPosition().y;
        newVel.nor();
        newVel.scl(speed);
        body.setLinearVelocity(newVel);
    }

    @Override
    public void idle() {
        body.setLinearVelocity(Vector2.Zero);
    }
}
