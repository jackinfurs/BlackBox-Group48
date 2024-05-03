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
    private final Random random;
    private Set<String> excludedCoords;
    private TiledMapTileLayer atomsLayer;
    private TiledMapTileLayer guessAtomsLayer;
    private TiledMapTileSet tileset;
    private TiledMapTileSet guessTileset;
    private int guessAtomsCount;
    
    public Atoms(TiledMap tiledMap)
    {
        this.tiledMap = tiledMap;
        atomCoordinates = new HashSet<>();
        excludedCoords = initializeExcludedCoords();
        createAtoms();
        createGuessAtoms();
        guessAtomsCount = 0;
        random = new Random();
    }
    
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
    
    private void createGuessAtoms()
    {
        guessAtomsLayer = new TiledMapTileLayer(9, 9, 32, 34);
        guessAtomsLayer.setName("GuessAtoms");
        tiledMap.getLayers().add(guessAtomsLayer);
        
        guessTileset = new TiledMapTileSet();
        
        TextureRegion guessTile = new TextureRegion(new Texture("GameScreen/guessAtom.png"));
        TiledMapTile guessTileData = new StaticTiledMapTile(guessTile);
        guessTileData.setId(AtomID.GUESS.ordinal());
        guessTileset.putTile(AtomID.GUESS.ordinal(), guessTileData);
        
        TextureRegion correctTile = new TextureRegion(new Texture("GameScreen/correctAtom.png"));
        TiledMapTile correctTileData = new StaticTiledMapTile(correctTile);
        correctTileData.setId(AtomID.CORRECT.ordinal());
        guessTileset.putTile(AtomID.CORRECT.ordinal(), correctTileData);
    }
    
    public void addAtom(int x, int y)
    {
        TiledMapTileLayer.Cell atomCell = new TiledMapTileLayer.Cell();
        atomCell.setTile(tileset.getTile(AtomID.RED.ordinal()));
        atomsLayer.setCell(x, y, atomCell);
    }
    
    public void removeAtom(int x, int y)
    {
        atomsLayer.setCell(x, y, null);
    }
    
    public int addGuessAtom(int x, int y)
    {
        if (!isExcluded(x, y)) {
            if (guessAtomsLayer.getCell(x, y) == null) {
                if (guessAtomsCount != 6) {
                    TiledMapTileLayer.Cell guessAtomCell = new TiledMapTileLayer.Cell();
                    guessAtomCell.setTile(guessTileset.getTile(AtomID.GUESS.ordinal()));
                    guessAtomsLayer.setCell(x, y, guessAtomCell);
                    guessAtomsCount++;
                }
            } else {
                guessAtomsLayer.setCell(x, y, null);
                guessAtomsCount--;
            }
            return guessAtomsCount;
        }
        return -1;
    }
    
    public void addCorrectAtom(int x, int y)
    {
        TiledMapTileLayer.Cell correctAtomCell = new TiledMapTileLayer.Cell();
        correctAtomCell.setTile(guessTileset.getTile(AtomID.CORRECT.ordinal()));
        guessAtomsLayer.setCell(x, y, correctAtomCell);
    }
    
    public int getGuessAtomsCount()
    {
        return guessAtomsCount;
    }
    
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
    
    public int placeRandomAtom()
    {
        int x = random.nextInt(atomsLayer.getWidth());
        int y = random.nextInt(atomsLayer.getHeight());
        if (!isExcluded(x, y) && !containsAtom(x, y)) {
            atomCoordinates.add(x + "," + y);
            return 0;
        }
        return -1;
    }
    
    public void placeRandomAtoms()
    {
        for (int i = 0 ; i < 6 ; ) {
            if (placeRandomAtom() == 0)
                i++;
        }
    }
    
    public TiledMapTileLayer getGuessAtomsLayer()
    {
        return guessAtomsLayer;
    }
    
    public boolean isExcluded(int x, int y)
    {
        return excludedCoords.contains(x + "," + y);
    }
    
    public Set<String> getAtomCoordinates()
    {
        return atomCoordinates;
    }
    
    public boolean containsAtom(int x, int y)
    {
        return atomCoordinates.contains(x + "," + y);
    }
    
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
