package com.me.mygdxgame;

import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by William on 1/10/14.
 */
public class GZCContactListener implements ContactListener {

    private GameScreen context;

    public GZCContactListener(GameScreen context) {
        this.context = context;
    }

    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();
        Object dataA = bodyA.getUserData();
        Object dataB = bodyB.getUserData();

        if (dataA instanceof WorldObject && dataB instanceof  WorldObject) {
            WorldObject worldDataA = (WorldObject) dataA;
            WorldObject worldDataB = (WorldObject) dataB;
            if (worldDataA.isFlaming && !worldDataB.isFlaming && worldDataB.isFlammable) {
                worldDataB.setFlaming(true);
            }
            if (worldDataB.isFlaming && !worldDataA.isFlaming && worldDataA.isFlammable) {
                worldDataA.setFlaming(true);
            }
            /*if (worldDataA instanceof Projectile) {
                if (((Projectile) worldDataA).destroyOnContact) {
                    ((Projectile) worldDataA).destroy();
                }
            }
            if (worldDataB instanceof Projectile) {
                if (((Projectile) worldDataB).destroyOnContact) {
                    ((Projectile) worldDataB).destroy();
                }
            }*/
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
