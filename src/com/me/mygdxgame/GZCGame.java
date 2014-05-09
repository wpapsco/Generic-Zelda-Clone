package com.me.mygdxgame;

import java.util.ArrayList;
import java.util.Hashtable;

import box2dLight.RayHandler;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GZCGame extends Game {
	private Screen startScreen;
	public OrthographicCamera camera;
	public OrthographicCamera lightCamera;
	public OrthographicCamera hudCamera;
	public SpriteBatch batch;
	public Hashtable<String, ArrayList[]> things;
    public static float scale = 2f;

	@Override
	public void create() {
		Texture.setEnforcePotImages(false);
		//camera positioning, scaling, etc
		camera = new OrthographicCamera(Gdx.graphics.getWidth() / scale, Gdx.graphics.getHeight() / scale);
		camera.position.x += camera.viewportWidth / 2;
		camera.position.y += camera.viewportHeight / 2;
		camera.update();
		things = new Hashtable<String, ArrayList[]>();
		lightCamera = new OrthographicCamera(camera.viewportWidth, camera.viewportHeight);
		lightCamera.zoom = 1f/30f;
		lightCamera.update();
		//hudCamera = new OrthographicCamera()
		batch = new SpriteBatch();
		startScreen = new StartScreen(this); //new UiTestScreen(this);
		
		this.setScreen(startScreen);
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		batch.dispose();
		Values.handler.dispose();
	}
	
	@Override
	public void setScreen(Screen screen) {
		// TODO Auto-generated method stub
		if (screen instanceof GameScreen) {
			
		}
		super.setScreen(screen);
	}
	
	public void setScreen(GameScreen screenTo, GameScreen screenFrom) {
		if (things.containsKey(screenTo.mapPath)) {
			screenTo.interactions = things.get(screenTo.mapPath)[0];
			screenTo.setDoors(things.get(screenTo.mapPath)[1]);
		}
		things.put(screenFrom.mapPath, new ArrayList[]{screenFrom.interactions, screenFrom.doors});
		super.setScreen(screenTo);
	}
}
