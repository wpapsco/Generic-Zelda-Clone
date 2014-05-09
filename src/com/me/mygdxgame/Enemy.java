package com.me.mygdxgame;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by William on 1/25/14.
 */
public abstract class Enemy extends NPC implements LoadedMapObject {
    protected float speed;
    protected float health = 100;
    protected float attackDam;
    public Enemy(Body body, boolean isFlammable, GameScreen screen, float idleDistance, float speed, String path) {
        super(body, isFlammable, screen, idleDistance, path);
        this.speed = speed;
    }

    @Override
    public void follow(Player player) {
        Vector2 newVel = new Vector2();
        newVel.x = (player.getPosition().x + (player.sprite.getWidth() / 2)) - this.getPosition().x;
        newVel.y = (player.getPosition().y + (player.sprite.getHeight() / 2)) - this.getPosition().y;
        newVel.nor();
        newVel.scl(speed);
        body.setLinearVelocity(newVel);
        if (newVel.x > 0) {
        	isRight = true;
        } else {
        	isRight = false;
        }
        if (newVel.y > 0) {
        	isUp = true;
        } else {
        	isUp = false;
        }
    }

    public void takeDamage(float damage, Vector2 pos) {
        health-=damage;
        if (health <= 0) {
            this.destroy();
        }
//        this.body.applyLinearImpulse(new Vector2(0, .2f), pos, true);
    }

    @Override
    public void idle() {
    	
    }

    @Override
    public LoadedMapObject init(MapObject object, Object context) {
        return null;
    }
}
