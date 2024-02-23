package com.blackbox.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.*;
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

    void selectTile(TiledMap tiledMap, int x, int y) {
//        MapProperties map = tiledMap.getProperties();
//        if (x > map.get("width", Integer.class) - 1 ||
//                y > map.get("height", Integer.class) - 1) {
//            throw new IllegalArgumentException("coordinates out of bounds");
//        } else {
            TiledMapTile selectedTile = tiledMap.getTileSets().getTile(2); // 1 = black, 2 = green; in Tiled it's 1 for some reason
            TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Base");
            TiledMapTileLayer.Cell cell = tileLayer.getCell(x, y);
            cell.setTile(selectedTile);
            renderer.render();
        //}
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        tiledMap = new TmxMapLoader().load("GameScreen/HexMap.tmx");
        renderer = new HexagonalTiledMapRenderer(tiledMap);

        game.batch.begin();

        renderer.setView(camera);
        camera.position.set(360, 110, 0);
        renderer.render();

        selectTile(tiledMap, 4, 4);

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
