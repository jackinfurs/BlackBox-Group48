package com.group48.blackbox;

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
    
    public boolean isCorner(int x, int y)
    {
        return cornerCoords.contains(x + "," + y);
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
    private CoordCell pointerTile;
    
    public GameBoard(final BlackBox game)
    {
        this.game = game;
        tiledMap = new TmxMapLoader().load("GameScreen/HexMap.tmx");
        
        renderer = new HexagonalTiledMapRenderer(tiledMap);
        specialCoords = new TileCoordinates();
        atoms = new Atoms(tiledMap);
        rays = new Rays(tiledMap);
        score = new Score(atoms);
    }
    
    public int addGuessAtom(Vector3 Mouse)
    {
        return atoms.addGuessAtom(getTileXCoord(Mouse), getTileYCoord(Mouse));
    }
    
    void deselectTiles()
    {
        // get base tile layer to clear
        TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Base");
        // get "clear" texture (replacing green with black)
        TiledMapTile defaultTile = tiledMap.getTileSets().getTileSet("Hex").getTile(Tile.BLACK.getValue());
        
        if (startTile != null) { // if first tile has been chosen
            tileLayer.getCell(startTile.getX(), startTile.getY()).setTile(defaultTile);
            startTile = null;
        }
        if (pointerTile != null) { // if second tile has been chosen
            tileLayer.getCell(pointerTile.getX(), pointerTile.getY()).setTile(defaultTile);
            pointerTile = null;
        }
    }
    
    public Atoms getAtoms()
    {
        return atoms;
    }
    
    public TiledMapRenderer getRenderer()
    {
        return renderer;
    }
    
    public int getTileXCoord(Vector3 Mouse)
    {
        return (int) Mouse.x / 32;
    }
    
    public int getTileYCoord(Vector3 Mouse)
    {
        return (int) Mouse.y / 26;
    }
    
    public void placeAtoms()
    {
        atoms.placeRandomAtoms();
    }
    
    public int selectTile(Vector3 mouse)
    {
        int tileX = getTileXCoord(mouse);
        int tileY = getTileYCoord(mouse);
        
        // regardless error checking for outside board
        if (atoms.isExcluded(tileX, tileY)) {
            System.out.println("Selected tile invalid.\n");
            deselectTiles();
            return -1;
        }
        
        // first selection
        // must be an edge
        if (specialCoords.isEdge(tileX, tileY) && startTile == null) {
            System.out.println("Selected an edge tile\nSelect a valid adjacent tile");
            startTile = new CoordCell(selectTile(tiledMap, tileX, tileY), tileX, tileY);
            return 0;
        }
        
        // second selection (if it applies)
        if (startTile != null) {
            pointerTile = new CoordCell(selectTile(tiledMap, tileX, tileY), tileX, tileY);
            // must be a neighbour
            if (startTile.isNeighbour(pointerTile)) {
                // if starter tile is a corner tile (since a corner is still an edge)
                if (specialCoords.isCorner(startTile.getX(), startTile.getY())) {
                    rays.newRay(startTile, pointerTile);
                    return 0;
                }
                // if second tile isn't an edge, cast a ray
                else if (!specialCoords.isEdge(pointerTile.getX(), pointerTile.getY())) {
                    rays.newRay(startTile, pointerTile);
                    return 0;
                }
            }
        }
        // if you have gotten to this point you have failed
        deselectTiles();
        return -1;
    }
    
    public void setFinished(boolean finished)
    {
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
    
    public void dispose()
    {
        tiledMap.dispose();
        rays.dispose();
    }
}

class CoordCell {
    private final TiledMapTileLayer.Cell tile;
    private final int x;
    private final int y;
    
    public CoordCell(TiledMapTileLayer.Cell tile, int x, int y)
    {
        this.tile = tile;
        this.x = x;
        this.y = y;
    }
    
    public boolean isNeighbour(CoordCell neighbour)
    {
        boolean isOdd = y % 2 == 1;
        // add all possible neighbours of this tile to a HashSet
        HashSet<CoordCell> neighbours = new HashSet<>();
        {
            neighbours.add(isOdd ? new CoordCell(null, x, y - 1) : new CoordCell(null, x - 1, y - 1));
            neighbours.add(isOdd ? new CoordCell(null, x + 1, y - 1) : new CoordCell(null, x, y - 1));
            neighbours.add(new CoordCell(null, x + 1, y));
            neighbours.add(isOdd ? new CoordCell(null, x + 1, y + 1) : new CoordCell(null, x, y + 1));
            neighbours.add(isOdd ? new CoordCell(null, x, y + 1) : new CoordCell(null, x - 1, y + 1));
            neighbours.add(new CoordCell(null, x - 1, y));
        }
        
        for (CoordCell c : neighbours)
            if (c.getX() == neighbour.getX() && c.getY() == neighbour.getY())
                return true;
        return false;
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
