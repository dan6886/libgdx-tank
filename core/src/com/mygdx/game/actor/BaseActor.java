package com.mygdx.game.actor;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;


public abstract class BaseActor extends Actor {

    private String type = "";


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void buildUserData() {

    }

    public Rectangle getRectangle() {
        return new Rectangle((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
    }
}
