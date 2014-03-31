package com.me.mygdxgame;

import com.badlogic.gdx.maps.MapObject;

public interface LoadedMapObject {
	LoadedMapObject init(MapObject object, Object context);
}
