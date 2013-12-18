package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Player extends Sprite {
	
	protected TextureRegion[] frameRegions;
	protected Animation nAnimation;
	protected Animation sAnimation;
	protected Animation eAnimation;
	protected Animation wAnimation;
	protected Animation currentAnim;
	public int width;
	public int height;
	public Body body;
	protected float stateTime;
	
	public Player(int x, int y) {
		width = 8;
		height = 13;
		createBody(x, y, width, height);
		TextureRegion[][] tRegions = Sprite.split(new Texture(Gdx.files.local("data/mage.png")), width, height);
		frameRegions = new TextureRegion[tRegions.length * tRegions[0].length];
		int index = 0;
		for (int i = 0; i < tRegions.length; i++) {
			for (int j = 0; j < tRegions[i].length; j++) {
				frameRegions[index++] = tRegions[i][j];
			}
		}
		float animSpeed = .2f;
		nAnimation = new Animation(animSpeed, frameRegions[4], frameRegions[5], frameRegions[6], frameRegions[7]);
		sAnimation = new Animation(animSpeed, frameRegions[0], frameRegions[1], frameRegions[2], frameRegions[3]);
		eAnimation = new Animation(animSpeed, frameRegions[8], frameRegions[9], frameRegions[10], frameRegions[11]);
		wAnimation = new Animation(animSpeed, frameRegions[12], frameRegions[13], frameRegions[14], frameRegions[15]);
		nAnimation.setPlayMode(Animation.LOOP);
		sAnimation.setPlayMode(Animation.LOOP);
		eAnimation.setPlayMode(Animation.LOOP);
		wAnimation.setPlayMode(Animation.LOOP);
		currentAnim = nAnimation;
		stateTime = 0;
	}
	
	private void createBody(int x, int y, int tWidth, int tHeight) {
		BodyDef def = new BodyDef();
		def.fixedRotation = true;
		def.position.set(x * Values.PIXEL_BOX, y * Values.PIXEL_BOX);
		def.type = BodyType.DynamicBody;
		
		//creates a diamond shape for collision
		PolygonShape shape = new PolygonShape();
		Vector2[] verts = new Vector2[4];
		float boxW = tWidth * Values.PIXEL_BOX;
		float boxH = tHeight * Values.PIXEL_BOX;
		verts[0] = new Vector2(0, boxH / 2);
		verts[1] = new Vector2(boxW / 2, 0);
		verts[2] = new Vector2(0, -(boxH / 2));
		verts[3] = new Vector2(-(boxW / 2), 0);
		shape.set(verts);
		
		//for a box, use this
		//shape.setAsBox((tWidth / 2) * Values.PIXEL_BOX, (tHeight / 2) * Values.PIXEL_BOX);
		
		this.body = Values.world.createBody(def);
		this.body.createFixture(shape, 1);
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		float speed = 3f;
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			currentAnim = nAnimation;
			this.body.setLinearVelocity(new Vector2(0, speed));
			stateTime += Gdx.graphics.getDeltaTime();
		} else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			currentAnim = sAnimation;
			this.body.setLinearVelocity(new Vector2(0, -speed));
			stateTime += Gdx.graphics.getDeltaTime();
		} else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			currentAnim = wAnimation;
			this.body.setLinearVelocity(new Vector2(-speed, 0));
			stateTime += Gdx.graphics.getDeltaTime();
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			this.body.setLinearVelocity(new Vector2(speed, 0));
			currentAnim = eAnimation;
			stateTime += Gdx.graphics.getDeltaTime();
		} else {
			this.body.setLinearVelocity(0, 0);
		}
		setX((this.body.getPosition().x * Values.BOX_PIXEL) - (width / 2));
		setY((this.body.getPosition().y * Values.BOX_PIXEL) - (height / 2));
		spriteBatch.draw(currentAnim.getKeyFrame(stateTime), this.getX(), this.getY());
	}
}
