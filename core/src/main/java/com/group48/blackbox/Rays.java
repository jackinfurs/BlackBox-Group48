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
    TiledMap tiledMap;
    // RayList = ArrayList - Ar. sidesplitting.
    ArrayList<Ray> RayList;
    
    public Rays(TiledMap tiledMap)
    {
        this.tiledMap = tiledMap;
        RayList = new ArrayList<>();
    }
    
    public void draw()
    // TODO change opacity of line
    {
        //        for (Ray ray : RayList) {
        //            if (!ray.isShown()) {
        //
        //            }
        //        }
        
    }
    
    // https://www.redblobgames.com/grids/hexagons/
    public void newRay(TiledMapTileLayer.Cell startTile, TiledMapTileLayer.Cell pointerTile)
    {
        // get the x,y coords of both tiles
        // determine the length (startTile.x - endTile.x)
        // determine the angle relative to the origin
        // pedal it back by half the length in the other direction of the angle
        
        //        RayList.add(new Ray(tileX, tileY, direction));
    }
}
