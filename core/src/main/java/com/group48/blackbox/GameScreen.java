package com.group48.blackbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameScreen extends SignIn implements Screen {
    final BlackBox game;
    OrthographicCamera camera;

    TiledMap tiledMap;
    TiledMapRenderer renderer;

    private Atoms atoms;

    public GameScreen(BlackBox game) {
        this.game = game;
        camera = new OrthographicCamera();

        // TODO please find a way to set the viewportWidth and Height to something more concrete
        //  this is exactly the time i'd like a preprocessor directive

        camera.setToOrtho(false, 800, 600);

        // ESC key exits to main menu
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            System.out.println("ESC button clicked!");
            game.setScreen(new MainMenuScreen(game));
        }
    }



    @Override
    public void show() {
        tiledMap = new TmxMapLoader().load("GameScreen/HexMap.tmx");
        renderer = new HexagonalTiledMapRenderer(tiledMap);

        atoms = new Atoms(tiledMap);
        atoms.placeRandomAtoms();
    }


    // this method changes a tile from black to green to signify that it has been selected
    void selectTile(TiledMap tiledMap, int x, int y) {
        // error checking
        MapProperties map = tiledMap.getProperties();
        if (x > map.get("width", Integer.class) - 1 ||
                y > map.get("height", Integer.class) - 1) {
            System.out.println("Cell not located within bounds");
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

    void deselectTiles(TiledMap tiledMap) {
        TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Base");
        TiledMapTile defaultTile = tiledMap.getTileSets().getTileSet("Hex").getTile(1);

        for (int y = 0; y < tileLayer.getHeight(); y++) {
            for (int x = 0; x < tileLayer.getWidth(); x++) {
                TiledMapTileLayer.Cell cell = tileLayer.getCell(x, y);
                if (cell != null) { // Check if the cell is not null
                    cell.setTile(defaultTile);
                }
            }
        }
    }


    @Override
    public void render(float delta) {

        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {

            // Check for button presses
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mousePos);

            int tileX = (int) (mousePos.x / 32);
            int tileY = (int) (mousePos.y / 34);
            System.out.println("position x: " + tileX + " position y: " + tileY);
            deselectTiles(tiledMap);
            selectTile(tiledMap, tileX, tileY);
        }

        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);


        renderer.setView(camera);
        camera.position.set(360, 110, 0);
        renderer.render();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            System.out.println("ESC button clicked!");
            game.setScreen(new MainMenuScreen(game));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            System.out.println("SPACEBAR clicked!");
            atoms.showCOI();
            atoms.setGameFinished(true);
            renderer.render();
        }

        if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {

            // Check for button presses
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mousePos);

            int tileX = (int) (mousePos.x / 32);
            int tileY = (int) (mousePos.y / 34);
            System.out.println("position x: " + tileX + " position y: " + tileY);
            atoms.addGuessAtom(tileX, tileY);
        }

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
