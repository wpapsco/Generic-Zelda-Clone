package com.me.mygdxgame;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.physics.box2d.Body;

public class MovableBlock extends WorldObject {

	private Body body;
	private TextureRegion region;
    private ParticleEffect effect;
    private boolean flaming = false;
    private PointLight flameLight;

	public MovableBlock(TextureRegion region, Body body) {
        super(region, body, true);
		this.body = body;
		this.region = region;
        //this.setTexture(region.getTexture());
	}

    @Override
	public void draw(SpriteBatch spriteBatch) {
        super.draw(spriteBatch);
//		this.setX((body.getPosition().x * Values.BOX_PIXEL) - (region.getRegionWidth() / 2));
//		this.setY((body.getPosition().y * Values.BOX_PIXEL) - (region.getRegionHeight() / 2));
        //effect.setPosition(body.getPosition().x * Values.BOX_PIXEL, body.getPosition().y * Values.BOX_PIXEL);
//		spriteBatch.draw(region, this.getX(), this.getY());
	}

    @Override
    public void create() {
        super.create();
    }
}
