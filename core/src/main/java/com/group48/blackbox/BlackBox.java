package com.group48.blackbox;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.group48.blackbox.Screens.*;

/**
 * "Master" class for project.
 * <p>
 * Most classes will have an instance of {@code BlackBox} passed to it in the constructor (named {@code game} by convention).
 * This allows the calling of: <ul>
 * <li>{@code AssetManager assets} (see {@link LoadingScreen})
 * <li>{@code SpriteBatch batch} (for texture rendering)
 * <li>{@code OrthographicCamera camera} (for Screen viewing, positioning etc.)
 * </ul>
 * <p>
 * Upon game start, all Screens are cached for memory management.
 * Generally speaking, this class is only modified when a new Screen is created.
 * <p>
 * render <b>must</b> call {@code super.render()}, otherwise no Screen renders.
 *
 * @author Jack Dunne 22483576
 * @see LoadingScreen
 * @since Sprint 1
 */

public class BlackBox extends Game {
    
    public AssetManager assets;
    public SpriteBatch batch;
    public OrthographicCamera camera;
    
    public LoadingScreen loadingScreen;
    public SplashScreen splashScreen;
    public MainMenuScreen mainMenuScreen;
    public GameScreen gameScreen;
    public TutorialScreen tutorialScreen;
    public LeaderboardScreen leaderboardScreen;
    public SignInScreen signInScreen;
    
    public void create()
    {
        assets = new AssetManager(); // see LoadingScreen.java
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        
        // init screens for later calling
        // this saves on memory and makes setting screens more concise
        loadingScreen = new LoadingScreen(this);
        splashScreen = new SplashScreen(this);
        mainMenuScreen = new MainMenuScreen(this);
        signInScreen = new SignInScreen(this);
        gameScreen = new GameScreen(this);
        tutorialScreen = new TutorialScreen(this);
        leaderboardScreen = new LeaderboardScreen(this);
        
        System.out.println("Welcome to Black Box+!");
        
        this.setScreen(loadingScreen); // don't change the starting screen here.
    }
    
    public void dispose()
    {
        assets.dispose();
        batch.dispose();
        
        loadingScreen.dispose();
        splashScreen.dispose();
        mainMenuScreen.dispose();
        signInScreen.dispose();
        gameScreen.dispose();
        tutorialScreen.dispose();
        leaderboardScreen.dispose();
    }
    
    public void render()
    {
        // very important! nothing renders without this being called
        super.render();
    }
}
