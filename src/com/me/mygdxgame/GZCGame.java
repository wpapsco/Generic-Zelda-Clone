package com.me.mygdxgame;

import box2dLight.RayHandler;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GZCGame extends Game {
	private GameScreen startScreen;
	public OrthographicCamera camera;
	public OrthographicCamera lightCamera;
	public SpriteBatch batch;

	@Override
	public void create() {
		Texture.setEnforcePotImages(false);
		Values.world = new World(new Vector2(), true);
		Values.handler = new RayHandler(Values.world);
		float scale = 2f;
		//camera positioning, scaling, etc
		camera = new OrthographicCamera(Gdx.graphics.getWidth() / scale, Gdx.graphics.getHeight() / scale);
		camera.position.x += camera.viewportWidth / 2;
		camera.position.y += camera.viewportHeight / 2;
		camera.update();
		lightCamera = new OrthographicCamera(camera.viewportWidth, camera.viewportHeight);
		lightCamera.zoom = 1f/30f;
		lightCamera.update();
		lightCamera.position.x += ((lightCamera.viewportWidth / 2) * Values.PIXEL_BOX);
		lightCamera.position.y += ((lightCamera.viewportHeight / 2) * Values.PIXEL_BOX);
		batch = new SpriteBatch();
		startScreen = new GameScreen(this);
		this.setScreen(startScreen);
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		batch.dispose();
		Values.handler.dispose();
	}
}
