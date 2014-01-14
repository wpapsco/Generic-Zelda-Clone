package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class HUD extends Sprite {
	protected Stage stage;
	public HUD(OrthographicCamera camera) {
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage.setCamera(camera);
		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		table.addActor(new Label("Clams: ", new LabelStyle(new BitmapFont(), Color.WHITE)));
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		//super.draw(spriteBatch);
		stage.act();
		stage.draw();
	}
}
