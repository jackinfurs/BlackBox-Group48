package com.group48.blackbox;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;

import java.util.HashSet;
import java.util.Set;

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

public class GameBoard {

    final BlackBox game;

    private final TiledMap tiledMap;
    private final TiledMapRenderer renderer;
    private final TileCoordinates specialCoords;
    private final Atoms atoms;
    private final Rays rays;
    private final Score score;
    private CoordCell startTile;
    private boolean finished;

    public GameBoard(final BlackBox game)
    {
        this.game = game;
        this.tiledMap = new TmxMapLoader().load("GameScreen/HexMap.tmx");
        atoms = new Atoms(tiledMap);
        rays = new Rays(tiledMap);
        renderer = new HexagonalTiledMapRenderer(tiledMap);
        specialCoords = new TileCoordinates();
        this.score = new Score(atoms);
    }

    public void placeAtoms()
    {
        atoms.placeRandomAtoms();
    }

    public int selectTile(Vector3 Mouse)
    {
        int tileX = getTileXCoord(Mouse);
        int tileY = getTileYCoord(Mouse);

        if (atoms.isExcluded(tileX, tileY)) {
            System.out.println("Selected tile is invalid\n");
            deselectTiles(tiledMap);
            return -1;
        }
        // if it's an edge, select the first tile
        else if (specialCoords.isEdge(tileX, tileY) && startTile == null) {
            System.out.println("Selected an edge tile\nSelect a valid adjacent tile");
            deselectTiles(tiledMap);
            startTile = new CoordCell(selectTile(tiledMap, tileX, tileY), tileX, tileY);
        }
        // if a tile has been selected before
        else if (startTile != null) {
            // if same tile is selected, then deselect tiles
            if (tileX == startTile.getX() && tileY == startTile.getY()) {
                System.out.println("Selected the same cell, deselecting...\n");
                deselectTiles(tiledMap);
                return -1;
            } else {
                // difference in X and Y
                int tileDiffX = startTile.getX() - tileX, tileDiffY = startTile.getY() - tileY;
                // TODO improve surrounding tile error checking
                if (Math.abs(tileDiffX) == 0 || Math.abs(tileDiffX) == 1) {
                    if (Math.abs(tileDiffY) == 0 || Math.abs(tileDiffY) == 1) {
                        // if it's not an edge tile, cast a ray
                        if (!specialCoords.isEdge(tileX, tileY)) {
                            System.out.println("Casting ray...\n");
                            //                                rays.newRay(selectedTile, selectTile(tiledMap, tileX, tileY));
                            rays.newRay(startTile, new CoordCell(selectTile(tiledMap, tileX, tileY), tileX, tileY));
                            return 0;
                        }
                        // if the starter tile is a corner tile, cast a ray
                        else if (specialCoords.getCornerCoords().contains(startTile.getX() + "," + startTile.getY())) {
                            System.out.println("Casting ray...\n");
                            //                                rays.newRay(selectedTile, selectTile(tiledMap, tileX, tileY));
                            rays.newRay(startTile, new CoordCell(selectTile(tiledMap, tileX, tileY), tileX, tileY));
                        } else deselectTiles(tiledMap);
                    } else {
                        System.out.println("Invalid selection\n");
                        deselectTiles(tiledMap);
                        return -1;
                    }
                } else {
                    System.out.println("Invalid selection\n");
                    deselectTiles(tiledMap);
                    return -1;
                }
                startTile = null;
            }
        } else {
            deselectTiles(tiledMap);
            return -1;
        }
        return 0;
    }

    public int getTileXCoord(Vector3 Mouse)
    {
        return (int) Mouse.x / 32;
    }

    public int getTileYCoord(Vector3 Mouse)
    {
        return (int) Mouse.y / 26;
    }

    public boolean isFinished()
    {
        return finished;
    }

    public void setFinished(boolean finished)
    {
        this.finished = finished;
        atoms.revealAtoms();
        // FIXME prevent this from being printed in TutorialScreen
//        int scoreTotal;
//        scoreTotal = score.calculateScore();
//        System.out.println("Score for this round: " + scoreTotal);
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

    public int addGuessAtom(Vector3 Mouse)
    {
        return atoms.addGuessAtom(getTileXCoord(Mouse), getTileYCoord(Mouse));
    }

    public Atoms getAtoms()
    {
        return atoms;
    }

    public TiledMapRenderer getRenderer()
    {
        return renderer;
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

    public void dispose()
    {
        tiledMap.dispose();
        rays.dispose();
    }
}

class CoordCell {
    private final int x;
    private final int y;

    public CoordCell(TiledMapTileLayer.Cell selectedTile, int x, int y)
    {
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

