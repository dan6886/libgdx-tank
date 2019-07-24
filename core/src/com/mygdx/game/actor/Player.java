package com.mygdx.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.MyGdxGame;

import java.util.ArrayList;
import java.util.List;

public class Player extends BaseActor implements Disposable, InputProcessor {

    private TextureRegion region;
    private int speed = 1;
    private int direction = Input.Keys.W;
    List<Integer> noPassDirection = new ArrayList<Integer>();
    private MyGdxGame game;
    private int firePosition = 10;

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
        if (isPressedUp && game.isCanMove(this, Input.Keys.W)) {
            setY(getY() + speed);
            direction = Input.Keys.W;
        } else if (isPressedDown && game.isCanMove(this, Input.Keys.S)) {
            setY(getY() - speed);
            direction = Input.Keys.S;
        } else if (isPressedLeft && game.isCanMove(this, Input.Keys.A)) {
            setX(getX() - speed);
            direction = Input.Keys.A;
        } else if (isPressedRight && game.isCanMove(this, Input.Keys.D)) {
            setX(getX() + speed);
            direction = Input.Keys.D;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (null == region || !isVisible()) {
            return;
        }
        batch.draw(region, getX(), getY());

    }

    @Override
    public void dispose() {

    }

    @Override
    public void makeBody(World world) {
    }

    public void fire() {
        Bullet bullet = game.pool.obtain();
        Vector2 headPosition = getHeadPosition();
        bullet.setCenterInPosition(headPosition.x, headPosition.y);
        bullet.setActive(true);
        bullet.setDirection(direction);
        bullet.setType(Bullet.ENMEY_BULLET);
        this.getStage().addActor(bullet);
        game.runningBullet.add(bullet);
    }

    private Vector2 getHeadPosition() {
        Vector2 v = new Vector2();
        switch (direction) {
            case Input.Keys.W:
                v.x = getX() + getWidth() / 2;
                v.y = getY() + getHeight() + firePosition;
                break;
            case Input.Keys.S:
                v.x = getX() + getWidth() / 2;
                v.y = getY() + -firePosition;
                break;
            case Input.Keys.A:
                v.x = getX() - firePosition;
                v.y = getY() + getHeight() / 2;
                break;
            case Input.Keys.D:
                v.x = getX() + getWidth() + firePosition;
                v.y = getY() + getHeight() / 2;
                break;
        }
        return v;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.SPACE:
                fire();
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
