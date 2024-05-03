package com.group48.blackbox;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Score{
    private final Atoms atoms;

    // Constructor takes an instance of Atoms to access its data
    public Score(Atoms atoms) {
        this.atoms = atoms;
    }
    boolean gamefinished = false;
    // Method to calculate the atoms score
    public int calculateScore() {
        int score = 0;

        TiledMapTileLayer guessAtomsLayer = atoms.getGuessAtomsLayer();

        if(gamefinished) {
            for (int x = 0; x < guessAtomsLayer.getWidth(); x++) {
                for (int y = 0; y < guessAtomsLayer.getHeight(); y++) {
                    if (guessAtomsLayer.getCell(x, y) != null && !atoms.containsAtom(x, y)) {
                        score += 5;
                    }
                }
            }
        }

        // ray point for deflections, hits, reflections to be implemented here

        return score;
    }
}
