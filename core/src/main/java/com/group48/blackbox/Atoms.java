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

public class Atoms {
    private final TiledMap tiledMap;
    private final Set<String> atomCoordinates;
    private TiledMapTileLayer atomsLayer;
    private TiledMapTileLayer guessAtomsLayer; // Layer for guess atoms
    private TiledMapTileSet tileset;
    private TiledMapTileSet guessTileset; // Tileset for guess atoms
    private int guessAtomsCount;
    private Set<String> excludedCoords;

    public Atoms(TiledMap tiledMap)
    {
        this.tiledMap = tiledMap;
        initializeExcludedCoords();
        atomCoordinates = new HashSet<>();
        createAtoms();
        createGuessAtoms();
        guessAtomsCount = 0;
    }

    private void createAtoms()
    {
        atomsLayer = new TiledMapTileLayer(9, 9, 32, 34);
        atomsLayer.setName("Atoms");

        tiledMap.getLayers().add(atomsLayer);

        tileset = new TiledMapTileSet();
        TextureRegion tile1 = new TextureRegion(new Texture("GameScreen/redAtom.png"));
        TiledMapTile tile1Data = new StaticTiledMapTile(tile1);
        tile1Data.setId(1);
        tileset.putTile(1, tile1Data);
    }

    private void createGuessAtoms()
    {
        guessAtomsLayer = new TiledMapTileLayer(9, 9, 32, 34);
        guessAtomsLayer.setName("GuessAtoms");
        tiledMap.getLayers().add(guessAtomsLayer);

        guessTileset = new TiledMapTileSet();
        TextureRegion guessTile = new TextureRegion(new Texture("GameScreen/guessAtom.png"));
        TiledMapTile guessTileData = new StaticTiledMapTile(guessTile);
        guessTileData.setId(2);
        guessTileset.putTile(2, guessTileData);
    }

    public void addAtom(int x, int y)
    {
        TiledMapTileLayer.Cell atomCell = new TiledMapTileLayer.Cell();
        atomCell.setTile(tileset.getTile(1));
        atomsLayer.setCell(x, y, atomCell);
    }

    public void removeAtom(int x, int y)
    {
        atomsLayer.setCell(x, y, null);
    }

    public int addGuessAtom(int x, int y)
    {
        if (guessAtomsCount != 6 &&
                guessAtomsLayer.getCell(x, y) == null &&
                !isExcluded(x, y)) {
            TiledMapTileLayer.Cell guessAtomCell = new TiledMapTileLayer.Cell();
            guessAtomCell.setTile(guessTileset.getTile(2));
            guessAtomsLayer.setCell(x, y, guessAtomCell);
            guessAtomsCount++;
            return 0;
        } else if (guessAtomsLayer.getCell(x, y) != null) {
            guessAtomsLayer.setCell(x, y, null);
            guessAtomsCount--;
            return 0;
        }
        return -1;
    }

    public int getGuessAtomsCount() {return guessAtomsCount;}

    public void revealAtoms()
    {
        for (String coordinate : atomCoordinates) {
            String[] parts = coordinate.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            addAtom(x, y);
        }
    }

    public int placeRandomAtom()
    {
        Random random = new Random();
        int x = random.nextInt(atomsLayer.getWidth());
        int y = random.nextInt(atomsLayer.getHeight());
        if (!isExcluded(x, y) && !cellIsOccupied(x, y)) {
            System.out.printf("placed random atom at %d,%d\n", x, y);
            atomCoordinates.add(x + "," + y);
            return 0;
        } else return -1;
    }

    public void placeRandomAtoms()
    {
        for (int i = 0 ; i < 6 ; ) {
            if (placeRandomAtom() == 0) i++;
        }
    }

    public TiledMapTileLayer getGuessAtomsLayer() {
        return guessAtomsLayer;
    }

    private boolean cellIsOccupied(int x, int y)
    {
        return atomCoordinates.contains(x + "," + y);
    }

    public boolean isExcluded(int x, int y)
    {
        return excludedCoords.contains(x + "," + y);
    }

    public Set<String> getAtomCoordinates()
    {
        return atomCoordinates;
    }

    public boolean isAtomAt(int x, int y)
    {
        return atomCoordinates.contains(x + "," + y);
    }

    private void initializeExcludedCoords()
    {

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
}
