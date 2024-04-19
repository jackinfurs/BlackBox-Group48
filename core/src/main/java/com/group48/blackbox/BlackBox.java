package com.group48.blackbox;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
        
        this.setScreen(loadingScreen);
    }
    
    public void dispose()
    {
        System.out.println("\ndisposing...\nassets");
        assets.dispose();
        System.out.println("batch");
        batch.dispose();
        
        System.out.println("\ndisposing screens...\nLoadingScreen");
        loadingScreen.dispose();
        System.out.println("SplashScreen");
        splashScreen.dispose();
        
        System.out.println("MainMenuScreen");
        mainMenuScreen.dispose();
        System.out.println("SignInScreen");
        signInScreen.dispose();
        
        System.out.println("GameScreen");
        gameScreen.dispose();
        System.out.println("TutorialScreen");
        tutorialScreen.dispose();
        System.out.println("LeaderboardScreen");
        leaderboardScreen.dispose();
    }
    
    public void render()
    {
        // very important! nothing renders without this being called
        super.render();
    }
}
