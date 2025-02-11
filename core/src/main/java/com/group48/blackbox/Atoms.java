package com.group48.blackbox;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector3;
import com.group48.blackbox.Screens.TutorialScreen;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * This class implements the functionality of atoms in Black Box+ for {@code GameBoard}.
 * Allows the creation & placement of atoms (game atoms, guess atoms, correct atoms)
 * and some utility methods for checking/revealing atom properties (position, etc.)
 * <p>
 * The usage of {@code Atoms} depends on an instance of {@link GameBoard}.
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
    private final Set<String> excludedCoords;
    private TiledMapTileLayer atomsLayer;
    private TiledMapTileLayer guessAtomsLayer;
    private TiledMapTileSet tileset;
    private TiledMapTileSet guessTileSet;
    private int guessAtomsCount;
    private int correctAtomsCount;
    
    /**
     * Constructor for {@code Atoms}. Requires an instance of {@code tiledMap} as a parameter.
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
     * Initialises {@code atomsLayer},
     * a <a href="https://libgdx.com/wiki/graphics/2d/tile-maps#tiled-map-layers">TiledMap tile layer</a> for atom placement,
     * and creates a new {@link TiledMapTileSet} for the atom (for use during drawing)
     * <p>
     * This must be called in the constructor,
     * otherwise an exception may occur when attempting to place atoms.
     *
     * @throws NullPointerException
     *         if class is not constructed properly
     * @see #Atoms
     * @see #addAtom(int, int)
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
     * Initialises {@code guessAtomsLayer}, a <a href="https://libgdx.com/wiki/graphics/2d/tile-maps#tiled-map-layers">TiledMap tile layer</a> for the placement of guess atoms and correctly-placed atoms,
     * and creates a new {@link TiledMapTileSet} for guess atoms and correct atoms (for use during drawing)
     * <p>
     * This must be called in the constructor,
     * otherwise an exception may occur when attempting to place guess/correct atoms.
     *
     * @throws NullPointerException
     *         if class is not constructed properly
     * @see #Atoms
     * @see #addGuessAtom(int, int)
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
     * Adds an atom to the specified x-y coordinate on the {@link TiledMap} (without hiding it).
     * <p>
     * Used mostly in {@code TutorialScreen} for the placement of demonstrative atoms displaying the function of {@link Rays},
     * but can also be used during debugging.
     * <p>
     * Since the parameters are explicitly known at compile time,
     * the method expects <b>valid</b> x-y coordinates as a parameter (x, y < 9 AND not in {@code excludedCoords}).
     * Otherwise, an {@code IllegalArgumentException} will be thrown.
     *
     * @param x
     *         the x-coordinate of the desired atom's placement on the {@code TiledMap}
     * @param y
     *         the y-coordinate of the desired atom's placement on the {@code TiledMap}
     *
     * @throws IllegalArgumentException
     *         if invalid x-y coordinates are provided
     * @see #removeAtom(int x, int y)
     * @see TiledMap
     * @see Rays
     * @see TutorialScreen
     */
    public void addAtom(int x, int y)
    {
        if (excludedCoords.contains("%d,%d".formatted(x, y)) || x > 9 || y > 9)
            throw new IllegalArgumentException("invalid coordinates");
        atomCoordinates.add("%d,%d".formatted(x, y));
        TiledMapTileLayer.Cell atomCell = new TiledMapTileLayer.Cell();
        atomCell.setTile(tileset.getTile(AtomID.RED.ordinal()));
        atomsLayer.setCell(x, y, atomCell);
    }
    
    /**
     * Removes an atom at the specified x-y coordinate on the {@link TiledMap}.
     * <p>
     * Used mostly in {@code TutorialScreen} for the removal of demonstrative atoms.
     * <p>
     * Since the parameters are explicitly known at compile time, the method expects <b>valid</b> x-y coordinates as a parameter (x, y < 9 AND not in {@code excludedCoords}).
     * Otherwise, an {@code IllegalArgumentException} will be thrown.
     *
     * @param x
     *         the x-coordinate of the desired atom's removal from the {@code TiledMap}
     * @param y
     *         the y-coordinate of the desired atom's removal from the {@code TiledMap}
     *
     * @throws IllegalArgumentException
     *         if invalid x-y coordinates are provided
     * @see #addAtom(int, int)
     * @see TiledMap
     * @see TutorialScreen
     */
    public void removeAtom(int x, int y)
    {
        if (excludedCoords.contains("%d,%d".formatted(x, y)) || x > 9 || y > 9)
            throw new IllegalArgumentException("invalid coordinates");
        atomCoordinates.remove("%d,%d".formatted(x, y));
        atomsLayer.setCell(x, y, null);
    }
    
    /**
     * Adds/removes a guess atom at the specified x-y coordinate on the {@link TiledMap},
     * depending on whether a guess atom has already been placed at the tile.
     * <p>
     * Usually called whenever the user right-clicks on the TiledMap to place/remove a guess atom,
     * but can also be called explicitly (e.g. in TutorialScreen)
     *
     * @param x
     *         the x-coordinate of the guess atom's placement on the {@code TiledMap}
     * @param y
     *         the y-coordinate of the guess atom's placement on the {@code TiledMap}
     *
     * @see #addGuessAtom(int, int)
     * @see TiledMap
     * @see GameScreen
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
                System.out.printf("Deselected guess atom #%d\n", guessAtomsCount);
                guessAtomsLayer.setCell(x, y, null);
                guessAtomsCount--;
            }
            return guessAtomsCount;
        }
        System.out.println("Selected tile invalid.\n");
        return -1;
    }
    
    /**
     * Adds a 'correct' atom to the specified x-y coordinate on the TiledMap.
     * <p>
     * Used to display which guesses were correct to the user.
     * <p>
     * Since the parameters are explicitly known at compile time, the method expects <b>valid</b> x-y coordinates as a parameter (x, y < 9 AND not in {@code excludedCoords}).
     * Otherwise, an {@code IllegalArgumentException} will be thrown.
     *
     * @param x
     *         the x-coordinate of the desired atom's placement on the {@code TiledMap}
     * @param y
     *         the y-coordinate of the desired atom's placement on the {@code TiledMap}
     *
     * @throws IllegalArgumentException
     *         if invalid x-y coordinates are provided
     * @see #removeAtom(int x, int y)
     * @see TiledMap
     * @see TutorialScreen
     * @see GameScreen
     */
    public void addCorrectAtom(int x, int y)
    {
        if (excludedCoords.contains("%d,%d".formatted(x, y)) || x > 9 || y > 9)
            throw new IllegalArgumentException("invalid coordinates");
        
        TiledMapTileLayer.Cell correctAtomCell = new TiledMapTileLayer.Cell();
        correctAtomCell.setTile(guessTileSet.getTile(AtomID.CORRECT.ordinal()));
        guessAtomsLayer.setCell(x, y, correctAtomCell);
        ++correctAtomsCount;
    }
    
    public int getGuessAtomsCount()
    {
        return guessAtomsCount;
    }
    
    public int getCorrectAtomsCount()
    {
        return correctAtomsCount;
    }
    
    /**
     * Reveals all atoms on the TiledMap. If a guess atom was correctly placed by the user, a correct guess atom is placed.
     * <p>
     * Coordinates must be added to the {@code atomCoordinates} HashMap in order for any atoms to be placed.
     * This also assumes that the coordinates placed into {@code atomCoordinates} are valid.
     * A TiledMap instance must also have been provided during construction.
     *
     * @see #addAtom(int x, int y)
     * @see GameBoard
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
     * Places a hidden atom at a random, valid tile on the game board.
     * <p>
     * This is most often used in {@link #placeRandomAtoms()}, where multiple calls are made.
     * <p>
     * A TiledMap must be constructed, along with an instance of {@link Random}.
     *
     * @see #placeRandomAtoms()
     * @see GameBoard
     */
    public int placeRandomAtom()
    {
        int x, y;
        do {
            x = random.nextInt(atomsLayer.getWidth());
            y = random.nextInt(atomsLayer.getHeight());
        } while (isExcluded(x, y) || containsAtom(x, y));
        atomCoordinates.add(x + "," + y);
        return 0;
    }
    
    /**
     * Places multiple atoms at random, making repeated calls to {@link #placeRandomAtom()}.
     *
     * @see #placeRandomAtom()
     * @see GameBoard
     */
    public void placeRandomAtoms()
    {
        for (int i = 0 ; i < 6 ; ++i)
            placeRandomAtom();
    }
    
    public TiledMapTileLayer getGuessAtomsLayer()
    {
        return guessAtomsLayer;
    }
    
    /**
     * Used to check if a coordinate provided as a parameter is an excluded coordinate.
     * <p>
     * This is used mostly in error checking to prevent errors and crashes.
     *
     * @param x
     *         the x-coordinate of the proposed excluded atom
     * @param y
     *         the y-coordinate of the proposed excluded atom
     *
     * @return true if provided atom is in {@code excludedCoords}
     *
     * @see #addGuessAtom(int x, int y)
     * @see #placeRandomAtom()
     * @see GameBoard#selectTile(Vector3)
     * @see #initializeExcludedCoords()
     */
    public boolean isExcluded(int x, int y)
    {
        return excludedCoords.contains(x + "," + y);
    }
    
    public Set<String> getAtomCoordinates()
    {
        return atomCoordinates;
    }
    
    /**
     * Used to check if a coordinate provided contains an atom on the {@link GameBoard}.
     * <p>
     * Mostly used for scoring and error checking during random atom placement.
     *
     * @param x
     *         the x-coordinate of the proposed atom at tile
     * @param y
     *         the y-coordinate of the proposed atom at tile
     *
     * @return true if provided atom is in {@code atomCoordinates}
     *
     * @see #placeRandomAtom()
     * @see Ray#cast()
     * @see Score#calculateScore()
     */
    public boolean containsAtom(int x, int y)
    {
        return atomCoordinates.contains(x + "," + y);
    }
    
    /**
     * Initialises the {@link GameBoard}'s excluded coordinates.
     * <p>
     * As the shape of the game board in Black Box+ (made of hexagons) is itself a hexagon,
     * naturally there will be some coordinates that are impossible to reach
     * as a result of most {@link TiledMap}s typically being square/rectangular in nature.
     * <p>
     * The use of excluded coordinates is used in error checking during random atom placement or user input.
     * Without proper checking, an atom could be placed out of bounds,
     * or a user could crash the game by placing a guess atom
     * or casting a ray into an unrendered tile.
     *
     * @return a HashMap of strings containing the coordinates of excluded tiles
     *
     * @see #addAtom(int x, int y)
     * @see #isExcluded(int, int)
     */
    private Set<String> initializeExcludedCoords()
    {
        Set<String> coords = new HashSet<>();
        
        coords.add("0,0");
        coords.add("1,0");
        coords.add("7,0");
        coords.add("8,0");
        coords.add("0,1");
        coords.add("7,1");
        coords.add("8,1");
        coords.add("0,2");
        coords.add("8,2");
        coords.add("8,3");
        coords.add("8,5");
        coords.add("0,6");
        coords.add("8,6");
        coords.add("0,7");
        coords.add("7,7");
        coords.add("8,7");
        coords.add("0,8");
        coords.add("1,8");
        coords.add("7,8");
        coords.add("8,8");
        
        return coords;
    }
    
    enum AtomID {
        RED, GUESS, CORRECT
    }
}
