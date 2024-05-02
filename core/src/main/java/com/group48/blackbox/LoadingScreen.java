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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class LoadingScreen extends InputAdapter implements Screen {
    
    private final BlackBox game;
    private final ShapeRenderer loadingBar;
    // loading bar
    private float progress;
    
    public LoadingScreen(final BlackBox game)
    {
        this.game = game;
        this.loadingBar = new ShapeRenderer();
    }
    
    // saves memory to queue all assets into one global (NON-STATIC) AssetManager
    // called using game.assets.get("insert/file/path");
    private void queueAssets()
    {
        game.assets.load("splash2.png", Texture.class);
        
        game.assets.load("MainMenuScreen/vaporBackground.png", Texture.class);
        game.assets.load("signinBackground.png", Texture.class);
        game.assets.load("MainMenuScreen/play.png", Texture.class);
        game.assets.load("MainMenuScreen/tutorial.png", Texture.class);
        game.assets.load("MainMenuScreen/leaderboard.png", Texture.class);
        game.assets.load("MainMenuScreen/exit.png", Texture.class);
        
        game.assets.load("GameScreen/circle.png", Texture.class);
        
        game.assets.load("uiskin.json", Skin.class);
        
        game.assets.load("Sound/clickBack.wav", Sound.class);
        game.assets.load("Sound/clickConfirm.wav", Sound.class);
        game.assets.load("Sound/clickHover.wav", Sound.class);
        game.assets.load("Sound/clickInvalid.wav", Sound.class);
        game.assets.load("Sound/gameEnd.wav", Sound.class);
        game.assets.load("Sound/gameStart.wav", Sound.class);
        game.assets.load("Sound/yousuck.wav", Sound.class);
        
//        System.out.printf("successfully loaded %d assets\n", game.assets.getQueuedAssets()); // DEBUG
        game.assets.finishLoading(); // synchronous
    }
    
    // show() methods are usually used for initialisation; called when screen comes into focus
    @Override
    public void show()
    {
        System.out.println("\n--- LOADING SCREEN ---\n");
        this.progress = 0f;
        
        System.out.println("Loading...");
        queueAssets();
        System.out.println("Finished loading");
        
        loadingBar.setProjectionMatrix(game.camera.combined);
    }
    
    // render() methods are called every frame
    @Override
    public void render(float delta)
    {
        // clear screen boilerplate
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        // update (in this case the progress bar) per item loaded per frame
        update(delta);
        
        // loading bar background
        loadingBar.begin(ShapeRenderer.ShapeType.Filled);
        loadingBar.setColor(Color.DARK_GRAY);
        loadingBar.rect(32, game.camera.viewportHeight / 2 - 8, game.camera.viewportWidth - 64, 16);
        
        // loading bar foreground
        loadingBar.setColor(Color.WHITE);
        loadingBar.rect(32, game.camera.viewportHeight / 2 - 8, progress * (game.camera.viewportWidth - 64), 16);
        loadingBar.end();
    }
    
    // below methods are fairly useless in this context
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
    
    // used when exiting; marks for garbage collection
    @Override
    public void dispose()
    {
        loadingBar.dispose();
    }
    
    private void update(float delta)
    {
        progress = MathUtils.lerp(progress, game.assets.getProgress(), .1f);
        if (game.assets.update() && progress >= game.assets.getProgress() - .001f) {
            // once loading is done, load a screen (default splashScreen)
            // change start screen for debugging here
                        game.setScreen(game.splashScreen);
//            game.setScreen(game.gameScreen);
        }
    }
}
