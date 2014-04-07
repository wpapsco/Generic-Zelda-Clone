package com.me.mygdxgame;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by William on 4/3/14.
 */
public class Hole implements LoadedMapObject {
    Rectangle rectangle;
    public Hole(Rectangle rectangle) {
        this.rectangle = rectangle;
    }
    @Override
    public LoadedMapObject init(MapObject object, Object context) {
        return null;
    }
}
