package com.group48.blackbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.io.FileWriter;  // Import the File class

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

class CoordCell {
    private TiledMapTileLayer.Cell selectedTile;
    private int x, y;
    
    public CoordCell(TiledMapTileLayer.Cell selectedTile, int x, int y)
    {
        this.selectedTile = selectedTile;
        this.x = x;
        this.y = y;
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }
}

public class GameScreen extends SignIn implements Screen {
    final BlackBox game;
    
    private TiledMap tiledMap;
    private TiledMapRenderer renderer;
    private TileCoordinates specialCoords;
    
    private Atoms atoms;
    private Rays rays;
    private CoordCell startTile;
    
    private int cX, cY;
    private FileWriter coords;
    
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
        
        try {
            coords = new FileWriter("C:\\Users\\dynam\\Desktop\\coords.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cX = 0;
        cY = 0;
    }
    
    // TODO implement direction + proper tile selection
    @Override
    public void render(float delta)
    {
        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);
        game.camera.update();
        
        // TODO redo this please
        TextButton endButton = new TextButton("End game", new Skin(Gdx.files.internal("uiskin.json")));
        endButton.setPosition(500, 150);
        Rectangle endButtonBounds = new Rectangle(endButton.getX(), endButton.getY(), endButton.getWidth(), endButton.getHeight());
        
        TextButton exitButton = new TextButton("Exit to main menu", new Skin(Gdx.files.internal("uiskin.json")));
        exitButton.setPosition(500, 50);
        Rectangle exitButtonBounds = new Rectangle(exitButton.getX(), exitButton.getY(), exitButton.getWidth(), exitButton.getHeight());
        
        game.camera.position.set(360, 110, 0);
        game.camera.update();
        renderer.setView(game.camera);
        game.batch.setProjectionMatrix(game.camera.combined);
        
        game.batch.begin();
        endButton.draw(game.batch, 1f);
        exitButton.draw(game.batch, 1f);
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            // Check for button presses
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(mousePos);
            
            try {
                coords.write("circleCoords[" + cY + "][" + cX + "] = \"" + mousePos.x + "," + mousePos.y + "\";\n");
                System.out.println("circleCoords[" + cY + "][" + cX + "] = \"" + mousePos.x + "," + mousePos.y + "\";\n");
                
                cX++;
                if (cX > 9) {
                    cY++;
                    cX = 0;
                }
                if (cY > 9) coords.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            // Check for button presses
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(mousePos);
            
            if (endButtonBounds.contains(mousePos.x, mousePos.y)) {
                atoms.setGameFinished();
            }
            if (exitButtonBounds.contains(mousePos.x, mousePos.y)) game.setScreen(new MainMenuScreen(game));
            
            // get coordinates of selected tile
            int tileX = (int) (mousePos.x / 32);
            int tileY = (int) (mousePos.y / 26);
            
            // if selected tile is invalid, deselect all tiles
            if (atoms.isExcluded(tileX, tileY)) {
                System.out.println("selected tile invalid");
                deselectTiles(tiledMap);
            }
            // if it's an edge, select the first tile
            else if (specialCoords.isEdge(tileX, tileY) && startTile == null) {
                System.out.println("first tile is an edge, selecting...");
                deselectTiles(tiledMap);
                startTile = new CoordCell(selectTile(tiledMap, tileX, tileY), tileX, tileY);
            }
            // if a tile has been selected before
            else if (startTile != null) {
                System.out.println("starting tile defined");
                
                // if same tile is selected, then deselect tiles
                if (tileX == startTile.getX() && tileY == startTile.getY()) {
                    System.out.println("selected the same cell, deselecting...");
                    deselectTiles(tiledMap);
                } else {
                    // difference in X and Y
                    int tileDiffX = startTile.getX() - tileX, tileDiffY = startTile.getY() - tileY;
                    // TODO improve surrounding tile error checking
                    if (Math.abs(tileDiffX) == 0 || Math.abs(tileDiffX) == 1) {
                        if (Math.abs(tileDiffY) == 0 || Math.abs(tileDiffY) == 1) {
                            // if it's not an edge tile, cast a ray
                            if (!specialCoords.isEdge(tileX, tileY)) {
                                System.out.println("selection is not an edge tile, casting ray...");
                                //                                rays.newRay(selectedTile, selectTile(tiledMap, tileX, tileY));
                                rays.newRay(startTile, new CoordCell(selectTile(tiledMap, tileX, tileY), tileX, tileY));
                            }
                            // if the starter tile is a corner tile, cast a ray
                            else if (specialCoords.getCornerCoords().contains(startTile.getX() + "," + startTile.getY())) {
                                System.out.println("first selection was a corner tile, casting ray...");
                                //                                rays.newRay(selectedTile, selectTile(tiledMap, tileX, tileY));
                                rays.newRay(startTile, new CoordCell(selectTile(tiledMap, tileX, tileY), tileX, tileY));
                            } else deselectTiles(tiledMap);
                        } else {
                            System.out.println("invalid selection");
                            deselectTiles(tiledMap);
                        }
                    } else {
                        System.out.println("invalid selection");
                        deselectTiles(tiledMap);
                    }
                    startTile = null;
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
            int tileY = (int) (mousePos.y / 26);
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
        game.batch.end();
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
        rays.dispose();
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
        } else if (atoms.isExcluded(x, y)) { // error checking; unrendered tile
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
