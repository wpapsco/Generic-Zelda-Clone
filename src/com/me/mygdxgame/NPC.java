package com.me.mygdxgame;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by William on 1/17/14.
 */
public abstract class NPC extends WorldObject {
    public NPC(Body body, boolean isFlammable) {
        super(body, isFlammable);
    }
}
