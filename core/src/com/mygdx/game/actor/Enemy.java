package com.mygdx.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.MyGdxGame;

import java.util.*;

public class Enemy extends TankActor implements LifecycleListener {
    private TextureRegion region;
    private MyGdxGame game;
    Random random = new Random();
    List<Integer> directions = new ArrayList<>();
    private boolean gamePause = false;

    public Enemy(TextureRegion region, MyGdxGame game) {
        super(region, game);
        this.region = region;
        this.game = game;
        directions.add(Input.Keys.W);
        directions.add(Input.Keys.S);
        directions.add(Input.Keys.A);
        directions.add(Input.Keys.D);
        Gdx.app.addLifecycleListener(this);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        boolean isMoveing = false;
        if (getDirection() == Input.Keys.W) {
            if (game.isCanMove(this, Input.Keys.W)) {
                setY(getY() + getSpeed());
                setState(TANKSTATE_MOVING);
                isMoveing = true;
            }
            lookAt(Input.Keys.W);
        } else if (getDirection() == Input.Keys.S) {
            if (game.isCanMove(this, Input.Keys.S)) {
                setY(getY() - getSpeed());
                setState(TANKSTATE_MOVING);
                isMoveing = true;
            }
            lookAt(Input.Keys.S);
        } else if (getDirection() == Input.Keys.A) {
            if (game.isCanMove(this, Input.Keys.A)) {
                setX(getX() - getSpeed());
                setState(TANKSTATE_MOVING);
                isMoveing = true;
            }
            lookAt(Input.Keys.A);
        } else if (getDirection() == Input.Keys.D) {
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

    public void startAttack() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Ai();
            }
        }, 200, 1000);
    }

    private void pressKey(int keycode) {
        // 模拟按键
        if (keycode != getDirection()) {
            lookAt(keycode);
        }
    }

    private void fire() {
        fireBullet();
    }


    private void Ai() {
        if (gamePause) {
            return;
        }
        Player player = game.getPlayer();
        if (getState() == TANKSTATE_MOVING) {
            int i = random.nextInt(90);
            if (i > 50) {
                fire();
            }
            int i2 = random.nextInt(518);
            if (i2 < 230) {
                int i1 = random.nextInt(4);
                pressKey(directions.get(i1));
            } else {
                pressKey(getAttackDirection());
            }
        } else if (getState() == TANKSTATE_STOP) {
            int i = random.nextInt(200);
            if (i > 120) {
                fire();
            }
            int i2 = random.nextInt(300);
            if (i2 < 180) {
                int i1 = random.nextInt(4);
                pressKey(directions.get(i1));
            } else {
                pressKey(getAttackDirection());
            }
        }
        if (isFacePlayer(game.getPlayer())) {
            fire();
        }
    }

    private int getAttackDirection() {
        float x = getX();
        int i = random.nextInt(100);
        if (i > 40) {
            if (x < 320) {
                // 向右转
                return Input.Keys.D;
            } else {
                // 向左转
                return Input.Keys.A;
            }
        } else {
            return Input.Keys.S;
        }
    }

    private boolean isFacePlayer(Actor actor) {
        float x1 = getX();
        float y1 = getY();
        float x2 = actor.getX();
        float y2 = actor.getY();
        if (Math.abs(x1 - x2) < 16) {
            return true;
        } else if (Math.abs(y1 - y2) < 16) {
            return true;
        }
        return false;
    }

    @Override
    public void pause() {
        gamePause = true;
    }

    @Override
    public void resume() {
        gamePause = false;
    }

    @Override
    public void dispose() {

    }

}
