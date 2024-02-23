package com.blackbox.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen implements Screen {
    final BlackBox game;
    OrthographicCamera camera;

    SpriteBatch batch;

    public GameScreen(BlackBox game) {
        this.game = game;
        camera = new OrthographicCamera();

        // TODO please find a way to set the viewportWidth and Height to something more concrete
        //  this is exactly the time i'd like a preprocessor directive
        camera.setToOrtho(false, 800, 600);
        batch = new SpriteBatch();

        // TODO insert input processor and insert below
        /*
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            System.out.println("ESC button clicked!");
            game.setScreen(new MainMenuScreen(game));
        }
         */
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.end();

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
        batch.dispose();
    }
}
