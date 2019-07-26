package com.mygdx.game.actor;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TankActor extends BaseActor {
    public static final int TANKSTATE_STOP = 0;
    public static final int TANKSTATE_MOVING = 1;
    private TextureRegion region;
    private int speed = 1;
    private int direction = Input.Keys.W;
    private int firePosition = 10;
    private MyGdxGame game;
    private int life = 50;
    /**
     * 0 stop;1 moving
     */
    private int state = 0;
    private int bulletActiveCount = 1;

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void hitted(Bullet bullet) {
        int damage = bullet.getDamage();
        this.life = this.life - damage;
        if (this.life <= 0) {
            die();
        }
    }

    public int getBulletActiveCount() {
        return bulletActiveCount;
    }

    public void setBulletActiveCount(int bulletActiveCount) {
        this.bulletActiveCount = bulletActiveCount;
    }

    public void increaseBulletActiveCount() {
        this.bulletActiveCount++;
    }

    public void decreaseBulletActiveCount() {
        this.bulletActiveCount--;
    }

    public boolean isDie() {
        return this.life <= 0;
    }

    public void die() {

    }

    public void lookAt(int direction) {
        setDirection(direction);
        switch (direction) {
            case Input.Keys.W:
                setRotation(0);
                break;
            case Input.Keys.S:
                setRotation(180);
                break;
            case Input.Keys.A:
                setRotation(90);
                break;
            case Input.Keys.D:
                setRotation(270);
                break;
        }
    }

    public TankActor(TextureRegion region, MyGdxGame game) {
        this.region = region;
        this.game = game;
        setSize(region.getRegionWidth(), region.getRegionHeight());
        setOrigin(getWidth() / 2, getHeight() / 2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (null == region || !isVisible()) {
            return;
        }

        batch.draw(region, getX(), getY(),
                getOriginX(), getOriginY(),
                getWidth(), getHeight(),
                getScaleX(), getScaleY(),
                getRotation());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public Vector2 getHeadPosition() {
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

    public void fireBullet() {
        if (getBulletActiveCount() < 1) {
            return;
        }
        Bullet bullet = game.pool.obtain();
        Vector2 headPosition = getHeadPosition();
        bullet.setCenterInPosition(headPosition.x, headPosition.y);
        bullet.setActive(true);
        bullet.setDirection(getDirection());
        bullet.setType(getBulletType());
        bullet.setTankActor(this);
        getStage().addActor(bullet);
        decreaseBulletActiveCount();
        game.runningBullet.add(bullet);
    }

    private String getBulletType() {
        switch (getType()) {
            case "Player":
                return Bullet.PLAYER_BULLET;
            case "Enemy":
                return Bullet.ENEMY_BULLET;
        }
        return "";
    }

    public void onHit() {
        // 给到礼物
    }
}
