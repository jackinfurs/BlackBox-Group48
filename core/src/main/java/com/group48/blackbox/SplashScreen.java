package com.group48.blackbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class SplashScreen extends InputAdapter implements Screen {
    
    final BlackBox game;
    private final Stage stage;
    
    private Image splash;
    private Texture splashTexture;
    
    public SplashScreen(final BlackBox game)
    {
        this.game = game;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), game.camera));
    }
    
    @Override
    public void show()
    {
        System.out.println("\n--- SPLASH SCREEN ---");
        Gdx.input.setInputProcessor(stage);
        
        System.out.println("displaying splash screen...");
        splashTexture = game.assets.get("splash2.png");
        splash = new Image(splashTexture);
        splash.setPosition(stage.getWidth() / 2 - 200, stage.getHeight() / 2 - 150);
        stage.addActor(splash);
        
        Runnable fade = () -> game.setScreen(game.mainMenuScreen);
        
        stage.addAction(sequence(
                sequence(alpha(0f), fadeIn(0.5f),
                        delay(2f),
                        fadeOut(0.5f),
                        run(fade))));
    }
    
    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        update(delta);
        
        game.batch.begin();
        stage.draw();
        game.batch.end();
    }
    
    @Override
    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height, false);
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
        stage.dispose();
    }
    
    public void update(float delta)
    {
        stage.act(delta);
    }
}
