package com.group48.blackbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class TutorialScreen implements Screen {
    
    final BlackBox game;
    private Stage stage;
    private GameBoard tiledMap;
    
    public TutorialScreen(final BlackBox game)
    {
        this.game = game;
        //        Gdx.input.setInputProcessor(new InputHandler());
    }
    
    @Override
    public void show()
    {
        stage = new Stage(new ScreenViewport());
        tiledMap = new GameBoard();
    }
    
    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);
        game.camera.update();
        
        stage.act(delta);
        stage.draw();
        
        tiledMap.getRenderer().render();
        
        tiledMap.getRenderer().setView(game.camera);
        game.batch.setProjectionMatrix(game.camera.combined);
        
        tiledMap.getRenderer().render();
    }
    
    @Override
    public void resize(int i, int i1)
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
        stage.dispose();
        tiledMap.dispose();
    }
}
