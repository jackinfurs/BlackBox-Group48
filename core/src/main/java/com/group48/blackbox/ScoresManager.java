package com.group48.blackbox;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScoresManager {
    static final String SCORES_FILE = "scores.csv";
    private static final int MAX_SCORES = 10;
    
    public static void addScore(String username, int score)
    {
        List<ScoreEntry> entries = loadScores();
        
        entries.add(new ScoreEntry(username, score));
        
        entries.sort((e1, e2) -> Integer.compare(e1.getScore(), e2.getScore()));
        
        // If there are more than MAX_SCORES, remove the lowest scores
        if (entries.size() > MAX_SCORES) {
            entries = entries.subList(0, MAX_SCORES);
        }
        
        // Save the sorted and possibly truncated scores back to the file
        saveScores(entries);
    }
    
    static List<ScoreEntry> loadScores()
    {
        List<ScoreEntry> entries = new ArrayList<>();
        File file = new File(SCORES_FILE);
        if (!file.exists()) {
            return entries; // Return an empty list if the file does not exist
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    entries.add(new ScoreEntry(parts[0].trim(), Integer.parseInt(parts[1].trim())));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading scores: " + e.getMessage());
        }
        return entries;
    }
    
    private static void saveScores(List<ScoreEntry> entries)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORES_FILE, false))) {
            for (ScoreEntry entry : entries) {
                writer.write(entry.getUsername() + "," + entry.getScore());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing scores: " + e.getMessage());
        }
    }
    
    static class ScoreEntry {
        private final String username;
        private final int score;
        
        public ScoreEntry(String username, int score)
        {
            this.username = username;
            this.score = score;
        }
        
        public String getUsername()
        {
            return username;
        }
        
        public int getScore()
        {
            return score;
        }
    }
}
