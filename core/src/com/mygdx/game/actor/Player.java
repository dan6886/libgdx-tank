package com.mygdx.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.MyGdxGame;

import java.util.ArrayList;
import java.util.List;

public class Player extends TankActor implements Disposable, InputProcessor {

    private TextureRegion region;

    List<Integer> noPassDirection = new ArrayList<Integer>();
    private MyGdxGame game;
    private int firePosition = 10;

    public Player(TextureRegion region, MyGdxGame game) {
        super(region, game);
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
        boolean isMoveing = false;
        if (isPressedUp) {
            if (game.isCanMove(this, Input.Keys.W)) {
                setY(getY() + getSpeed());
                setState(TANKSTATE_MOVING);
                isMoveing = true;
            }
            lookAt(Input.Keys.W);
        } else if (isPressedDown) {
            if (game.isCanMove(this, Input.Keys.S)) {
                setY(getY() - getSpeed());
                setState(TANKSTATE_MOVING);
                isMoveing = true;
            }
            lookAt(Input.Keys.S);
        } else if (isPressedLeft) {
            if (game.isCanMove(this, Input.Keys.A)) {
                setX(getX() - getSpeed());
                setState(TANKSTATE_MOVING);
                isMoveing = true;
            }
            lookAt(Input.Keys.A);
        } else if (isPressedRight) {
            if (game.isCanMove(this, Input.Keys.D)) {
                setX(getX() + getSpeed());
                setState(TANKSTATE_MOVING);
                isMoveing = true;
            }
            lookAt(Input.Keys.D);
        }
        if (!isMoveing) {
            setState(TANKSTATE_STOP);
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    public void dispose() {

    }


    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.SPACE:
                fireBullet();
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
