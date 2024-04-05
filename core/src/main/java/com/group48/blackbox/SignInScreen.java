package com.group48.blackbox;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.Texture;

public class SignInScreen implements Screen {

    final BlackBox game;
    private final Stage stage;
    private final TextField usernameField;
    private final Texture backgroundTexture;

    public SignInScreen(final BlackBox game)
    {
        this.game = game;

        stage = new Stage(new ScreenViewport(), game.batch);
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        usernameField = new TextField("", skin);
        usernameField.setMessageText("Enter username...");

        Table table = new Table();
        table.setFillParent(true);
        table.add(usernameField).width(200).height(30);

        stage.addActor(table);

        // Load background texture from assets folder
        backgroundTexture = new Texture("signinBackground.png");
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        // Clear the screen
        ScreenUtils.clear(0, 0, 0, 1);
        game.batch.begin();

        // Draw background
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        game.batch.end();

        // Update and draw stage
        stage.act();
        stage.draw();

        // Check for Enter key press to proceed to the game screen
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            SignIn.username = usernameField.getText();
            game.setScreen(new GameScreen(game));
        }
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
        stage.dispose();
        backgroundTexture.dispose();
    }
}
