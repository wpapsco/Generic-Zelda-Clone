package com.me.mygdxgame;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;

public class BossLocation implements LoadedMapObject {

	public Vector2 location;
	public float direction;
	
	public BossLocation(MapObject object, Object context) {
		direction = 0f;
		RectangleMapObject rect = (RectangleMapObject) object;
		location = new Vector2(rect.getRectangle().x, rect.getRectangle().y);
		String[] args = new String[]{"1", "0"};
		if (object.getProperties().containsKey("direction")) {
			direction = Float.parseFloat(object.getProperties().get("direction").toString());
		}
		
	}
	
	@Override
	public LoadedMapObject init(MapObject object, Object context) {
		// TODO Auto-generated method stub
		return null;
	}
}
