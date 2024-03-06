package com.group48.blackbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SignInScreen implements Screen {

    final BlackBox game;
    private Stage stage;
    private TextField usernameField;

    public SignInScreen(final BlackBox game) {
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

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);
        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        stage.act();
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            SignIn.username = usernameField.getText();
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
