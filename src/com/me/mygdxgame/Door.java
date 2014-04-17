package com.me.mygdxgame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * Created by William on 2/3/14.
 */
public class Door extends WorldObject implements LoadedMapObject {
    private TextureRegion openRegion;
    private TextureRegion closedRegion;
    private Vector2 position;
    private Vector2 size;
    private boolean isClosed;
    public String name;

    public Door(TextureRegion openRegion, TextureRegion closedRegion, boolean startsOpen, Vector2 position, Vector2 size, String name) {
        super(closedRegion, Door.makeBody(position, size), false);
        this.openRegion = openRegion;
        this.closedRegion = closedRegion;
        this.position = position;
        this.size = size;
        this.name = name;
        this.sprite.setRegion(closedRegion);
        isClosed = true;
        position.set(position.x + (size.x / 2), position.y + (size.y / 2));
        if (startsOpen) {this.open();}
    }
    
    public Door(Door door) {
    	this(new TextureRegion(door.openRegion), new TextureRegion(door.closedRegion), !door.isClosed, new Vector2(door.position.x - (door.size.x / 2), door.position.y - (door.size.y / 2)), door.size, door.name);
    }

    private static Body makeBody(Vector2 position, Vector2 size) {
        BodyDef def = new BodyDef();
        def.position.set(position.x + (size.x / 2), position.y + (size.y / 2));
        def.type = BodyDef.BodyType.StaticBody;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x / 2, size.y / 2);

        Body bod = Values.world.createBody(def);
        bod.createFixture(shape, 1);

        return bod;
    }

    public void open() {
        isClosed = false;
        this.body.setTransform(new Vector2(-1, -1), 0);
        this.sprite.setRegion(openRegion);
    }

    public void close() {
        isClosed = true;
        this.body.setTransform(position, 0);
        this.sprite.setRegion(closedRegion);
    }

    public void toggle() {
        if (isClosed) {
            open();
        } else {
            close();
        }
    }

    @Override
    public void update() {
        super.update();
        this.sprite.setPosition((position.x - (size.x / 2)) * Values.BOX_PIXEL, (position.y - (size.y / 2)) * Values.BOX_PIXEL);
    }

    @Override
    public LoadedMapObject init(MapObject object, Object context) {

        return null;
    }
}
