package com.group48.blackbox;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.collision.Ray;

import java.util.ArrayList;
//
//// SET THE RAY TO JERRY
//class Ray {
//    private int startX, startY;
//    private int endX, endY;
//    private int direction;
//    private String outcome;
//    private boolean isShown;
//
//    public Ray(int tileX, int tileY, char direction)
//    {
//        outcome = "";
//        this.tileX = tileX;
//        this.tileY = tileY;
//        this.direction = direction;
//    }
//
//    public boolean isShown()
//    {
//        return isShown;
//    }
//
//    public void setShown(boolean shown)
//    {
//        this.isShown = shown;
//    }
//
//    // TODO
//    public void cast()
//    {
//
//    }
//}

public class Rays extends BlackBox {
    TiledMapTileLayer atomsLayer;
    ArrayList<Ray> RayList; // RayList = ArrayList - Ar. sidesplitting.
    
    public Rays(TiledMap tiledMap)
    {
        atomsLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Atoms");
        RayList = new ArrayList<>();
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
    public void newRay(TiledMapTileLayer.Cell startTile, TiledMapTileLayer.Cell pointerTile)
    {
        // get the x,y coords of both tiles
        // draw line between the two
        // pedal it back by half the length in the other direction of the angle
        
        //        RayList.add(new Ray(tileX, tileY, direction));
    }
    
    public void clear() {
        RayList = null;
    }
}
