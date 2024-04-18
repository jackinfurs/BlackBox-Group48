package com.group48.blackbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class MainMenuScreen implements Screen {
    final BlackBox game;
    private Stage stage;
    
    private Image background;
    private Texture backgroundTexture;
    
    public MainMenuScreen(final BlackBox game)
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
        
        game.batch.end();
    }
    
    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(stage);
        
        backgroundTexture = game.assets.get("MainMenuScreen/vaporBackground.png");
        background = new Image(backgroundTexture);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // sequence; one after the other. parallel; both at the same time.
        // forever is always called and will keep being called
        stage.addAction(sequence(alpha(0f), fadeIn(0.5f)));
        stage.addActor(background);
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
