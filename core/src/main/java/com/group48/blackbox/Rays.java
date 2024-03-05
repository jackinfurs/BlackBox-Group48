package com.group48.blackbox;

import java.util.HashSet;
import java.util.Set;

class Ray {
    int start;
    int end;
    int angle;

    

    private void draw() {

    }
}

public class Rays extends BlackBox {
    Set<Ray> RayMap;

    public Rays() {
        RayMap = new HashSet<>();
    }

}
