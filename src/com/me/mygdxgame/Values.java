package com.me.mygdxgame;

import box2dLight.RayHandler;

import com.badlogic.gdx.physics.box2d.World;

public class Values {

	public static World world;
	public static RayHandler handler;
	public static final float BOX_PIXEL = 30f;
	public static final float PIXEL_BOX = 1f/30f;
	
	public Values() throws Exception {
		throw new Exception("Do not instansiate the Values class!");
	}
}
