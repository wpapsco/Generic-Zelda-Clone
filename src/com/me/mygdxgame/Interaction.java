package com.me.mygdxgame;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by William on 4/1/14.
 */
public class Interaction implements LoadedMapObject {
    public MapProperties properties;
    public Rectangle rectangle;
    public String name;

    public Interaction(MapProperties properties, Rectangle rectangle, String name) {
        this.properties = properties;
        this.rectangle = rectangle;
        this.name = name;
    }

    @Override
    public LoadedMapObject init(MapObject object, Object context) {
        return null;
    }
}
