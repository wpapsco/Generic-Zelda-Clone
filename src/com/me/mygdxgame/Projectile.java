package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Projectile extends Sprite {
	public int width;
	public int height;
	public Body body;
	
	public Vector2 vel;
	public Vector2 pos;
	public Texture texture;
	
	public Projectile(float x, float y) {
		texture = new Texture(Gdx.files.internal("data/BigBooty.png"));
		createBody(x, y);
		
		vel = body.getLinearVelocity();
		pos = body.getPosition();
	}
	
	public void createBody(float x, float y) {
		BodyDef def = new BodyDef();
		def.position.set(x * Values.PIXEL_BOX, y * Values.PIXEL_BOX);
		def.type = BodyType.DynamicBody;
		
		CircleShape circle = new CircleShape();
		circle.setRadius(4 * Values.PIXEL_BOX);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;
        fixtureDef.filter.groupIndex = -1;
		
		this.body = Values.world.createBody(def);
		this.body.createFixture(fixtureDef);
		
		circle.dispose();
	}
	
	
	
	@Override
	public void draw(SpriteBatch spriteBatch) {
		setX((this.body.getPosition().x * Values.BOX_PIXEL) - (texture.getWidth() / 2));
		setY((this.body.getPosition().y * Values.BOX_PIXEL) - (texture.getHeight() / 2));
		spriteBatch.draw(texture, this.getX(), this.getY());
	}

}
