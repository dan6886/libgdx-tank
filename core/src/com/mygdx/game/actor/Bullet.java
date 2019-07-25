package com.mygdx.game.actor;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

public class Bullet extends BaseActor {
    private int speed = 1;
    private TextureRegion region;
    private int direction;
    private boolean isActive = false;
    public static final String ENEMY_BULLET = "enemy_bullet";
    public static final String PLAYER_BULLET = "player_bullet";
    public static final int BULLET_LEVEL1 = 1;
    public static final int BULLET_LEVEL2 = 2;
    private int strength = 1;

    public Bullet(TextureRegion region) {
        setSize(region.getRegionWidth(), region.getRegionHeight());
        this.region = region;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setCenterInPosition(float x, float y) {
        setPosition(x - getWidth() / 2, y - getHeight() / 2);
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (null == region || !isVisible() || !isActive) {
            return;
        }
        batch.draw(region, getX(), getY());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (isActive) {
            switch (direction) {
                case Input.Keys.W:
                    setY(getY() + speed);
                    break;
                case Input.Keys.S:
                    setY(getY() - speed);
                    break;
                case Input.Keys.A:
                    setX(getX() - speed);
                    break;
                case Input.Keys.D:
                    setX(getX() + speed);
                    break;
            }
        }

    }

    @Override
    public void makeBody(World world) {

    }
}
