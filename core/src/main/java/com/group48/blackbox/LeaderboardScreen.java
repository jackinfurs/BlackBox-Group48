package com.group48.blackbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class LeaderboardScreen extends InputAdapter implements Screen {
    
    final BlackBox game;
    //    private final Stage stage;
    private final Texture backgroundTexture;
    
    public LeaderboardScreen(final BlackBox game)
    {
        //        stage = new Stage(new ScreenViewport(), game.batch);
        //        Gdx.input.setInputProcessor(stage);
        this.game = game;
        
        //        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        
        backgroundTexture = new Texture("signinBackground.png");
    }
    
    @Override
    public void show()
    {
    
    }
    
    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        game.batch.begin();
        
        // Draw background
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        game.batch.end();
        
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            System.out.println("ESC button clicked!");
            dispose();
            game.setScreen(new MainMenuScreen(game));
        }
        
    }
    
    @Override
    public void resize(int width, int height)
    {
    
    }
    
    @Override
    public void pause()
    {
    
    }
    
    @Override
    public void resume()
    {
    
    }
    
    @Override
    public void hide()
    {
    
    }
    
    @Override
    public void dispose()
    {
        //        stage.dispose();
        backgroundTexture.dispose();
    }
}
