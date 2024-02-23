package com.blackbox.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class Atoms {
    private TiledMapTileLayer atomsLayer;
    private TiledMapTileLayer guessAtomsLayer; // Layer for guess atoms
    private TiledMapTileSet tileset;
    private TiledMapTileSet guessTileset; // Tileset for guess atoms
    private final TiledMap tiledMap;
    private Array<int[]> hiddenAtomPositions = new Array<>();

    public Atoms(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        createAtoms();
        createGuessAtoms();
    }

    private void createAtoms() {
        MapLayers layers = tiledMap.getLayers();
        atomsLayer = new TiledMapTileLayer(9, 9, 32, 34);
        atomsLayer.setName("Atoms");
        tiledMap.getLayers().add(atomsLayer);

        tileset = new TiledMapTileSet();
        TextureRegion tile1 = new TextureRegion(new Texture("GameScreen/redAtom.png"));
        TiledMapTile tile1Data = new StaticTiledMapTile(tile1);
        tile1Data.setId(1);
        tileset.putTile(1, tile1Data);
    }

    private void createGuessAtoms() {
        guessAtomsLayer = new TiledMapTileLayer(9, 9, 32, 34);
        guessAtomsLayer.setName("GuessAtoms");
        tiledMap.getLayers().add(guessAtomsLayer);

        guessTileset = new TiledMapTileSet();
        TextureRegion guessTile = new TextureRegion(new Texture("GameScreen/guessAtom.png"));
        TiledMapTile guessTileData = new StaticTiledMapTile(guessTile);
        guessTileData.setId(2);
        guessTileset.putTile(2, guessTileData);
    }

    public void addAtom(int x, int y) {
        TiledMapTileLayer.Cell atomCell = new TiledMapTileLayer.Cell();
        atomCell.setTile(tileset.getTile(1));
        atomsLayer.setCell(x, y, atomCell);
    }

    public void addGuessAtom(int x, int y) {
        TiledMapTileLayer.Cell guessAtomCell = new TiledMapTileLayer.Cell();
        guessAtomCell.setTile(guessTileset.getTile(2));
        guessAtomsLayer.setCell(x, y, guessAtomCell);
    }

    public void placeRandomAtoms() {
        Random random = new Random();
        int totalAtoms = 6;
        int gridWidth = atomsLayer.getWidth();
        int gridHeight = atomsLayer.getHeight();

        for (int i = 0; i < totalAtoms; i++) {
            int x, y;
            do {
                x = random.nextInt(gridWidth);
                y = random.nextInt(gridHeight);
            } while (cellIsOccupied(x, y)); // Check if the randomly selected cell is already occupied

            addAtom(x, y); // Add an atom to the randomly selected position
        }
    }

    private boolean cellIsOccupied(int x, int y) {
        return atomsLayer.getCell(x, y) != null;
    }
}