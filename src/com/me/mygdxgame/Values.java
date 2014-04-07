package com.me.mygdxgame;

import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class Values {

	public static World world;
	public static RayHandler handler;
    public static Body groundBody;
    public static ParticleEffectPool flamingThingsPool;
    public static ParticleEffectPool fireWandPool;
    public static Color fireColor = new Color(1f, .31f, 0f, 1f);
	public static final float BOX_PIXEL = 30f;
	public static final float PIXEL_BOX = 1f/30f;
    public static final ArrayList<Body> bodiesToDelete = new ArrayList<Body>();
	
	public Values() throws Exception {
		throw new Exception("Do not instansiate the Vaules class!");
	}
}
