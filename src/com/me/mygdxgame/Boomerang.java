package com.me.mygdxgame;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by William on 2/4/14.
 */
public class Boomerang extends WorldObject {
    public Boomerang(Body body, boolean isFlammable) {
        super(body, isFlammable);
    }
}
