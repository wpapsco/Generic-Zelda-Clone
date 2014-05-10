package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class DeadScreen implements Screen, ControllerListener {
	protected GZCGame game;
	protected Texture title = new Texture(Gdx.files.internal("data/dead.png"));;
	public DeadScreen(GZCGame game) {
		this.game = game;
		Controllers.addListener(this);
	}
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		game.batch.begin();
		game.batch.draw(title, 0, 0);
		game.batch.end();
	}

	@Override
	public boolean accelerometerMoved(Controller arg0, int arg1, Vector3 arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean axisMoved(Controller arg0, int arg1, float arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean buttonDown(Controller arg0, int arg1) {
		if (arg1 == Ouya.BUTTON_MENU) {
			StartScreen s = new StartScreen(game);
			game.setScreen(s);
			Controllers.removeListener(this);
		}
		return false;
	}

	@Override
	public boolean buttonUp(Controller arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void connected(Controller arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disconnected(Controller arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean povMoved(Controller arg0, int arg1, PovDirection arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
