package com.group48.blackbox;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BlackBox extends Game {
    
    public OrthographicCamera camera;
    public SpriteBatch batch;
    public BitmapFont font;
    
    public void create()
    {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, +800, 600);
        batch = new SpriteBatch();
        font = new BitmapFont(); // use libGDX's default Arial font
        this.setScreen(new SplashScreen(this));
        //        this.setScreen(new TutorialScreen(this));
    }
    
    public void dispose()
    {
        batch.dispose();
        font.dispose();
        this.getScreen().dispose();
    }
    
    public void render()
    {
        super.render();
    }
}
