package com.me.mygdxgame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class HUD extends Sprite {
	protected Stage stage;
	protected Table table;
	protected Label label;
	protected Image image;
	protected ArrayList<Image> images;
	protected Texture heartTexture;
	protected Texture deadHeartTexture;
	public int coin;
	
	public HUD(OrthographicCamera camera) {
		coin = 0;
		images = new ArrayList<Image>();
		heartTexture = new Texture(Gdx.files.internal("data/heart.png"));
		deadHeartTexture = new Texture(Gdx.files.internal("data/heartdead.png"));
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage.setCamera(camera);
		table = new Table();
		table.setFillParent(true);
		table.setBounds(-180, -100, 50, 50);
		label = new Label("Clams: " + coin, new LabelStyle(new BitmapFont(), Color.WHITE));
		image = new Image(heartTexture);
		image.setBounds(0, 200, 10, 10);
		
		stage.addActor(table);
		table.addActor(label);
		
		for(int i = 0; i < 3; i++) {
			int x = 0;
			images.add(image);
			image.setBounds(x, 200, 10, 10);
			x += 15;
			table.addActor(images.get(i));
		}
		
		//label = new Label("Clams: ", new LabelStyle(new BitmapFont(), Color.WHITE));
		//label.setX(100);
		//label.setY(100);
		//table.addActor(image);
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		label.setText("Clams: " + coin);
		stage.act();
		stage.draw();
	}
	
	public void setCoin(int coins) {
		coin = coins;
	}
	
	
}
