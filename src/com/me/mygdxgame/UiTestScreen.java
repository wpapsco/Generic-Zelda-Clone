package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

public class UiTestScreen implements Screen {
	
	private Stage stage;
	private GZCGame game;
	protected int pNum = 1;
	
	public UiTestScreen(GZCGame game) {
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        this.game = game;
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = new BitmapFont();
		style.downFontColor = new Color(1, 0, 0, 1);  
		style.fontColor = new Color(1, 1, 1, 1);
		TextButton playersButton = new TextButton("2Play Boolean", style);
		playersButton.addListener(new ContextualChangeListener(game, pNum) {
			@Override                                 
			public void changed(ChangeEvent event, Actor actor) {
				if (pNum == 1) {
					pNum++;
				} else if (pNum == 2) {
					pNum--;
				}
			}
		});
		table.add(playersButton);
		table.row();
		
		generateLevelButtons(table, new String[]{"data/testMap1.tmx", "data/testMap2.tmx", "data/testMap3.tmx", "data/testMap4.tmx", "5", "6", "7", "8", "9", "10"}, new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});
	}
	
	private void generateLevelButtons(Table table, String[] levels, String[] buttonTitles) {
		TextButton.TextButtonStyle style =  new TextButton.TextButtonStyle();
		style.font = new BitmapFont();
		style.downFontColor = new Color(1, 0, 0, 1);  
		style.fontColor = new Color(1, 1, 1, 1);      
		
		TextButton[] buttons = new TextButton[levels.length];
		
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new TextButton(buttonTitles[i], style);
			buttons[i].addListener(new ContextualChangeListener(game, levels[i]) {
				@Override                                 
				public void changed(ChangeEvent event, Actor actor) {
					game.setScreen(new GameScreen(game, (String) information, pNum)); 
				}
			});
			table.add(buttons[i]);
			table.row();
		}
	}
	
	@Override                             
	public void render(float delta) {     
		// TODO Auto-generated method stub
		stage.act();                      
		stage.draw();                     
		Table.drawDebug(stage);           
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
		stage.dispose();
	}
}
