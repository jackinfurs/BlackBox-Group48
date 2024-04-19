package com.group48.blackbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Objects;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class MainMenuScreen implements Screen {
    final BlackBox game;
    private final Stage stage;
    
    private Image play, tutorial, exit, leaderboard;
    
    public MainMenuScreen(final BlackBox game)
    {
        this.game = game;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), game.camera));
    }
    
    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(stage);
        
        Texture backgroundTex = game.assets.get("MainMenuScreen/vaporBackground.png");
        Image background = new Image(backgroundTex);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // sequence; one after the other. parallel; both at the same time.
        // forever is always called and will keep being called
        stage.addAction(sequence(alpha(0f), fadeIn(0.5f)));
        stage.addActor(background);
        
        initButtons();
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
        stage.addAction(fadeOut(0.5f));
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
    
    private void initButtons()
    {
        System.out.println("creating play button...");
        Texture playTex = game.assets.get("MainMenuScreen/play.png");
        play = new Image(playTex);
        play.setPosition(Gdx.graphics.getWidth() / 2f, 410);
        play.setSize(280, 60);
        play.addListener(new ClickListener() {
            
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
            {
                System.out.println("mouseover play");
                game.assets.get("Sound/clickHover.wav", Sound.class).play();
                play.addAction(color(Color.CORAL));
            }
            
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
            {
                System.out.println("notsomouseover play\n");
                play.addAction(color(Color.WHITE));
            }
            
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                game.assets.get("Sound/clickConfirm.wav", Sound.class).play();
                if (Objects.isNull(SignIn.getUsername()) || Objects.equals(SignIn.getUsername(), "sv_cheats 1")) {
                    System.out.println("username not specified or cheats used\nlaunching SignInScreen");
                    game.setScreen(game.signInScreen);
                } else {
                    System.out.println("username already specified\nlaunching GameScreen");
                    game.setScreen(game.gameScreen);
                }
            }
        });
        
        System.out.println("creating tutorial button...");
        Texture tutorialTex = game.assets.get("MainMenuScreen/tutorial.png");
        tutorial = new Image(tutorialTex);
        tutorial.setPosition(Gdx.graphics.getWidth() / 2f, 310);
        tutorial.setSize(280, 60);
        tutorial.addListener(new ClickListener() {
            
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
            {
                game.assets.get("Sound/clickHover.wav", Sound.class).play();
                System.out.println("mouseover tutorial");
                tutorial.addAction(color(Color.CORAL));
            }
            
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
            {
                System.out.println("notsomouseover tutorial\n");
                tutorial.addAction(color(Color.WHITE));
            }
            
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                game.assets.get("Sound/clickConfirm.wav", Sound.class).play();
                System.out.println("loading tutorial screen...");
                game.setScreen(game.tutorialScreen);
            }
        });
        
        System.out.println("creating leaderboard button...");
        Texture leaderboardTex = game.assets.get("MainMenuScreen/leaderboard.png");
        leaderboard = new Image(leaderboardTex);
        leaderboard.setPosition(Gdx.graphics.getWidth() / 2f, 210);
        leaderboard.setSize(280, 60);
        leaderboard.addListener(new ClickListener() {
            
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
            {
                game.assets.get("Sound/clickHover.wav", Sound.class).play();
                System.out.println("mouseover leaderboard");
                leaderboard.addAction(color(Color.CORAL));
            }
            
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
            {
                System.out.println("notsomouseover leaderboard\n");
                leaderboard.addAction(color(Color.WHITE));
            }
            
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                game.assets.get("Sound/clickConfirm.wav", Sound.class).play();
                System.out.println("loading leaderboard screen...");
                game.setScreen(game.leaderboardScreen);
            }
        });
        
        System.out.println("creating exit button...");
        Texture exitTex = game.assets.get("MainMenuScreen/exit.png");
        exit = new Image(exitTex);
        exit.setPosition(Gdx.graphics.getWidth() / 2f, 110);
        exit.setSize(280, 60);
        exit.addListener(new ClickListener() {
            
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
            {
                game.assets.get("Sound/clickHover.wav", Sound.class).play();
                System.out.println("mouseover exit");
                exit.addAction(color(Color.CORAL));
            }
            
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
            {
                System.out.println("notsomouseover exit\n");
                exit.addAction(color(Color.WHITE));
            }
            
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                game.assets.get("Sound/clickConfirm.wav", Sound.class).play();
                System.out.println("exiting...");
                Gdx.app.exit();
            }
        });
        
        System.out.println("adding actors for all buttons...");
        stage.addActor(play);
        stage.addActor(tutorial);
        stage.addActor(leaderboard);
        stage.addActor(exit);
    }
}
