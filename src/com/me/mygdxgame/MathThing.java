package com.me.mygdxgame;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by William on 1/17/14.
 */
public class MathThing {
    public static float getDistance(Vector2 point1, Vector2 point2) {
        return (float) Math.sqrt(Math.pow(point2.x - point1.x, 2) + Math.pow(point2.y - point1.y, 2));
    }
}
