package com.mygdx.game.actor.tank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.actor.bonus.BaseBonus;

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
        setType("Player");
        InputMultiplexer inputProcessor = (InputMultiplexer) Gdx.input.getInputProcessor();
        inputProcessor.addProcessor(this);
        setDirection(Input.Keys.W);
        setBulletActiveCount(20);
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
                isMoveing = true;
            }
            lookAt(Input.Keys.W);
        } else if (isPressedDown) {
            if (game.isCanMove(this, Input.Keys.S)) {
                setY(getY() - getSpeed());
                isMoveing = true;
            }
            lookAt(Input.Keys.S);
        } else if (isPressedLeft) {
            if (game.isCanMove(this, Input.Keys.A)) {
                setX(getX() - getSpeed());
                isMoveing = true;
            }
            lookAt(Input.Keys.A);
        } else if (isPressedRight) {
            if (game.isCanMove(this, Input.Keys.D)) {
                setX(getX() + getSpeed());
                isMoveing = true;
            }
            lookAt(Input.Keys.D);
        }
        if (isMoveing) {
            if (getState() == TANKSTATE_STOP) {
                setState(TANKSTATE_MOVING);
            }
        } else {
            if (getState() == TANKSTATE_MOVING) {
//                adjustStopPosition();
                setState(TANKSTATE_STOP);
            }
        }

    }

    private void adjustStopPosition() {
        float x = (int) ((getX() + getWidth()) / getWidth()) * getWidth();
        float y = (int) ((getY() + getHeight()) / getHeight()) * getHeight();
        setPosition(x, y);
        System.out.println(getX() + "|" + getY());
    }

    private void startMove() {
        MoveByAction action = Actions.moveBy(0, 0, 0.05F);
        switch (getDirection()) {
            case Input.Keys.W:
                action.setAmountY(8);
                break;
            case Input.Keys.S:
                action.setAmountY(-8);
                break;
            case Input.Keys.A:
                action.setAmountX(-8);
                break;
            case Input.Keys.D:
                action.setAmountX(8);
                break;
        }
        sequenceAction.addAction(Actions.run(before));
        sequenceAction.addAction(action);
        sequenceAction.addAction(Actions.run(after));
        addAction(sequenceAction);
    }

    SequenceAction sequenceAction = Actions.sequence();
    private Runnable before = new Runnable() {
        @Override
        public void run() {
            if (getState() == TANKSTATE_STOP) {
                removeAction(sequenceAction);
            }
        }
    };

    private Runnable after = new Runnable() {
        @Override
        public void run() {
            removeAction(sequenceAction);
            startMove();
        }
    };

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    public void dispose() {
        InputMultiplexer inputProcessor = (InputMultiplexer) Gdx.input.getInputProcessor();
        inputProcessor.removeProcessor(this);
    }

    @Override
    public void onHit() {
        super.onHit();
    }

    @Override
    public void onBonus(BaseBonus bonus) {
        bonus.applyToPlayer(this);
        System.out.println("礼物碰撞了" + bonus.toString());
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.SPACE:
                System.out.println("发射");
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
