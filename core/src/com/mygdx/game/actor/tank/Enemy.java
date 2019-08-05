package com.mygdx.game.actor.tank;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.MyGdxGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemy extends TankActor {
    private TextureRegion region;
    private MyGdxGame game;
    Random random = new Random();
    List<Integer> directions = new ArrayList<>();
    private boolean gamePause = false;
    private Timer timer = new Timer();
    public static final int ENEMY_TYPE1 = 10;
    public static final int ENEMY_TYPE2 = 11;
    public static final int ENEMY_TYPE3 = 12;
    /**
     * 特殊敌人可以产生礼物
     */
    private boolean isBonus = false;

    public Enemy(String type, TextureRegion region, MyGdxGame game) {
        super(region, game);
        setType(type);
        this.region = region;
        this.game = game;
        directions.add(Input.Keys.W);
        directions.add(Input.Keys.S);
        directions.add(Input.Keys.A);
        directions.add(Input.Keys.D);
        setDirection(Input.Keys.S);
        setBulletActiveCount(1);
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

    public boolean isBonus() {
        return isBonus;
    }

    public void setBonus(boolean bonus) {
        isBonus = bonus;
    }

    public void startAttack() {
        int i = random.nextInt(3);
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                Ai();
            }
        }, 2, 2 + i);

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
//            return;
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
    public void die() {
        timer.clear();
        this.remove();
    }

    @Override
    public void onHit() {
        if (isBonus()) {
            // 产生礼物,调用game的刷新管理器生成礼物
            game.spawnBonus();
            isBonus = false;

        }

    }
}
