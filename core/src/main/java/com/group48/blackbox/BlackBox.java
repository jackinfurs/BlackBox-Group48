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
        this.setScreen(new MainMenuScreen(this));
        //        this.setScreen(new TutorialScreen(this));
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
