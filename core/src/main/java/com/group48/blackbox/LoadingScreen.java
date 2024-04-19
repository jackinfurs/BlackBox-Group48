package com.group48.blackbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class LoadingScreen extends InputAdapter implements Screen {
    
    private final BlackBox game;
    
    private final ShapeRenderer shapeRenderer;
    private float progress;
    
    public LoadingScreen(final BlackBox game)
    {
        this.game = game;
        this.shapeRenderer = new ShapeRenderer();
    }
    
    private void queueAssets()
    {
        System.out.println("queueing assets...");
        
        game.assets.load("splash.png", Texture.class);
        game.assets.load("splash2.png", Texture.class);
        
        game.assets.load("MainMenuScreen/vaporBackground.png", Texture.class);
        game.assets.load("MainMenuScreen/exit.png", Texture.class);
        game.assets.load("MainMenuScreen/leaderboard.png", Texture.class);
        game.assets.load("MainMenuScreen/play.png", Texture.class);
        game.assets.load("MainMenuScreen/tutorial.png", Texture.class);
        
        game.assets.load("signinBackground.png", Texture.class);
        
        game.assets.load("Sound/clickBack.wav", Sound.class);
        game.assets.load("Sound/clickConfirm.wav", Sound.class);
        game.assets.load("Sound/clickHover.wav", Sound.class);
        game.assets.load("Sound/clickInvalid.wav", Sound.class);
        game.assets.load("Sound/gameEnd.wav", Sound.class);
        game.assets.load("Sound/gameStart.wav", Sound.class);
        game.assets.load("Sound/youSuck.wav", Sound.class);
        
        System.out.printf("successfully loaded %d assets\n", game.assets.getQueuedAssets());
        game.assets.finishLoading();
    }
    
    @Override
    public void show()
    {
        System.out.println("--- LOADING SCREEN ---");
        this.progress = 0f;
        queueAssets();
        shapeRenderer.setProjectionMatrix(game.camera.combined);
    }
    
    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        update(delta);
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(32, game.camera.viewportHeight / 2 - 8, game.camera.viewportWidth - 64, 16);
        
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(32, game.camera.viewportHeight / 2 - 8, progress * (game.camera.viewportWidth - 64), 16);
        shapeRenderer.end();
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
        shapeRenderer.dispose();
    }
    
    private void update(float delta)
    {
        progress = MathUtils.lerp(progress, game.assets.getProgress(), .1f);
        if (game.assets.update() && progress >= game.assets.getProgress() - .001f) {
            game.setScreen(game.splashScreen); // change start screen for debugging here
        }
    }
}
