package com.group48.blackbox;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Score{
    private Atoms atoms;

    // Constructor takes an instance of Atoms to access its data
    public Score(Atoms atoms) {
        this.atoms = atoms;
    }

    // Method to calculate the score
    public int calculateScore() {
        int score = 0;
        TiledMapTileLayer guessAtomsLayer = atoms.getGuessAtomsLayer();

        for (int x = 0; x < guessAtomsLayer.getWidth(); x++) {
            for (int y = 0; y < guessAtomsLayer.getHeight(); y++) {
                if (guessAtomsLayer.getCell(x, y) != null && !atoms.isAtomAt(x, y)) {
                    score += 5;
                }
            }
        }

        return score;
    }
}
