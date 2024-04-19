package com.group48.blackbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.FitViewport;

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
        Gdx.input.setInputProcessor(stage);
        
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        
        Texture backgroundTex = game.assets.get("signinBackground.png");
        Image background = new Image(backgroundTex);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        usernameField = new TextField("", skin);
        usernameField.setMessageText("Enter username...");
        usernameField.setPosition(stage.getWidth() / 2f + 25, stage.getHeight() / 2f);
        usernameField.setSize(300, 40);
        
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
                System.out.printf("username %s, loading gameScreen\n", usernameField.getText());
                SignIn.setUsername(usernameField.getText());
                game.setScreen(game.gameScreen);
            } else System.out.println("username not provided");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            System.out.println("back to the main menu");
            game.setScreen(game.mainMenuScreen);
        }
        
        game.batch.end();
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
}
