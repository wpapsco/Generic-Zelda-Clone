package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Bat extends Enemy {

	public Bat(GameScreen screen, Vector2 position) {
		super(Bat.createBody(position), true, screen, 5000, 2, "data/batw.png");
	}
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		super.create();
		sprite = new Sprite(new Texture(Gdx.files.internal("data/batw.png")));
	}
	
	public static Body createBody(Vector2 position) {
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(8 * Values.PIXEL_BOX, 8 * Values.PIXEL_BOX);
		
		BodyDef def = new BodyDef();
		def.position.set(position.cpy().scl(Values.PIXEL_BOX));
		def.fixedRotation = true;
		def.type =  BodyType.DynamicBody;
		
		Body body = Values.world.createBody(def);
		body.createFixture(shape, 1f);
		
		return body;
	}
}
