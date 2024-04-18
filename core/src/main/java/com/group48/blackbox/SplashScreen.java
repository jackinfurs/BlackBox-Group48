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
    private Stage stage;
    
    private Image splash;
    private Texture splashTexture;
    private float time;
    
    public SplashScreen(final BlackBox game)
    {
        this.game = game;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), game.camera));
        
    }
    
    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        update(delta);
        
        game.batch.begin();
        
        stage.draw();
        
        // wish i knew a better way to do this.
        time += Gdx.graphics.getDeltaTime();
        if (time > 3) game.setScreen(new MainMenuScreen(game));
        
        game.batch.end();
    }
    
    @Override
    public void show()
    {
        System.out.println("--- SPLASH SCREEN ---");
        Gdx.input.setInputProcessor(stage);
        time = 0;
        
        System.out.println("displaying splash screen...");
        splashTexture = game.assets.get("splash.png");
        //        splashTexture = game.assets.get("splash2.png");
        splash = new Image(splashTexture);
        splash.setPosition(stage.getWidth() / 2 - 100, stage.getHeight() / 2f - 75);
        //                splash.setPosition(stage.getWidth() / 2 - 200, stage.getHeight() / 2 - 150);
        stage.addActor(splash);
        stage.addAction(sequence(
                sequence(alpha(0f), fadeIn(0.5f),
                        delay(2f),
                        fadeOut(0.5f))));
    }
    
    public void update(float delta)
    {
        stage.act(delta);
    }
    
    @Override
    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height, false);
    }
    
    @Override
    public void hide()
    {
        dispose();
    }
    
    @Override
    public void dispose()
    {
        stage.dispose();
    }
    
    @Override
    public void pause()
    {
    
    }
    
    @Override
    public void resume()
    {
    
    }
}
