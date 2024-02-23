package com.blackbox.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameScreen implements Screen {
    final BlackBox game;
    OrthographicCamera camera;

    TiledMap tiledMap;
    TiledMapRenderer renderer;

    public GameScreen(BlackBox game) {
        this.game = game;
        camera = new OrthographicCamera();

        // TODO please find a way to set the viewportWidth and Height to something more concrete
        //  this is exactly the time i'd like a preprocessor directive
        camera.setToOrtho(false, 800, 600);

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
        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        tiledMap = new TmxMapLoader().load("GameScreen/HexMap.tmx");
        renderer = new HexagonalTiledMapRenderer(tiledMap);

        game.batch.begin();

        int[] baseLayer = {0}; // only black outlines
        int[] selectLayers = {1, 2}; // green outline + highlight

        renderer.setView(camera);
        renderer.render(baseLayer);
        camera.position.set(360,110,0);

        game.batch.end();

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
        tiledMap.dispose();
    }
}
