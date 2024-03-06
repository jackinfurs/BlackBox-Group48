package com.group48.blackbox;

import com.badlogic.gdx.maps.tiled.TiledMap;

import java.util.ArrayList;

// SET THE RAY TO JERRY
class Ray {
    private final int tileX;
    private final int tileY;
    private final char direction;
    private final String outcome;
    private boolean isShown;
    
    public Ray(int tileX, int tileY, char direction)
    {
        outcome = "";
        this.tileX = tileX;
        this.tileY = tileY;
        this.direction = direction;
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
    
    }
}

public class Rays extends BlackBox {
    TiledMap tiledMap;
    // RayList = ArrayList - Ar. sidesplitting.
    ArrayList<Ray> RayList;
    
    public Rays(TiledMap tiledMap)
    {
        this.tiledMap = tiledMap;
        RayList = new ArrayList<>();
    }
    
    public void newRay(int tileX, int tileY, char direction)
    {
        RayList.add(new Ray(tileX, tileY, direction));
    }
    
    // TODO change opacity of line
    public void draw()
    {
        //        for (Ray ray : RayList) {
        //            if (!ray.isShown()) {
        //
        //            }
        //        }
    }
}
