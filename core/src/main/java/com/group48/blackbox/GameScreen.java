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

// TODO get atoms.excludedCoords() in here
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
            edgeCoords.add("1,1");
            edgeCoords.add("6,1");
            edgeCoords.add("1,2");
            edgeCoords.add("7,2");
            edgeCoords.add("0,3");
            edgeCoords.add("7,3");
            edgeCoords.add("0,4");
            edgeCoords.add("8,4");
            edgeCoords.add("0,5");
            edgeCoords.add("7,5");
            edgeCoords.add("1,6");
            edgeCoords.add("7,6");
            edgeCoords.add("1,7");
            edgeCoords.add("6,7");
            edgeCoords.add("2,8");
            edgeCoords.add("3,8");
            edgeCoords.add("4,8");
            edgeCoords.add("5,8");
            edgeCoords.add("6,8");
        }
        return edgeCoords;
    }
    
    public boolean isEdge(int x, int y)
    {
        return edgeCoords.contains(x + "," + y);
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

enum Tile {
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

public class GameScreen extends SignIn implements Screen {
    final BlackBox game;
    
    private TiledMap tiledMap;
    private TiledMapRenderer renderer;
    private TileCoordinates specialCoords;
    
    private Atoms atoms;
    private Rays rays;
    private TiledMapTileLayer.Cell selectedTile;
    
    public GameScreen(BlackBox game)
    {
        this.game = game;
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
    }
    
    // TODO implement direction + proper tile selection
    @Override
    public void render(float delta)
    {
        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);
        
        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);
        
        renderer.setView(game.camera);
        game.camera.position.set(360, 110, 0);
        renderer.render();
        
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            // Check for button presses
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(mousePos);
            
            // get coordinates of selected tile
            int tileX = (int) (mousePos.x / 32);
            int tileY = (int) (mousePos.y / 34);
            
            // if selected tile is invalid, deselect all tiles
            if (atoms.isExcluded(tileX, tileY)) {
                System.out.println("selected tile invalid");
                deselectTiles(tiledMap);
            }
            // if it's an edge, select the first tile
            else if (specialCoords.isEdge(tileX, tileY) && selectedTile == null) {
                System.out.println("first tile is an edge, selecting...");
                deselectTiles(tiledMap);
                selectedTile = selectTile(tiledMap, tileX, tileY);
            }
            // if a tile has been selected before
            else if (selectedTile != null) {
                System.out.println("starting tile defined");
                
                // if same tile is selected, then deselect tiles
                if (tileX == selectTileX && tileY == selectTileY) {
                    System.out.println("selected the same cell, deselecting...");
                    deselectTiles(tiledMap);
                } else {
                    // difference in X and Y
                    int tileDiffX = selectTileX - tileX, tileDiffY = selectTileY - tileY;
                    // TODO improve surrounding tile error checking
                    if (Math.abs(tileDiffX) == 0 || Math.abs(tileDiffX) == 1) {
                        if (Math.abs(tileDiffY) == 0 || Math.abs(tileDiffY) == 1) {
                            // if it's not an edge tile, cast a ray
                            if (!specialCoords.isEdge(tileX, tileY)) {
                                System.out.println("selection is not an edge tile, casting ray...");
                                rays.newRay(selectedTile, selectTile(tiledMap, tileX, tileY));
                            }
                            // if the starter tile is a corner tile, cast a ray
                            else if (specialCoords.getCornerCoords().contains(selectTileX + "," + selectTileY)) {
                                System.out.println("first selection was a corner tile, casting ray...");
                                rays.newRay(selectedTile, selectTile(tiledMap, tileX, tileY));
                            } else deselectTiles(tiledMap);
                        } else {
                            System.out.println("invalid selection");
                            deselectTiles(tiledMap);
                        }
                    } else {
                        System.out.println("invalid selection");
                        deselectTiles(tiledMap);
                    }
                    selectedTile = null;
                }
            } else {
                deselectTiles(tiledMap);
            }
            System.out.println();
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
        
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            System.out.println("ESC button clicked!");
            game.setScreen(new MainMenuScreen(game));
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            System.out.println("SPACEBAR clicked!");
            atoms.setGameFinished();
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
    
    int selectTileX, selectTileY;
    
    // this method changes a tile from black to green to signify that it has been selected
    TiledMapTileLayer.Cell selectTile(TiledMap tiledMap, int x, int y)
    {
        TiledMapTileLayer.Cell cell;
        
        selectTileX = x;
        selectTileY = y;
        
        // error checking; not in tilemap region
        MapProperties map = tiledMap.getProperties();
        if (selectTileX > map.get("width", Integer.class) - 1 ||
                selectTileY > map.get("height", Integer.class) - 1) {
            return null;
        } else if (atoms.isExcluded(selectTileX, selectTileY)) { // error checking; unrendered tile
            return null;
        } else {
            // get green tile tilemap (image)
            TiledMapTile selectedTile = tiledMap.getTileSets().getTileSet("Hex").getTile(Tile.GREEN.getValue());
            // select "Base" tile layer from Hex.tmx; makes next line more concise
            TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Base");
            // use this to select a specific tile by the X and Y coordinate (in the range of 0-8)
            // x: 0 = leftmost, 8 = rightmost on board
            // y: 0 = lowest, 8 = highest on board
            cell = tileLayer.getCell(selectTileX, selectTileY);
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
