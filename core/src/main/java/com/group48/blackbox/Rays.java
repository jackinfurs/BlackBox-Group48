package com.group48.blackbox;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import java.util.ArrayList;

//
//// SET THE RAY TO JERRY
class Ray {
    private CoordCell startCell, pointerCell;
    private boolean isShown;
    
    public Ray(CoordCell startCell, CoordCell pointerCell)
    {
        this.startCell = startCell;
        this.pointerCell = pointerCell;
    }
    
    public boolean isShown()
    {
        return isShown;
    }
    
    public void setShown(boolean shown)
    {
        this.isShown = shown;
    }
    
    // TODO
    public void cast()
    {
        // get world x,y coords of both tiles
        
        // draw line between the two
        
        // pedal it back by half the length in the other direction of the angle
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
    
    //    // TODO change opacity of line
    //    public void draw()
    //    {
    //        //        for (Ray ray : RayList) {
    //        //            if (!ray.isShown()) {
    //        //
    //        //            }
    //        //        }
    //    }
    
    // https://www.redblobgames.com/grids/hexagons/
    public void newRay(CoordCell startTile, CoordCell pointerTile)
    {
        Ray ray = new Ray(startTile, pointerTile);
        RayList.add(ray);
        ray.cast();
    }
    
    public void dispose()
    {
        RayList = null;
        shapeRenderer.dispose();
    }
}
