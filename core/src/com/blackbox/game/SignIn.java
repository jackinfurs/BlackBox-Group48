package com.blackbox.game;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class SignIn {

    private static final String FILE_NAME = "users.csv";
    public static String username;

    public static String getUsername() {
        return username;
    }

    public static void localSignIn() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username:");
        username = scanner.nextLine();

        // Validate username
        if (!username.isEmpty()) {
            System.out.println("Welcome " + username + "!");
        } else {
            System.out.println("Invalid username, please enter your username");
            localSignIn();
        }
    }

    public static void saveScore(String username, int score) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bufferedWriter.write(username + "," + score);
            bufferedWriter.newLine();
            System.out.println("Score saved successfully " + username);
        } catch (IOException e) {
            System.err.println("Error saving score: " + e.getMessage());
        }
    }

    public static void readScores() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_NAME))) {
            List<Score> scores = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                String username = parts[0];
                int score = Integer.parseInt(parts[1]);
                scores.add(new Score(username, score));
            }
            Collections.sort(scores);
            int count = 0;
            for (Score score : scores) {
                System.out.println("Username: " + score.getUsername() + ", Score: " + score.getScore());
                count++;
                if (count >= 5) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading scores: " + e.getMessage());
        }
    }

    // Used to compare scores for the leaderboard
    private static class Score implements Comparable<Score> {
        private String username;
        private int score;

        public Score(String username, int score) {
            this.username = username;
            this.score = score;
        }

        public String getUsername() {
            return username;
        }

        public int getScore() {
            return score;
        }

        // Compares the scores for sorting
        @Override
        public int compareTo(Score other) {
            return Integer.compare(other.score, this.score);
        }
    }
}