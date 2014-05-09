package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Spawner extends WorldObject implements LoadedMapObject {

	private float interval;
	private Vector2 position;
	private GameScreen screen;
	private String type;
	private float count = 0;
	
	public Spawner(GameScreen context, Vector2 position, float interval, String type, TextureRegion region) {
		super(region, Spawner.createBody(position), true);
		//super(new Texture());
		this.interval = interval;
		this.position = position;
		this.screen = context;
		this.type = type;
	}
	
	public static Body createBody(Vector2 position) {
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		def.position.set(position.cpy().scl(Values.PIXEL_BOX));
		def.fixedRotation = true;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(16 * Values.PIXEL_BOX, 16 * Values.PIXEL_BOX);
		
		Body bod = Values.world.createBody(def);
		bod.createFixture(shape, 1f);
		
		return bod;
	}
	
	
	@Override
	public void update() {
		super.update();
		count += Gdx.graphics.getDeltaTime();
		if (count >= interval) {
			if (type.equals("NudeDude")) {
				NudeDude enemy = new NudeDude(screen, position);
				screen.add(enemy);
			}
			if (type.equals("Bat")) {
				Bat enemy = new Bat(screen, position);
				screen.add(enemy);
			}
			count = 0;
		}
	}
	
	@Override
	public LoadedMapObject init(MapObject object, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

}
