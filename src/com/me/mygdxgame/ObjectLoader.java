package com.me.mygdxgame;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Hashtable;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class ObjectLoader {
	public Hashtable<Class<LoadedMapObject>, ArrayList<LoadedMapObject>> objects;
	
	public ObjectLoader(TiledMap map, Class<LoadedMapObject>[] classes, Object context) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		objects = new Hashtable<Class<LoadedMapObject>, ArrayList<LoadedMapObject>>();
        MapObjectFactory factory = new MapObjectFactory();
		for (MapLayer layer : map.getLayers()) {
			for (MapObject object : layer.getObjects()) {
				boolean instantiated = false;
				for (Class<LoadedMapObject> c : classes){
					if (object.getProperties().get("type") != null && object.getProperties().get("type").equals(c.getSimpleName())) {
						//LoadedMapObject loaded = c.getConstructor(MapObject.class, Object.class).newInstance(object, context);
                        LoadedMapObject loaded = (LoadedMapObject)
                                MapObjectFactory.class
                                .getMethod(c.getSimpleName(), MapObject.class, Object.class)
                                .invoke(factory, object, context);
						instantiated = true;
						if (objects.get(loaded.getClass()) != null) {
							objects.get(loaded.getClass()).add(loaded);
						} else {
							ArrayList<LoadedMapObject> list = new ArrayList<LoadedMapObject>();
							list.add(loaded);
							objects.put((Class<LoadedMapObject>) loaded.getClass(), list);
						}
					}
				}
				if (!instantiated) {
					System.err.println("Could not instantiate object with type \"" + object.getProperties().get("type") + "\"");
				}
			}
		}
	}
}
