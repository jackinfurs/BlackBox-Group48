package com.group48.blackbox;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.ArrayList;

//// TODO SET THE RAY TO JERRY
class Ray {
    private final CoordCell startCell;
    private final CoordCell pointerCell;
    
    // random colour here except red, yellow
    
    public Ray(CoordCell startCell, CoordCell pointerCell)
    {
        this.startCell = startCell;
        this.pointerCell = pointerCell;
        cast();
    }
    
    public void show()
    {
        // change opacity of line
    }
    
    public void cast()
    {
        // get world x,y coords of both tiles
        
        // draw line between the two
        
        // startLine = pedal it back by half the length in the other direction of the angle
        
        // ray logic
        // if tile directly ahead has an atom, set ray to red
        // if tile slightly left/right has an atom, change direction by 30 degrees
        // if no tile ahead (end of board) render endLine
        // else (no problems) advance forward one tile
    }
}

public class Rays {
    TiledMapTileLayer atomsLayer;
    ArrayList<Ray> RayList; // RayList = ArrayList - Ar. sidesplitting.
    ShapeRenderer shapeRenderer;
    
    public Rays(TiledMap tiledMap)
    {
        atomsLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Atoms");
        RayList = new ArrayList<>();
        shapeRenderer = new ShapeRenderer();
    }
    
    // https://www.redblobgames.com/grids/hexagons/
    public void newRay(CoordCell startTile, CoordCell pointerTile)
    {
        Ray ray = new Ray(startTile, pointerTile);
        RayList.add(ray);
    }
    
    public void render() {
        // iterate through RayList, call show on each
        for (Ray r : RayList) r.show();
    }
    
    public void dispose()
    {
        RayList = null;
        shapeRenderer.dispose();
    }
}
