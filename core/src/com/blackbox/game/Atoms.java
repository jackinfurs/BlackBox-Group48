package com.blackbox.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Atoms extends Actor {
    private final float radius = 5;

    public void Atom(float x, float y) {
        setPosition(x, y);
        setSize(radius * 2, radius * 2);
    }


    public void draw(ShapeRenderer shapeRenderer, float parentAlpha) {
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.circle(getX() + radius, getY() + radius, radius);
    }
}