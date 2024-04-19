package com.group48.blackbox;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BlackBox extends Game {
    
    public AssetManager assets;
    public SpriteBatch batch;
    public OrthographicCamera camera;
    public BitmapFont font;
    
    public LoadingScreen loadingScreen;
    public SplashScreen splashScreen;
    public MainMenuScreen mainMenuScreen;
    public GameScreen gameScreen;
    public TutorialScreen tutorialScreen;
    public LeaderboardScreen leaderboardScreen;
    public SignInScreen signInScreen;
    
    public void create()
    {
        assets = new AssetManager();
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, +800, 600);
        font = new BitmapFont(); // use libGDX's default Arial font
        
        loadingScreen = new LoadingScreen(this);
        splashScreen = new SplashScreen(this);
        mainMenuScreen = new MainMenuScreen(this);
        signInScreen = new SignInScreen(this);
        gameScreen = new GameScreen(this);
        tutorialScreen = new TutorialScreen(this);
        //        leaderboardScreen = new LeaderboardScreen(this);
        
        this.setScreen(loadingScreen);
    }
    
    public void dispose()
    {
        System.out.println("\ndisposing...\nassets");
        assets.dispose();
        System.out.println("batch");
        batch.dispose();
        System.out.println("font");
        font.dispose();
        System.out.println("loadingScreen");
        loadingScreen.dispose();
        System.out.println("splashScreen");
        splashScreen.dispose();
        System.out.println("mainmenu");
        mainMenuScreen.dispose();
//        System.out.println("game");
//        gameScreen.dispose();
        System.out.println("tutorial");
        tutorialScreen.dispose();
        //        System.out.println("leaderboard");
        //        leaderboardScreen.dispose();
        //        System.out.println("signin");
        //        signInScreen.dispose();
    }
    
    public void render()
    {
        super.render();
    }
}
