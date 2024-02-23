package com.blackbox.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

public class Atoms {
    private TiledMapTileLayer atomsLayer;
    private TiledMapTileSet tileset;
    private final TiledMap tiledMap;

    public Atoms(TiledMap tiledMap){
        this.tiledMap = tiledMap;
        createAtoms();
    }

    private void createAtoms(){
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

    public void addAtom(int x, int y) {
        TiledMapTileLayer.Cell atomCell = new TiledMapTileLayer.Cell();
        atomCell.setTile(tileset.getTile(1));
        atomsLayer.setCell(x, y, atomCell);
    }
}
