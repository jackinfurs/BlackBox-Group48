package com.blackbox.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
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

    // this method changes a tile from black to green to signify that it has been selected
    void selectTile(TiledMap tiledMap, int x, int y) {
        // error checking
        MapProperties map = tiledMap.getProperties();
        if (x > map.get("width", Integer.class) - 1 ||
                y > map.get("height", Integer.class) - 1) {
            System.out.println("YOU FUCKED IT. OUT OF BOUNDS."); // change it to something less vulgar. this repeatedly prints and i'm not sure how to make it print only once.
        } else {
            // get green tile tilemap (image)
            // getTile(1) = black, getTile(2) = green
            TiledMapTile selectedTile = tiledMap.getTileSets().getTileSet("Hex").getTile(2);
            // select "Base" tile layer from Hex.tmx; makes next line more concise
            TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Base");
            // use this to select a specific tile by the X and Y coordinate (in the range of 0-8)
            // x: 0 = leftmost, 8 = rightmost on board
            // y: 0 = lowest, 8 = highest on board
            TiledMapTileLayer.Cell cell = tileLayer.getCell(x, y);
            // change cell to green tile (selectedTile above)
            cell.setTile(selectedTile);
            // finally render (please make sure to call this anytime you change the board)
            renderer.render();
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // load HexMap.tmx, contains board + tiles
        tiledMap = new TmxMapLoader().load("GameScreen/HexMap.tmx");
        renderer = new HexagonalTiledMapRenderer(tiledMap);

        renderer.setView(camera);
        camera.position.set(360, 110, 0);
        renderer.render();

        selectTile(tiledMap, 4, 4);

        // TODO CLEAN THIS FUCKIN MESS
        // to create an "Atoms" layer
        MapLayers layers = tiledMap.getLayers();
        TiledMapTileLayer atomsLayer = new TiledMapTileLayer(9,9,32,34);
        atomsLayer.setName("Atoms");
        layers.add(atomsLayer);

        // to create an "Atom" tileset with red atoms (do the same for guessAtom.png later on)
        TiledMapTileSet tileset = new TiledMapTileSet();
        TextureRegion tile1 = new TextureRegion(new Texture("GameScreen/redAtom.png"));
        TiledMapTile tile1Data = new StaticTiledMapTile(tile1);
        tile1Data.setId(1);
        tileset.putTile(1, tile1Data);

        // to place an atom at x=4,y=4
        TiledMapTileLayer.Cell atomCell = new TiledMapTileLayer.Cell();
        atomCell.setTile(tileset.getTile(1));
        atomsLayer.setCell(4,4,atomCell);

        renderer.render();

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
