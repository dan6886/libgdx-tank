package com.mygdx.game.actor;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.awt.*;

public abstract class BaseActor extends Actor {

    private String type = "";

    public abstract void makeBody(World world);

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
