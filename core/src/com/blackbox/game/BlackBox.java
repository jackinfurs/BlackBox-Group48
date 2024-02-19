package com.blackbox.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BlackBox extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture img;
    private Texture backgroundTexture;
    private Texture playButtonTexture;
    private Texture signInButtonTexture;
    private Texture signUpButtonTexture;
    private Texture leaderboardButtonTexture;
    private Texture exitButtonTexture;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

        // Load menu assets
        backgroundTexture = new Texture("VaporBackground.png");
        playButtonTexture = new Texture("play.png");
        signInButtonTexture = new Texture("sign in.png");
        signUpButtonTexture = new Texture("sign up.png");
        leaderboardButtonTexture = new Texture("Leaderboard.png");
        exitButtonTexture = new Texture("Exit.png");
    }

    @Override
    public void render() {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the main menu
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        float buttonWidth = 200;
        float buttonHeight = 50;
        float buttonX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
        float buttonY = Gdx.graphics.getHeight() / 2 - buttonHeight / 2;

        // Draw buttons
        batch.draw(playButtonTexture, buttonX, buttonY, buttonWidth, buttonHeight);
        batch.draw(signInButtonTexture, buttonX, buttonY - 50, buttonWidth, buttonHeight);
        batch.draw(signUpButtonTexture, buttonX, buttonY - 100, buttonWidth, buttonHeight);
        batch.draw(leaderboardButtonTexture, buttonX, buttonY - 150, buttonWidth, buttonHeight);
        batch.draw(exitButtonTexture, buttonX, buttonY - 200, buttonWidth, buttonHeight);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        backgroundTexture.dispose();
        playButtonTexture.dispose();
        signInButtonTexture.dispose();
        signUpButtonTexture.dispose();
        leaderboardButtonTexture.dispose();
        exitButtonTexture.dispose();
    }
}
