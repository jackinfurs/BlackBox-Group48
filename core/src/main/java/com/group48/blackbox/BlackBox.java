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
        
        System.out.println("Welcome to Black Box+!");
        
        this.setScreen(loadingScreen);
//        this.setScreen(gameScreen); // DEBUG
    }
    
    public void dispose()
    {
//        System.out.println("\ndisposing...\nassets"); // DEBUG
        assets.dispose();
//        System.out.println("batch"); // DEBUG
        batch.dispose();
        
//        System.out.println("\ndisposing screens...\nLoadingScreen"); // DEBUG
        loadingScreen.dispose();
//        System.out.println("SplashScreen"); // DEBUG
        splashScreen.dispose();
        
//        System.out.println("MainMenuScreen"); // DEBUG
        mainMenuScreen.dispose();
//        System.out.println("SignInScreen"); // DEBUG
        signInScreen.dispose();
        
//        System.out.println("GameScreen"); // DEBUG
        gameScreen.dispose();
//        System.out.println("TutorialScreen"); // DEBUG
        tutorialScreen.dispose();
//        System.out.println("LeaderboardScreen"); // DEBUG
        leaderboardScreen.dispose();
    }
    
    public void render()
    {
        // very important! nothing renders without this being called
        super.render();
    }
}
