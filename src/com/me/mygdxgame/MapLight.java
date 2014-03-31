package com.me.mygdxgame;

import box2dLight.Light;
import box2dLight.PointLight;
import com.badlogic.gdx.maps.MapObject;

/**
 * Created by William on 3/31/14.
 */
public class MapLight implements LoadedMapObject {
    PointLight light;
    public MapLight(PointLight light) {
        this.light = light;
    }
    @Override
    public LoadedMapObject init(MapObject object, Object context) {
        return null;
    }
}
