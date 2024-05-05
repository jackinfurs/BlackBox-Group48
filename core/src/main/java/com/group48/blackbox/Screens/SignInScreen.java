package com.group48.blackbox.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.group48.blackbox.BlackBox;
import com.group48.blackbox.UsersManager;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class SignInScreen implements Screen {
    
    final BlackBox game;
    private final Stage stage;
    private TextField usernameField;
    
    public SignInScreen(final BlackBox game)
    {
        this.game = game;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), game.camera));
    }
    
    @Override
    public void show()
    {
        System.out.println("\n--- SIGN IN SCREEN ---\nEnter your username and hit ENTER to begin playing Black Box+\n");
        Gdx.input.setInputProcessor(stage);
        
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        
        Texture backgroundTex = game.assets.get("signinBackground.png");
        Image background = new Image(backgroundTex);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        usernameField = new TextField("", skin);
        usernameField.setMessageText("Enter username...");
        usernameField.setPosition(stage.getWidth() / 2f + 40, stage.getHeight() / 2f);
        usernameField.setSize(300, 35);
        
        stage.addActor(background);
        stage.addActor(usernameField);
        stage.addAction(sequence(alpha(0f), fadeIn(0.5f)));
    }
    
    @Override
    public void render(float delta)
    {
        
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        update(delta);
        
        game.batch.begin();
        stage.draw();
        
        // not sure there's a better way to do this.
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (!usernameField.getText().isEmpty()) {
                UsersManager.setUsername(usernameField.getText());
                game.setScreen(game.gameScreen);
            } else {
                game.assets.get("Sound/clickInvalid.wav", Sound.class).play();
                System.out.println("Username not provided. Please provide a username.");
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.assets.get("Sound/clickBack.wav", Sound.class).play();
            game.setScreen(game.mainMenuScreen);
        }
        
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
        stage.clear();
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
}
