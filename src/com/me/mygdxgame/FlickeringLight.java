package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class FlickeringLight extends PointLight {

	private float origDist;
	private float varyAmount;
	private float varyFreq;
	private float tracker;
	
	public FlickeringLight(RayHandler rayHandler, int rays, PointLight light, float varyAmount, float varyFreq) {
		super(rayHandler, rays, light.getColor(), light.getDistance(), light.getX(), light.getY());
		origDist = light.getDistance();
		this.varyAmount = varyAmount;
		this.varyFreq = varyFreq;
		tracker = 0;
	}
	
	public void updateToFlicker() {
		if (tracker >= varyFreq) {
			this.setDistance((float) (origDist + ((Math.random() - .5f) * varyAmount)));
			tracker = 0;
		} else {
			tracker += Gdx.graphics.getDeltaTime();
		}
	}
}
