package com.mygdx.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.UserData;

import java.util.ArrayList;
import java.util.List;

public class Player extends BaseActor implements Disposable, InputProcessor {

    private TextureRegion region;
    private int speed = 1;
    private int direction = Input.Keys.W;
    List<Integer> noPassDirection = new ArrayList<Integer>();
    private MyGdxGame game;
    private Body body;

    public Player(TextureRegion region, MyGdxGame game) {
        super();
        setSize(region.getRegionWidth(), region.getRegionHeight());
        this.region = region;
        this.game = game;
        Gdx.input.setInputProcessor(this);
        setType("Player");
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        boolean isPressedUp = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean isPressedDown = Gdx.input.isKeyPressed(Input.Keys.S);
        boolean isPressedLeft = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean isPressedRight = Gdx.input.isKeyPressed(Input.Keys.D);
        if (isPressedUp && !noPassDirection.contains(Input.Keys.W)) {
            setY(getY() + speed);
            direction = Input.Keys.W;
        } else if (isPressedDown && !noPassDirection.contains(Input.Keys.S)) {
            setY(getY() - speed);
            direction = Input.Keys.S;
        } else if (isPressedLeft && !noPassDirection.contains(Input.Keys.A)) {
            setX(getX() - speed);
            direction = Input.Keys.A;
        } else if (isPressedRight && !noPassDirection.contains(Input.Keys.D)) {
            setX(getX() + speed);
            direction = Input.Keys.D;
        }
    }

    public void setForbidden(int direction) {
        if (!noPassDirection.contains(direction)) {
            noPassDirection.add(direction);
        }
    }

    public void setPass(int direction) {
        if (noPassDirection.contains(direction)) {
            noPassDirection.remove(Integer.valueOf(direction));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (null == region || !isVisible()) {
            return;
        }
        batch.draw(region, getX(), getY());
        body.setTransform(getX() + region.getRegionWidth() / 2, getY() + region.getRegionHeight() / 2, 0);

    }

    @Override
    public void dispose() {

    }

    @Override
    public void makeBody(World world) {

        BodyDef bodyGroundDef = new BodyDef();
        // 只有动态的才能碰撞
        bodyGroundDef.type = BodyDef.BodyType.DynamicBody;
        bodyGroundDef.position.set(getX(), getY());
        bodyGroundDef.awake = true;

        body = world.createBody(bodyGroundDef);
        PolygonShape shapeGround = new PolygonShape();
        shapeGround.setAsBox(region.getRegionWidth() / 2, getHeight() / 2);
        body.createFixture(shapeGround, 1);
        body.setUserData(new UserData.Builder().setName(getType()).append("obj", this).build());
    }

    @Override
    public boolean keyDown(int keycode) {
//        System.out.println("keydown" + keycode);
        switch (keycode) {
            case 19:
                setY(getY() + 16);
                break;
            case 20:
                setY(getY() - 16);
                break;
            case 21:
                setX(getX() - 16);
                break;
            case 22:
                setX(getX() + 16);
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
