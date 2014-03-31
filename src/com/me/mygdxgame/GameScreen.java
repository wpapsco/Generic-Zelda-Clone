package com.me.mygdxgame;

import static box2dLight.RayHandler.useDiffuseLight;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen, InputProcessor {

	protected GZCGame game;
	protected ArrayList<WorldObject> sprites;
	protected OrthogonalTiledMapRenderer renderer;
	protected TiledMap map;
	protected ArrayList<Body> bodyWalls;
	protected ArrayList<FlickeringLight> lights;
    protected ArrayList<MapObject> interactions;
	protected PointLight light;
	protected MapObjects objects;
	protected ArrayList<Player> players;
    protected ArrayList<Door> doors;
	protected HUD pHud;
	public int pNum;
	protected Music sound;
	
	public GameScreen(GZCGame game, String mapLocation, int playerNum) {
		sound = Gdx.audio.newMusic(Gdx.files.internal("data/fireballnoise.mp3"));
		sound.setLooping(true);
		sound.play();
		Gdx.input.setInputProcessor(this);
		pNum = playerNum;
		this.game = game;
        makeParticleEffects();
		bodyWalls = new ArrayList<Body>();
		sprites = new ArrayList<WorldObject>();
		lights = new ArrayList<FlickeringLight>();
        players = new ArrayList<Player>();
        interactions = new ArrayList<MapObject>();
        doors = new ArrayList<Door>();
		objects = new MapObjects();
		TmxMapLoader loader = new TmxMapLoader();
		map = loader.load(mapLocation);
		renderer = new OrthogonalTiledMapRenderer(map, game.batch);
		renderer.setView(game.camera);
		createBox2dWorld(true);
		String[] vals;
		if (map.getProperties().get("LightColor") != null) {
			vals = map.getProperties().get("LightColor").toString().split(",");
		} else {
			vals = new String[]{"0", "0", "0", "1"};
		}
		if (map.getProperties().get("AmbientLight") != null) {
			Values.handler.setAmbientLight(Float.parseFloat(map.getProperties().get("AmbientLight").toString()));
		} else {
			Values.handler.setAmbientLight(0);
		}
		light = new PointLight(Values.handler, 300, new Color(Float.parseFloat(vals[0]), Float.parseFloat(vals[1]), Float.parseFloat(vals[2]), Float.parseFloat(vals[3])), 200 * Values.PIXEL_BOX, 320 * Values.PIXEL_BOX, 320 * Values.PIXEL_BOX);
		light.setSoft(true);
		players.add(new Player(
                Math.round(Float.parseFloat(map.getProperties().get("StartX").toString()) * Integer.parseInt(map.getProperties().get("TileWidth").toString())),
                Math.round(Float.parseFloat(map.getProperties().get("StartY").toString()) * Integer.parseInt(map.getProperties().get("TileHeight").toString())),
                game.camera, game.lightCamera, false, "data/Bully_Sheet.png"
        ));
        OrthographicCamera camera = new OrthographicCamera(game.camera.viewportWidth, game.camera.viewportHeight);
        camera.view.set(game.camera.view);
        OrthographicCamera lightCamera = new OrthographicCamera(game.lightCamera.viewportWidth, game.lightCamera.viewportHeight);
        lightCamera.view.set(game.lightCamera.view);
        lightCamera.zoom = game.lightCamera.zoom;
        
        if (pNum == 2) {
	        players.add(new Player(
                    Math.round(Float.parseFloat(map.getProperties().get("StartX").toString()) * Integer.parseInt(map.getProperties().get("TileWidth").toString())),
                    Math.round(Float.parseFloat(map.getProperties().get("StartY").toString()) * Integer.parseInt(map.getProperties().get("TileHeight").toString())),
                    camera, lightCamera, true, "data/Base_Sheet.png"
            ));
        }
        ObjectLoader l;
        try {
            l = new ObjectLoader(map, new Class[]{Door.class, MapLight.class}, this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        light.attachToBody(players.get(0).body, 0, 0);

        sprites.add(new NudeDude(this, new Vector2(90, 100)));
	}

    private void makeParticleEffects() {
        //the effect for the fire wand
        ParticleEffect fireEffect = new ParticleEffect();
        fireEffect.load(Gdx.files.internal("data/fire_wand.p"), Gdx.files.internal("data/"));
        Values.fireWandPool = new ParticleEffectPool(fireEffect, 1, 10);

        //the effect for flaming objects
        ParticleEffect flamingThingsEffect = new ParticleEffect();
        flamingThingsEffect.load(Gdx.files.internal("data/square_fire.p"), Gdx.files.internal("data/"));
        Values.flamingThingsPool = new ParticleEffectPool(flamingThingsEffect, 1, 10);
    }

    private void createBox2dWorld(boolean test) {
		Values.world = new World(new Vector2(), true);
		Values.handler = new RayHandler(Values.world);
        Light.setContactFilter((short) 0, (short) 0, (short) 3);
        useDiffuseLight(true);
        Values.world.setContactListener(new GZCContactListener(this));
		for (int i = 0; i < map.getLayers().getCount(); i++) {
			if (map.getLayers().get(i) instanceof TiledMapTileLayer) {
				TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(i);
				boolean shadow;
				boolean collide;
				boolean movable;
				if (layer.getProperties().get("casts_shadow") != null) {
					shadow = Boolean.parseBoolean(layer.getProperties().get("casts_shadow").toString());
				} else {
					shadow = false;
				}
                collide = layer.getProperties().get("collide") != null ? Boolean.parseBoolean(layer.getProperties().get("collide").toString()) : false;
                movable = layer.getProperties().get("moveable") != null && Boolean.parseBoolean(layer.getProperties().get("moveable").toString());
				if (shadow || collide) {
					for (int x = 0; x < layer.getWidth(); x++) {
						for (int y = 0; y < layer.getHeight(); y++) {
							if (layer.getCell(x, y) != null) {
								createWallCube(x, y, layer.getTileWidth(), layer.getTileHeight(), collide, movable, layer.getCell(x, y).getTile().getTextureRegion());
							}
						}
					}
				}
			} else {
				MapObjects objects = map.getLayers().get(i).getObjects();
				for (int j = 0; j < objects.getCount(); j++) {
                    if (objects.get(j).getProperties().get("type").equals("interaction")) {
                        interactions.add(objects.get(j));
                    }
					this.objects.add(objects.get(j));
					if (objects.get(j).getProperties().get("enabled") == null) {
						objects.get(j).getProperties().put("enabled", "true");
					}
				}
			}
		}
	}
	
	private void createWallCube(int x, int y, float tileWidth, float tileHeight, boolean collide, boolean moveable, TextureRegion region) {
		BodyDef def = new BodyDef();
		def.position.x = ((x * tileWidth) + (tileWidth / 2)) * Values.PIXEL_BOX;
		def.position.y = ((y * tileHeight) + (tileHeight / 2)) * Values.PIXEL_BOX; //THIS MIGHT CAUSE A PROBLEM (INVERTED Y)
		def.fixedRotation = true;
		
		PolygonShape shape = new PolygonShape();
		
		FixtureDef fixDef = new FixtureDef();
		if (!collide) {
			fixDef.filter.maskBits = 0;
		}
		fixDef.shape = shape;
		fixDef.density = 0;
		
		Body bod;
		
		if (!moveable) {
			shape.setAsBox(((tileWidth) / 2) * Values.PIXEL_BOX, ((tileHeight) / 2) * Values.PIXEL_BOX);
			def.type = BodyType.StaticBody;
			fixDef.density = 0;
			bod = Values.world.createBody(def);
		} else {
			float boxWidth = (tileWidth - 1) * Values.PIXEL_BOX;
			float boxHeight = (tileHeight - 1) * Values.PIXEL_BOX;
			float percentTruncation = .2f;
			Vector2[] points = new Vector2[] {
					//making a truncated square
				    new Vector2(-(boxWidth / 2) + (boxWidth * percentTruncation), boxHeight / 2),     //top left
					new Vector2((boxWidth / 2) - (boxWidth * percentTruncation), boxHeight / 2),      //top right
					new Vector2(boxWidth / 2, (boxHeight / 2) - (boxHeight * percentTruncation)),     //right top
					new Vector2(boxWidth / 2, -(boxHeight / 2) + (boxHeight * percentTruncation)),    //right bottom
					new Vector2(-(boxWidth / 2) + (boxWidth * percentTruncation), -(boxHeight / 2)),  //bottom left
					new Vector2((boxWidth / 2) - (boxWidth * percentTruncation), -(boxHeight / 2)),   //bottom right
					new Vector2(-(boxWidth / 2), (boxHeight / 2) - (boxHeight * percentTruncation)),  //right top
					new Vector2(-(boxWidth / 2), -(boxHeight / 2) + (boxHeight * percentTruncation)), //right bottom
			};
			shape.set(points);
			def.type = BodyType.DynamicBody;
            def.linearDamping = .7f;
			fixDef.density = 1;
			bod = Values.world.createBody(def);
            add(new MovableBlock(region, bod));
		}
		
		bod.createFixture(fixDef);
		bodyWalls.add(bod);
	}

    public void renderWorld(Player player) {
        game.batch.setProjectionMatrix(player.camera.combined);
        renderer.setView(player.camera);
        for (int i = 0; i < map.getLayers().getCount(); i++) {
            if (map.getLayers().get(i).getName().equals("Characters")) {
                game.batch.begin();
                for (Door door : doors) {
                    door.draw(game.batch);
                }
                for (Player ply : players) {
                    ply.draw(game.batch);
                }
                for (int j = 0; j < sprites.size(); j++) {
                    sprites.get(j).draw(game.batch);
                }
                if(players.get(0).xDown && (TimeUtils.nanoTime() - players.get(0).shotTime > 500000000 && player.getCoin() > 0)) {
                    players.get(0).createProjectile();
                    player.addCoin(-10);
                }
                game.batch.end();
                renderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(i));
            } else {
                MapLayer layer = map.getLayers().get(i);
                if (layer instanceof TiledMapTileLayer) {
                    if (layer.getProperties().get("moveable") == null || !Boolean.parseBoolean(layer.getProperties().get("moveable").toString())) {
                        game.batch.begin();
                        renderer.renderTileLayer((TiledMapTileLayer) layer);
                        game.batch.end();
                    }
                }
                game.batch.begin();
                player.hud.draw(game.batch);
                game.batch.end();
            }
            if (map.getLayers().get(i).getName().equals("Lights")) {
                Values.handler.setCombinedMatrix(player.lightCamera.combined, player.lightCamera.position.x, player.lightCamera.position.y, player.lightCamera.viewportWidth, player.lightCamera.viewportHeight);
                Values.handler.render();
            }
        }
    }

	@Override
	public void render(float delta) {
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
		for (int i = 0; i < lights.size(); i++) {
			lights.get(i).updateToFlicker();
		}
        for (Door door : doors) {
            door.update();
        }
        for (WorldObject object : sprites) {
            object.update();
        }
        for (Body body : Values.bodiesToDelete) {
            Values.world.destroyBody(body);
        }
        Values.bodiesToDelete.clear();
        for (Player player : players) {
            player.update();
        }
        Values.world.step(Gdx.graphics.getDeltaTime(), 10, 10);
        changeViewportAndRender();
        Values.handler.update();
	}

    private void changeViewportAndRender() {
        for (Player player : players) {
            player.camera.update();
            player.lightCamera.update();
        }
        if (players.size() == 1) {
            players.get(0).camera.viewportWidth = Gdx.graphics.getWidth() / GZCGame.scale;
            players.get(0).lightCamera.viewportWidth = Gdx.graphics.getWidth() / GZCGame.scale;
            Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            renderWorld(players.get(0));
        } else if (players.size() == 2) {
            players.get(0).camera.viewportWidth = (Gdx.graphics.getWidth() / GZCGame.scale) / 2;
            players.get(0).lightCamera.viewportWidth = (Gdx.graphics.getWidth() / GZCGame.scale) / 2;
            Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
            renderWorld(players.get(0));
            players.get(1).camera.viewportWidth = (Gdx.graphics.getWidth() / GZCGame.scale) / 2;
            players.get(1).lightCamera.viewportWidth = (Gdx.graphics.getWidth() / GZCGame.scale) / 2;
            Gdx.gl.glViewport(Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
            renderWorld(players.get(1));
        } else if (players.size() == 3) {

        } else if (players.size() == 4) {

        }
    }

    public void onInteraction(Player player) {
		//oh god why what do not touch this
		//holy shit what the fuck
        for (int i = 0; i < objects.getCount(); i++) {
            if (objects.get(i) instanceof RectangleMapObject &&
                ((RectangleMapObject) objects.get(i)).getRectangle().contains(player.getSprite().getX(), player.getSprite().getY())) {
                RectangleMapObject rect = (RectangleMapObject) objects.get(i);
                if (rect.getRectangle().overlaps(player.getSprite().getBoundingRectangle()) &&
                    rect.getProperties().get("interaction_type").equals("player")) {
                    doProperties(player, rect);
                }
            }
        }
    }

    private void doProperties(MapObject object) {
        for (Player player : players) {
            doProperties(player, object);

            //TODO: FIX THIS, CAN'T CALL TOGGLE DOOR MORE THAN ONCE!!
        }
    }

    private void doProperties(Player player, MapObject object) {
        for (int j = 0; j < objects.getCount(); j++) {
            int order = 0;
            if (object.getProperties().get("call_order") != null) {
                order = Integer.parseInt(object.getProperties().get("call_order").toString());
            }
            if (order == j) {
                if (object.getProperties().get("enabled").equals("true")) {
                    if (object.getProperties().get("display_text") != null) {
                        System.out.println(object.getProperties().get("display_text").toString());
                        player.hud.setDialog(object.getProperties().get("display_text").toString());
                    }
                    if (object.getProperties().get("enable_object") != null) {
                        objects.get(object.getProperties().get("enable_object").toString()).getProperties().put("enabled", "true");
                    }
                    if (object.getProperties().get("disable_object") != null) {
                        objects.get(object.getProperties().get("disable_object").toString()).getProperties().put("enabled", "false");
                    }
                    if (object.getProperties().get("add_coin") != null) {
                        player.addCoin(Integer.parseInt(object.getProperties().get("add_coin").toString()));
                    }
                    if (object.getProperties().get("change_map") != null) {
                        game.setScreen(new GameScreen(this.game, object.getProperties().get("change_map").toString(), pNum));
                    }
                    if (object.getProperties().get("open_door") != null) {
                        for (Door door : doors) {
                            if (door.name.equals(object.getProperties().get("open_door"))) {
                                door.open();
                            }
                        }
                    }
                    if (object.getProperties().get("close_door") != null) {
                        for (Door door : doors) {
                            if (door.name.equals(object.getProperties().get("close_door"))) {
                                door.close();
                            }
                        }
                    }
                    if (object.getProperties().get("toggle_door") != null) {
                        for (Door door : doors) {
                            if (door.name.equals(object.getProperties().get("toggle_door"))) {
                                door.toggle();
                            }
                        }
                    }
                }
            }
        }
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
        renderer.dispose();
    }

    public void add(WorldObject sprite) {
        sprites.add(sprite);
    }

    @Override
    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
        if (keycode == Keys.SPACE) {
            onInteraction(players.get(0));
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        lights.add(new FlickeringLight(Values.handler, 300, light, 1f, .01f));
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}
