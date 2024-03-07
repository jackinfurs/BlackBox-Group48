package com.group48.blackbox;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BlackBox extends Game {
    public SpriteBatch batch;
    public BitmapFont font;
    public OrthographicCamera camera;
    
    public void create()
    {
        batch = new SpriteBatch();
        font = new BitmapFont(); // use libGDX's default Arial font
        camera = new OrthographicCamera(800, 600);
        // TODO PLEASE CHANGE THIS BACK ONCE DONE
        this.setScreen(new MainMenuScreen(this));
//        this.setScreen(new GameScreen(this));
    }
    
    public void dispose()
    {
        batch.dispose();
        font.dispose();
    }
    
    public void render()
    {
        super.render();
    }
}
