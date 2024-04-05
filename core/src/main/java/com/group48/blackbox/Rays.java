package com.group48.blackbox;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.ArrayList;

//// TODO SET THE RAY TO JERRY
class Ray {
    private final CoordCell startCell;
    private final CoordCell pointerCell;
    private final TiledMapTileLayer atomsLayer;
    private final Atoms atoms;

    public Ray(CoordCell startCell, CoordCell pointerCell, TiledMapTileLayer atomsLayer, Atoms atoms)
    {
        this.startCell = startCell;
        this.pointerCell = pointerCell;
        this.atomsLayer = atomsLayer;
        this.atoms = atoms;
        cast();
    }
    public Ray(CoordCell startCell, CoordCell pointerCell) {
        this(startCell, pointerCell, null, null); // this.atoms was giving me issues so default to this
    }
    private CoordCell calculateNextCell(CoordCell currentCell, CoordCell targetCell) {
        int currentX = currentCell.getX();
        int currentY = currentCell.getY();
        int targetX = targetCell.getX();
        int targetY = targetCell.getY();

        // Calculate the direction of movement
        int dx = targetX - currentX;
        int dy = targetY - currentY;

        // Adjust the movement direction based on the coordinate system used
        if (currentY % 2 == 1) {
            dx -= (currentY + 1) / 2;
            targetX -= (currentY + 1) / 2;
        } else {
            dx -= currentY / 2;
            targetX -= currentY / 2;
        }

        // Determine the next cell based on the direction of movement
        int nextX = currentX;
        int nextY = currentY;

        if (dx > 0) {
            nextX++;
        } else if (dx < 0) {
            nextX--;
        }

        if (dy > 0) {
            nextY++;
        } else if (dy < 0) {
            nextY--;
        }

        // Check if the next cell is within the bounds of the map
        if (nextX >= 0 && nextX < atomsLayer.getWidth() && nextY >= 0 && nextY < atomsLayer.getHeight()) {
            return new CoordCell(atomsLayer.getCell(nextX, nextY), nextX, nextY);
        } else {
            return null; // Return null if the next cell is out of bounds
        }
    }

    public void show()
    {
        // change opacity of line
    }

    public void cast()
    {
        if (atoms == null) {
            System.out.println("Atoms instance not provided.");
            return; // Don't proceed with casting if atoms instance is not provided
        }
        // get world x,y coords of both tiles
        CoordCell nextCell = calculateNextCell(startCell, pointerCell);

        if (nextCell != null) {
            // Check if the next cell contains an atom
            if (atoms.isAtomAt(nextCell.getX(), nextCell.getY())) {
                System.out.println("Direct hit!"); // Register direct hit in terminals
            }
        }
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
    private TiledMapTileLayer atomsLayer;
    private ArrayList<Ray> rayList; // RayList = ArrayList - Ar. sidesplitting.
    private ShapeRenderer shapeRenderer;
    private Atoms atoms;

    public Rays(TiledMap tiledMap)
    {
        atomsLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Atoms");
        rayList = new ArrayList<>();
        shapeRenderer = new ShapeRenderer();
        atoms = new Atoms(tiledMap); // Initialize the Atoms instance
    }

    // https://www.redblobgames.com/grids/hexagons/
    public void newRay(CoordCell startTile, CoordCell pointerTile)
    {
        Ray ray = new Ray(startTile, pointerTile, atomsLayer, atoms);
        rayList.add(ray);
    }

    public void render()
    {
        // iterate through RayList, call show on each
        for (Ray ray : rayList) {
            ray.show();
        }
    }

    public void dispose()
    {
        rayList.clear();
        shapeRenderer.dispose();
    }
}
