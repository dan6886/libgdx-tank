package com.mygdx.game.actor;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;


public class Bullet extends BaseActor {
    private int speed = 2;
    private TextureRegion region;
    private int direction;
    private boolean isActive = false;
    public static final String ENEMY_BULLET = "enemy_bullet";
    public static final String PLAYER_BULLET = "player_bullet";
    public static final int BULLET_LEVEL1 = 10;
    public static final int BULLET_LEVEL2 = 20;
    private int damage = BULLET_LEVEL2;
    private TankActor tankActor;

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

    public void recycle() {
        if (isActive()) {
            setActive(false);
            getTankActor().increaseBulletActiveCount();
        }
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setCenterInPosition(float x, float y) {
        setPosition(x - getWidth() / 2, y - getHeight() / 2);
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int strength) {
        this.damage = strength;
    }

    public TankActor getTankActor() {
        return tankActor;
    }

    public void setTankActor(TankActor tankActor) {
        this.tankActor = tankActor;
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
    public Rectangle getRectangle() {
        final int changetTo = 18;
        int extend = (int) (changetTo - getWidth()) / 2;
        Rectangle rectangle = new Rectangle((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
        switch (direction) {
            case Input.Keys.W:
            case Input.Keys.S:
                rectangle.set(rectangle.x - extend, rectangle.y, changetTo, rectangle.height);
                break;
            case Input.Keys.A:
            case Input.Keys.D:
                rectangle.set(rectangle.x, rectangle.y - extend, rectangle.width, changetTo);
                break;
        }
        return rectangle;
    }
}
