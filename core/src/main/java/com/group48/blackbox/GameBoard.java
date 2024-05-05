package com.group48.blackbox;

import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;

import java.util.HashSet;
import java.util.Objects;
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

/**
 * @author Jack Dunne 22483576
 * @see Atoms
 * @see Rays
 * @see GameScreen
 * @since Sprint 1
 */

public class GameBoard {
    
    final BlackBox game;
    
    private final TiledMap tiledMap;
    private final TiledMapRenderer renderer;
    private final TileCoordinates specialCoords;
    private final Atoms atoms;
    private final Rays rays;
    private final Score score;
    private boolean finished;
    private CoordCell startTile;
    private CoordCell pointerTile;
    
    /**
     * Constructor for {@link GameBoard}. Requires an instance of {@link BlackBox} as a parameter.
     * <p>
     * Initialises:
     * <ul>
     *     <li>{@code this.game} to the {@code game} parameter
     *     <li>{@code tiledMap} to a new <a href="https://libgdx.com/wiki/graphics/2d/tile-maps">TiledMap</a> + renderer (taken from assets)
     *     <li>{@code specialCoords} to a new <a href="https://libgdx.com/wiki/graphics/2d/tile-maps">TiledMap</a> (taken from assets)
     *     <li>{@code atoms}, {@code rays}, {@code score} (see their respective documentation)
     * </ul>
     *
     * @param game
     *         an instance of {@link BlackBox}
     *
     * @see Atoms
     * @see Rays
     * @see Score
     */
    public GameBoard(final BlackBox game)
    {
        this.game = game;
        finished = false;
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
    
    private void deselectTiles()
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
    
    private TiledMapTileLayer.Cell greenify(int tileX, int tileY)
    {
        // get green tile tilemap (image)
        TiledMapTile selectedTile = tiledMap.getTileSets().getTileSet("Hex").getTile(Tile.GREEN.getValue());
        // select "Base" tile layer from Hex.tmx; makes next line more concise
        TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Base");
        // use this to select a specific tile by the X and Y coordinate (in the range of 0-8)
        // x: 0 = leftmost, 8 = rightmost on board
        // y: 0 = lowest, 8 = highest on board
        TiledMapTileLayer.Cell cell = tileLayer.getCell(tileX, tileY);
        // change cell to green tile (selectedTile above)
        return cell.setTile(selectedTile);
    }
    
    /**
     * @return -1 if invalid tile, 0 if first tile selected, 1 if ray is hit, 2 if ray is reflected, 3 if ray is deflected, 4 if ray is missed, 5 if ray cast
     */
    public int selectTile(Vector3 mouse)
    {
        int tileX = getTileXCoord(mouse);
        int tileY = getTileYCoord(mouse);
        
        if (Objects.nonNull(startTile) && Objects.nonNull(pointerTile)) {
            deselectTiles();
        }
        
        // regardless error checking for outside board
        if (!atoms.isExcluded(tileX, tileY)) {
            
            // first selection
            // must be an edge
            if (specialCoords.isEdge(tileX, tileY) && startTile == null) {
                System.out.println("Selected an edge tile\nSelect a valid adjacent tile");
                startTile = new CoordCell(greenify(tileX, tileY), tileX, tileY);
                return 0;
            }
            
            // second selection (if it applies)
            if (startTile != null) {
                // must be a neighbour
                pointerTile = new CoordCell(null, tileX, tileY);
                if (startTile.isNeighbour(pointerTile)) {
                    pointerTile.setTile(greenify(tileX, tileY));
                    System.out.println("Casting ray...\n");
                    // if starter tile is a corner tile (since a corner is still an edge)
                    if (specialCoords.isCorner(startTile.getX(), startTile.getY())) {
                        return rays.newRay(startTile, pointerTile);
                    }
                    // if second tile isn't an edge, cast a ray
                    else if (!specialCoords.isEdge(pointerTile.getX(), pointerTile.getY())) {
                        rays.newRay(startTile, pointerTile);
                        return rays.newRay(startTile, pointerTile);
                    }
                }
            }
        }
        // if you have gotten to this point you have failed
        System.out.println("Selected tile invalid.\n");
        deselectTiles();
        return -1;
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
    
    public void dispose()
    {
        tiledMap.dispose();
        rays.dispose();
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

class CoordCell {
    private final int x;
    private final int y;
    private TiledMapTileLayer.Cell tile;
    
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
    
    public TiledMapTileLayer.Cell setTile(TiledMapTileLayer.Cell tile)
    {
        var old = tile;
        this.tile = tile;
        return old;
    }
}
