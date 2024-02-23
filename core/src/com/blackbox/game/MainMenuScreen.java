package com.blackbox.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {
    final BlackBox game;
    OrthographicCamera camera;

    /*
    // Button class for code cleanup; to be implemented as a misc task
    class Button {
        float width = 200f;
        float height = 50f;
        float x = Gdx.graphics.getWidth() / 2 - width / 2;
        float y = 400;
        Texture texture;
        float worldX = Gdx.graphics.getHeight() * Gdx.graphics.getWidth() / (float) Gdx.graphics.getWidth();
        float worldY = (Gdx.graphics.getHeight() - Gdx.graphics.getHeight()) * Gdx.graphics.getHeight() / (float) Gdx.graphics.getHeight();

        public Button(Texture texture) {
            this.texture = texture;
        }

        public void draw(int y) {
            float x = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
            batch.draw(texture, x, y, buttonWidth, buttonHeight);
        }

        public boolean isClicked() {
            return worldX >= x &&
                    worldX <= y + width &&
                    worldY >= y &&
                    worldY <= y + height;
        }
    }
     */

    // init textures
    Texture backgroundTexture;
    Texture playButtonTexture;
    Texture leaderboardButtonTexture;
    Texture exitButtonTexture;
    // TODO might switch below for class methods; to make a button class and have .isClicked(), etc
    //  makes code cleaner when these buttons are objects, shortens code, reduces lines, looks nicer
    //  all following TODOs have to do with buttons
    boolean playButtonClicked;
    boolean exitButtonClicked;
    boolean leaderboardButtonClicked;

    // MainMenuScreen constructor
    public MainMenuScreen(final BlackBox game) {
        this.game = game;

        camera = new OrthographicCamera();
        // TODO please find a way to set the viewportWidth and Height to something more concrete
        //  this is exactly the time i'd like a preprocessor directive
        // TODO also set this to not stretch/borderless, etc
        camera.setToOrtho(false, 800, 600);

        // Load menu assets/pngs
        backgroundTexture = new Texture("MainMenuScreen/vaporBackground.png");
        // Button playButton = new Button(new Texture("MainMenuScreen/play.png"));
        playButtonTexture = new Texture("MainMenuScreen/play.png");
        leaderboardButtonTexture = new Texture("MainMenuScreen/leaderboard.png");
        exitButtonTexture = new Texture("MainMenuScreen/exit.png");

        Gdx.input.setInputProcessor(new InputHandler());
    }

    float buttonWidth = 200;
    float buttonHeight = 50;

    private void drawButton(Texture t, int y) {
        float x = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
        game.batch.draw(t, x, y, buttonWidth, buttonHeight);
    }

    @Override
    public void render(float v) {
        // Clear the screen
        ScreenUtils.clear(0, 0, 0, 1);

        // Render the main menu
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // draw backgroundTexture
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // TODO
        // draw buttons
        drawButton(leaderboardButtonTexture, 400);
        drawButton(playButtonTexture, 300);
        drawButton(exitButtonTexture, 200);

        game.batch.end();

        // Process button clicks
        if (playButtonClicked) {
            System.out.println("Play button clicked!");
            // SignIn.localSignIn();
            playButtonClicked = false; // Reset
            game.setScreen(new GameScreen(game));
            dispose();
        }

        if (leaderboardButtonClicked) {
            System.out.println("Leaderboard button clicked!");
            SignIn.readScores();
            leaderboardButtonClicked = false; // Reset
        }

        if (exitButtonClicked) {
            System.out.println("Exit button clicked!");
            Gdx.app.exit(); // Exit the application
        }
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
            if (worldX >= playButtonX &&
                    worldX <= playButtonX + buttonWidth &&
                    worldY >= playButtonY &&
                    worldY <= playButtonY + buttonHeight) {
                playButtonClicked = true;
            }

            // TODO
            // Check if the touch is within the bounds of the leaderboard button
            float leaderboardButtonX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
            float leaderboardButtonY = 400;
            if (worldX >= leaderboardButtonX &&
                    worldX <= leaderboardButtonX + buttonWidth &&
                    worldY >= leaderboardButtonY &&
                    worldY <= leaderboardButtonY + buttonHeight) {
                leaderboardButtonClicked = true;
            }

            // Check if the touch is within the bounds of the exit button
            float exitButtonX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
            float exitButtonY = 200;
            if (worldX >= exitButtonX &&
                    worldX <= exitButtonX + buttonWidth &&
                    worldY >= exitButtonY &&
                    worldY <= exitButtonY + buttonHeight) {
                exitButtonClicked = true;
            }
            return true;
        }
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        playButtonTexture.dispose();
        leaderboardButtonTexture.dispose();
        exitButtonTexture.dispose();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
