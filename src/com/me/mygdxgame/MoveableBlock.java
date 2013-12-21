package com.me.mygdxgame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class MoveableBlock extends Sprite {
	
	private Body body;
	private TextureRegion region;
	
	public MoveableBlock(TextureRegion region, Body body) {
		this.body = body;
		this.region = region;
		this.setRegion(region);
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {
		// TODO Auto-generated method stub
		region.getRegionWidth();
		this.setX((body.getPosition().x * Values.BOX_PIXEL) - (region.getRegionWidth() / 2));
		this.setY((body.getPosition().y * Values.BOX_PIXEL) - (region.getRegionHeight() / 2));
		spriteBatch.draw(region, this.getX(), this.getY());
	}
}
