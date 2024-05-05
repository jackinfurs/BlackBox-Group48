package com.group48.blackbox;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * This class implements the functionality of {@code Atoms} for {@code GameBoard}.
 * Allows the creation & placement of {@code Atoms} (game atoms, guess atoms, correct atoms)
 * and some utility methods for checking/revealing atom properties (position, etc.)
 * <p>
 * The usage of {@code Atoms} depends on an instance of {@link GameBoard}.
 * One must create a new instance of {@code Atoms} which inherits the current {@code GameBoard}.
 *
 * @author Jack Dunne 22483576
 * @author Harry McCormack 22513539
 * @see GameBoard
 * @since Sprint 1
 */

public class Atoms {
    private final TiledMap tiledMap;
    private final Set<String> atomCoordinates;
    private final Random random;
    private Set<String> excludedCoords;
    private TiledMapTileLayer atomsLayer;
    private TiledMapTileLayer guessAtomsLayer;
    private TiledMapTileSet tileset;
    private TiledMapTileSet guessTileSet;
    private int guessAtomsCount;
    private int correctAtomsCount;
    
    /**
     * Constructor for {@code Atoms}. Requires an instance of {@code tiledMap}.
     * <p>
     * Initialises:
     * <ul>
     *     <li>{@code this.tiledMap} to the {@code tiledMap} parameter
     *     <li>{@code atomCoordinates}, a HashSet denoting the positions of atoms
     *     <li>{@code excludedCoordinates}, a Set denoting the positions on the {@code GameBoard} where atoms cannot be placed
     *     <li>The {@code Atoms} and {@code GuessAtoms} layer for the placement of (guess) atoms.
     * </ul>
     *
     * @param tiledMap
     *         see <a href="https://libgdx.com/wiki/graphics/2d/tile-maps">Tile Maps in libGDX</a>
     *
     * @see GameBoard
     * @see #createAtoms()
     * @see #createGuessAtoms()
     */
    public Atoms(TiledMap tiledMap)
    {
        this.tiledMap = tiledMap;
        atomCoordinates = new HashSet<>();
        excludedCoords = initializeExcludedCoords();
        createAtoms();
        createGuessAtoms();
        guessAtomsCount = 0;
        correctAtomsCount = 0;
        random = new Random();
    }
    
    /**
     * Initialises {@code atomsLayer}, a <a href="https://libgdx.com/wiki/graphics/2d/tile-maps#tiled-map-layers">TiledMap tile layer</a> for atoms,
     * and creates a new tileset for the atom (for use during drawing)
     * <p>
     * This must be called in the constructor, otherwise an exception may occur when attempting to place atoms.
     *
     * @see #Atoms
     */
    private void createAtoms()
    {
        atomsLayer = new TiledMapTileLayer(9, 9, 32, 34);
        atomsLayer.setName("Atoms");
        tiledMap.getLayers().add(atomsLayer);
        
        tileset = new TiledMapTileSet();
        TiledMapTile redAtomData = new StaticTiledMapTile(new TextureRegion(new Texture("GameScreen/redAtom.png")));
        redAtomData.setId(AtomID.RED.ordinal());
        tileset.putTile(AtomID.RED.ordinal(), redAtomData);
    }
    
    /**
     * Initialises {@code guessAtomsLayer}, a <a href="https://libgdx.com/wiki/graphics/2d/tile-maps#tiled-map-layers">TiledMap tile layer</a> for guess atoms and correctly-placed atoms,
     * and creates a new tileset for guess atoms and correct atoms (for use during drawing)
     * <p>
     * This must be called in the constructor, otherwise an exception may occur when attempting to place guess atoms.
     *
     * @see #Atoms
     */
    private void createGuessAtoms()
    {
        guessAtomsLayer = new TiledMapTileLayer(9, 9, 32, 34);
        guessAtomsLayer.setName("GuessAtoms");
        tiledMap.getLayers().add(guessAtomsLayer);
        
        guessTileSet = new TiledMapTileSet();
        
        TextureRegion guessTile = new TextureRegion(new Texture("GameScreen/guessAtom.png"));
        TiledMapTile guessTileData = new StaticTiledMapTile(guessTile);
        guessTileData.setId(AtomID.GUESS.ordinal());
        guessTileSet.putTile(AtomID.GUESS.ordinal(), guessTileData);
        
        TextureRegion correctTile = new TextureRegion(new Texture("GameScreen/correctAtom.png"));
        TiledMapTile correctTileData = new StaticTiledMapTile(correctTile);
        correctTileData.setId(AtomID.CORRECT.ordinal());
        guessTileSet.putTile(AtomID.CORRECT.ordinal(), correctTileData);
    }
    
    /**
     * @see #removeAtom(int x, int y)
     */
    public void addAtom(int x, int y)
    {
        atomCoordinates.add("%d,%d".formatted(x,y));
        TiledMapTileLayer.Cell atomCell = new TiledMapTileLayer.Cell();
        atomCell.setTile(tileset.getTile(AtomID.RED.ordinal()));
        atomsLayer.setCell(x, y, atomCell);
    }
    
    /**
     * @see #addAtom(int x, int y)
     */
    public void removeAtom(int x, int y)
    {
        atomCoordinates.remove("%d,%d".formatted(x, y));
        atomsLayer.setCell(x, y, null);
    }
    
    /**
     * @see #addAtom(int x, int y)
     */
    public int addGuessAtom(int x, int y)
    {
        if (!isExcluded(x, y)) {
            if (guessAtomsLayer.getCell(x, y) == null) {
                if (guessAtomsCount != 6) {
                    TiledMapTileLayer.Cell guessAtomCell = new TiledMapTileLayer.Cell();
                    guessAtomCell.setTile(guessTileSet.getTile(AtomID.GUESS.ordinal()));
                    guessAtomsLayer.setCell(x, y, guessAtomCell);
                    System.out.printf("Guess atom #%d\n", ++guessAtomsCount);
                }
            } else {
                System.out.printf("Deselected guess atom #%d.\n", guessAtomsCount);
                guessAtomsLayer.setCell(x, y, null);
                guessAtomsCount--;
            }
            return guessAtomsCount;
        }
        System.out.println("Selected tile invalid.\n");
        return -1;
    }
    
    /**
     * @see #addAtom(int x, int y)
     */
    public void addCorrectAtom(int x, int y)
    {
        TiledMapTileLayer.Cell correctAtomCell = new TiledMapTileLayer.Cell();
        correctAtomCell.setTile(guessTileSet.getTile(AtomID.CORRECT.ordinal()));
        guessAtomsLayer.setCell(x, y, correctAtomCell);
        ++correctAtomsCount;
    }
    
    /**
     * @see #addAtom(int x, int y)
     */
    public int getGuessAtomsCount()
    {
        return guessAtomsCount;
    }
    
    public int getCorrectAtomsCount()
    {
        return correctAtomsCount;
    }
    
    /**
     * @see #addAtom(int x, int y)
     */
    public void revealAtoms()
    {
        for (String coordinate : atomCoordinates) {
            String[] parts = coordinate.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            if (guessAtomsLayer.getCell(x, y) != null)
                addCorrectAtom(x, y);
            else
                addAtom(x, y);
        }
    }
    
    /**
     * @see #placeRandomAtoms()
     */
    public int placeRandomAtom()
    {
        int x,y;
        do {
            x = random.nextInt(atomsLayer.getWidth());
            y = random.nextInt(atomsLayer.getHeight());
        } while (isExcluded(x, y) || containsAtom(x, y));
        atomCoordinates.add(x + "," + y);
        return 0;
    }
    
    /**
     * @see #placeRandomAtom()
     */
    public void placeRandomAtoms()
    {
        for (int i = 0 ; i < 6 ; ++i)
            placeRandomAtom();
    }
    
    /**
     * @see #addAtom(int x, int y)
     */
    public TiledMapTileLayer getGuessAtomsLayer()
    {
        return guessAtomsLayer;
    }
    
    /**
     * @see #addAtom(int x, int y)
     */
    public boolean isExcluded(int x, int y)
    {
        return excludedCoords.contains(x + "," + y);
    }
    
    /**
     * @see #addAtom(int x, int y)
     */
    public Set<String> getAtomCoordinates()
    {
        return atomCoordinates;
    }
    
    /**
     * @see #addAtom(int x, int y)
     */
    public boolean containsAtom(int x, int y)
    {
        return atomCoordinates.contains(x + "," + y);
    }
    
    /**
     * @see #addAtom(int x, int y)
     */
    private Set<String> initializeExcludedCoords()
    {
        if (excludedCoords == null) {
            excludedCoords = new HashSet<>();
            
            excludedCoords.add("0,0");
            excludedCoords.add("1,0");
            excludedCoords.add("7,0");
            excludedCoords.add("8,0");
            excludedCoords.add("0,1");
            excludedCoords.add("7,1");
            excludedCoords.add("8,1");
            excludedCoords.add("0,2");
            excludedCoords.add("8,2");
            excludedCoords.add("8,3");
            excludedCoords.add("8,5");
            excludedCoords.add("0,6");
            excludedCoords.add("8,6");
            excludedCoords.add("0,7");
            excludedCoords.add("7,7");
            excludedCoords.add("8,7");
            excludedCoords.add("0,8");
            excludedCoords.add("1,8");
            excludedCoords.add("7,8");
            excludedCoords.add("8,8");
        }
        return excludedCoords;
    }
    
    enum AtomID {
        RED, GUESS, CORRECT
    }
}
