package com.group48.blackbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.HashSet;
import java.util.Set;

class TileCoordinates {
    private Set<String> edgeCoords, cornerCoords;
    
    public TileCoordinates()
    {
        this.edgeCoords = getEdgeCoords();
        this.cornerCoords = getCornerCoords();
    }
    
    public Set<String> getEdgeCoords()
    {
        if (edgeCoords == null) {
            edgeCoords = new HashSet<>();
            edgeCoords.add("2,0");
            edgeCoords.add("3,0");
            edgeCoords.add("4,0");
            edgeCoords.add("5,0");
            edgeCoords.add("6,0");
            edgeCoords.add("2,1");
            edgeCoords.add("6,1");
            edgeCoords.add("1,2");
            edgeCoords.add("7,2");
            edgeCoords.add("1,3");
            edgeCoords.add("7,3");
            edgeCoords.add("0,4");
            edgeCoords.add("8,4");
            edgeCoords.add("1,5");
            edgeCoords.add("7,5");
            edgeCoords.add("1,6");
            edgeCoords.add("7,6");
            edgeCoords.add("2,7");
            edgeCoords.add("6,7");
            edgeCoords.add("2,8");
            edgeCoords.add("3,8");
            edgeCoords.add("4,8");
            edgeCoords.add("5,8");
            edgeCoords.add("6,8");
        }
        return edgeCoords;
    }
    
    public Set<String> getCornerCoords()
    {
        if (cornerCoords == null) {
            cornerCoords = new HashSet<>();
            cornerCoords.add("2,0");
            cornerCoords.add("6,0");
            cornerCoords.add("0,4");
            cornerCoords.add("8,4");
            cornerCoords.add("2,8");
            cornerCoords.add("6,8");
        }
        return cornerCoords;
    }
}

public class GameScreen extends SignIn implements Screen {
    final BlackBox game;
    
    TiledMap tiledMap;
    TiledMapRenderer renderer;
    private TileCoordinates specialCoords;
    private Atoms atoms;
    private Rays rays;
    private TiledMapTileLayer.Cell selectedTile;
    
    private enum Tile {
        BLACK(1), GREEN(2);
        private final int value;
        
        Tile(int value)
        {
            this.value = value;
        }
        
        public int getValue()
        {
            return value;
        }
    }
    
    public GameScreen(BlackBox game)
    {
        this.game = game;
        
        // ESC key exits to main menu
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            System.out.println("ESC button clicked!");
            game.setScreen(new MainMenuScreen(game));
        }
    }
    
    @Override
    public void show()
    {
        tiledMap = new TmxMapLoader().load("GameScreen/HexMap.tmx");
        renderer = new HexagonalTiledMapRenderer(tiledMap);
        specialCoords = new TileCoordinates();
        atoms = new Atoms(tiledMap);
        atoms.placeRandomAtoms();
        rays = new Rays(tiledMap);
        selectedTile = null;
    }
    
    // TODO implement direction + proper tile selection
    @Override
    public void render(float delta)
    {
        
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            // Check for button presses
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(mousePos);
            
            int tileX = (int) (mousePos.x / 32);
            int tileY = (int) (mousePos.y / 34);
            String tileCoordinate = tileX + "," + tileY;
            
            if (atoms.getExcludedCoords().contains(tileCoordinate)) deselectTiles(tiledMap);
                // if no tile has been selected before, and it's an edge tile
            else if (specialCoords.getEdgeCoords().contains(tileCoordinate) && selectedTile == null) {
                deselectTiles(tiledMap);
                selectedTile = selectTile(tiledMap, tileX, tileY);
            }
            // if a tile has been selected before
            else if (selectedTile != null) {
                // if it's not an excluded tile, cast a ray
                if (!specialCoords.getEdgeCoords().contains(tileCoordinate) || specialCoords.getCornerCoords().contains(tileCoordinate))
                    rays.newRay(selectedTile, selectTile(tiledMap, tileX, tileY));
                
                selectedTile = null;
            }
            System.out.println(tileCoordinate + "\nedge piece?: " + specialCoords.getEdgeCoords().contains(tileCoordinate));
        }
        
        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);
        
        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);
        
        renderer.setView(game.camera);
        game.camera.position.set(360, 110, 0);
        renderer.render();
        
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            System.out.println("ESC button clicked!");
            game.setScreen(new MainMenuScreen(game));
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            System.out.println("SPACEBAR clicked!");
            atoms.setGameFinished();
            renderer.render();
        }
        
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            
            // Check for button presses
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(mousePos);
            
            int tileX = (int) (mousePos.x / 32);
            int tileY = (int) (mousePos.y / 34);
            System.out.println("position x: " + tileX + " position y: " + tileY);
            atoms.addGuessAtom(tileX, tileY);
        }
        
        renderer.render();
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
        tiledMap.dispose();
    }
    
    // this method changes a tile from black to green to signify that it has been selected
    TiledMapTileLayer.Cell selectTile(TiledMap tiledMap, int x, int y)
    {
        TiledMapTileLayer.Cell cell;
        
        // error checking; not in tilemap region
        MapProperties map = tiledMap.getProperties();
        if (x > map.get("width", Integer.class) - 1 ||
                y > map.get("height", Integer.class) - 1) {
            return null;
        } else if (atoms.getExcludedCoords().contains(x + "," + y)) { // error checking; unrendered tile
            return null;
        } else {
            // get green tile tilemap (image)
            TiledMapTile selectedTile = tiledMap.getTileSets().getTileSet("Hex").getTile(Tile.GREEN.getValue());
            // select "Base" tile layer from Hex.tmx; makes next line more concise
            TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Base");
            // use this to select a specific tile by the X and Y coordinate (in the range of 0-8)
            // x: 0 = leftmost, 8 = rightmost on board
            // y: 0 = lowest, 8 = highest on board
            cell = tileLayer.getCell(x, y);
            // change cell to green tile (selectedTile above)
            
            // finally render (please make sure to call this anytime you change the board)
            renderer.render();
            return cell.setTile(selectedTile);
        }
    }
    
    void deselectTiles(TiledMap tiledMap)
    {
        selectedTile = null;
        TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Base");
        TiledMapTile defaultTile = tiledMap.getTileSets().getTileSet("Hex").getTile(Tile.BLACK.getValue());
        
        for (int y = 0 ; y < tileLayer.getHeight() ; y++) {
            for (int x = 0 ; x < tileLayer.getWidth() ; x++) {
                TiledMapTileLayer.Cell cell = tileLayer.getCell(x, y);
                if (cell != null) { // Check if the cell is not null
                    cell.setTile(defaultTile);
                }
            }
        }
    }
}
