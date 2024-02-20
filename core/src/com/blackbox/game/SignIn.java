package com.blackbox.game;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SignIn {

    private static final String FILE_NAME = "users.csv";

    public static String localSignIn() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username:");
        String username = scanner.nextLine();

        // Validate username
        if (!username.isEmpty()) {
            System.out.println("Welcome " + username + "!");
        } else {
            System.out.println("Invalid username, please enter your username");
            localSignIn();
        }
        return username;
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
}