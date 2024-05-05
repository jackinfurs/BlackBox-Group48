package com.group48.blackbox;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Score {
    private final Atoms atoms;
    boolean gamefinished = false;
    private ScoresManager scoresManager;
    private int score = 0;
    
    // Constructor takes an instance of Atoms to access its data
    public Score(Atoms atoms)
    {
        this.atoms = atoms;
        this.scoresManager = scoresManager;
    }
    
    // Method to calculate the atoms score
    public int calculateScore()
    {
        
        TiledMapTileLayer guessAtomsLayer = atoms.getGuessAtomsLayer();
        
        if (gamefinished) {
            for (int x = 0 ; x < guessAtomsLayer.getWidth() ; x++) {
                for (int y = 0 ; y < guessAtomsLayer.getHeight() ; y++) {
                    if (guessAtomsLayer.getCell(x, y) != null && !atoms.containsAtom(x, y)) {
                        score += 5;
                    }
                }
            }
            ScoresManager.addScore(UsersManager.getUsername(), score);
            gamefinished = false;
        }
        
        // ray points for deflections, hits, reflections to be implemented here
        
        return score;
    }
    
    public void setGameFinished(boolean b)
    {
        gamefinished = b;
    }
}
