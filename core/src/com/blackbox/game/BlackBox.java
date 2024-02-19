package com.blackbox.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BlackBox extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Texture playButtonTexture;
    private Texture signUpButtonTexture;
    private Texture signInButtonTexture;
    private Texture leaderboardButtonTexture;
    private Texture exitButtonTexture;
    private boolean playButtonClicked;
    private boolean signUpButtonClicked;
    private boolean signInButtonClicked;
    private boolean leaderboardButtonClicked;
    private boolean exitButtonClicked;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Load menu assets/pngs
        backgroundTexture = new Texture("VaporBackground.png");
        playButtonTexture = new Texture("play.png");
        signUpButtonTexture = new Texture("sign up.png");
        signInButtonTexture = new Texture("sign in.png");
        leaderboardButtonTexture = new Texture("Leaderboard.png");
        exitButtonTexture = new Texture("Exit.png");


        Gdx.input.setInputProcessor(new InputHandler());
    }

    @Override
    public void render() {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the main menu
        batch.begin();

        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw play button
        float buttonWidth = 200;
        float buttonHeight = 50;
        float playButtonX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
        float playButtonY = 300;
        batch.draw(playButtonTexture, playButtonX, playButtonY, buttonWidth, buttonHeight);

        // Draw sign-up button
        float signUpButtonX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
        float signUpButtonY = 400;
        batch.draw(signUpButtonTexture, signUpButtonX, signUpButtonY, buttonWidth, buttonHeight);

        // Draw sign-in button
        float signInButtonX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
        float signInButtonY = 500;
        batch.draw(signInButtonTexture, signInButtonX, signInButtonY, buttonWidth, buttonHeight);

        // Draw leaderboard button
        float leaderboardButtonX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
        float leaderboardButtonY = 600;
        batch.draw(leaderboardButtonTexture, leaderboardButtonX, leaderboardButtonY, buttonWidth, buttonHeight);

        // Draw exit button
        float exitButtonX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
        float exitButtonY = 200;
        batch.draw(exitButtonTexture, exitButtonX, exitButtonY, buttonWidth, buttonHeight);

        batch.end();

        // Process button clicks
        if (playButtonClicked) {
            System.out.println("Play button clicked!");
            playButtonClicked = false; // Reset
        }

        if (signUpButtonClicked) {
            System.out.println("Sign Up button clicked!");
            signUpButtonClicked = false; // Reset
        }

        if (signInButtonClicked) {
            System.out.println("Sign In button clicked!");
            signInButtonClicked = false; // Reset
        }

        if (leaderboardButtonClicked) {
            System.out.println("Leaderboard button clicked!");
            leaderboardButtonClicked = false; // Reset
        }

        if (exitButtonClicked) {
            System.out.println("Exit button clicked!");
            Gdx.app.exit(); // Exit the application
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
        playButtonTexture.dispose();
        signUpButtonTexture.dispose();
        signInButtonTexture.dispose();
        leaderboardButtonTexture.dispose();
        exitButtonTexture.dispose();
    }

    private class InputHandler extends InputAdapter {
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            // Convert screen coordinates to world coordinates
            float worldX = screenX * Gdx.graphics.getWidth() / (float) Gdx.graphics.getWidth();
            float worldY = (Gdx.graphics.getHeight() - screenY) * Gdx.graphics.getHeight() / (float) Gdx.graphics.getHeight();

            // Check if the touch is within the bounds of the play button
            float buttonWidth = 200;
            float buttonHeight = 50;
            float playButtonX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
            float playButtonY = 300;
            if (worldX >= playButtonX && worldX <= playButtonX + buttonWidth &&
                    worldY >= playButtonY && worldY <= playButtonY + buttonHeight) {
                playButtonClicked = true;
            }

            // Check if the touch is within the bounds of the sign-up button
            float signUpButtonX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
            float signUpButtonY = 400;
            if (worldX >= signUpButtonX && worldX <= signUpButtonX + buttonWidth &&
                    worldY >= signUpButtonY && worldY <= signUpButtonY + buttonHeight) {
                signUpButtonClicked = true;
            }

            // Check if the touch is within the bounds of the sign-in button
            float signInButtonX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
            float signInButtonY = 500;
            if (worldX >= signInButtonX && worldX <= signInButtonX + buttonWidth &&
                    worldY >= signInButtonY && worldY <= signInButtonY + buttonHeight) {
                signInButtonClicked = true;
            }

            // Check if the touch is within the bounds of the leaderboard button
            float leaderboardButtonX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
            float leaderboardButtonY = 600;
            if (worldX >= leaderboardButtonX && worldX <= leaderboardButtonX + buttonWidth &&
                    worldY >= leaderboardButtonY && worldY <= leaderboardButtonY + buttonHeight) {
                leaderboardButtonClicked = true;
            }

            // Check if the touch is within the bounds of the exit button
            float exitButtonX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
            float exitButtonY = 200;
            if (worldX >= exitButtonX && worldX <= exitButtonX + buttonWidth &&
                    worldY >= exitButtonY && worldY <= exitButtonY + buttonHeight) {
                exitButtonClicked = true;
            }

            return true;
        }
    }
}
