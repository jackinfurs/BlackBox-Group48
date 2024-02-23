package com.blackbox.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Game;

public class BlackBox extends Game {
    // global SpriteBatch and BitmapFont instance variables
    // disposed at end of program;
    // PLEASE REMEMBER TO USE "game.batch()"
    public SpriteBatch batch;

    // create main menu screen at startup
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new MainMenuScreen(this));
    }

    // begin rendering
    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
    }
}