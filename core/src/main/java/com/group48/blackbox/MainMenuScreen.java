package com.group48.blackbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

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

public class MainMenuScreen implements Screen {
    // TODO might switch below for class methods; to make a button class and have .isClicked(), etc
    //  makes code cleaner when these buttons are objects, shortens code, reduces lines, looks nicer
    final BlackBox game;
    InputHandler inputHandler;
    
    Texture backgroundTexture;
    Texture tutorialButtonTexture;
    Texture playButtonTexture;
    Texture leaderboardButtonTexture;
    Texture exitButtonTexture;

    // Rectangles to store button bounds
    Rectangle playButtonBounds;
    Rectangle tutorialButtonBounds;
    Rectangle leaderboardButtonBounds;
    Rectangle exitButtonBounds;

    // Textures for hovered buttons
    Texture playButtonHoverTexture;
    Texture tutorialButtonHoverTexture;
    Texture leaderboardButtonHoverTexture;
    Texture exitButtonHoverTexture;

    public MainMenuScreen(final BlackBox game) {
        this.game = game;
        // Initialize textures
        backgroundTexture = new Texture("MainMenuScreen/vaporBackground.png");
        tutorialButtonTexture = new Texture("MainMenuScreen/tutorial.png");
        playButtonTexture = new Texture("MainMenuScreen/play.png");
        leaderboardButtonTexture = new Texture("MainMenuScreen/leaderboard.png");
        exitButtonTexture = new Texture("MainMenuScreen/exit.png");

        // Initialize hover textures
        tutorialButtonHoverTexture = new Texture("MainMenuScreen/tutorial-red.png");
        playButtonHoverTexture = new Texture("MainMenuScreen/play-red.png");
        leaderboardButtonHoverTexture = new Texture("MainMenuScreen/leaderboard-red.png");
        exitButtonHoverTexture = new Texture("MainMenuScreen/exit-red.png");

        // Set up button bounds
        float buttonWidth = 200;
        float buttonHeight = 50;
        float buttonX = Gdx.graphics.getWidth() / 2f - buttonWidth / 2;

        playButtonBounds = new Rectangle(buttonX, 300, buttonWidth, buttonHeight);
        tutorialButtonBounds = new Rectangle(buttonX, 200, buttonWidth, buttonHeight);
        leaderboardButtonBounds = new Rectangle(buttonX, 400, buttonWidth, buttonHeight);
        exitButtonBounds = new Rectangle(buttonX, 100, buttonWidth, buttonHeight);
        
        inputHandler = new InputHandler();
        Gdx.input.setInputProcessor(inputHandler);
    }

    private void drawButton(Rectangle bounds, Texture texture, Texture hoverTexture) {
        // Base resolution
        final float baseWidth = 800;
        final float baseHeight = 600;

        // Current screen size
        float currentWidth = Gdx.graphics.getWidth();
        float currentHeight = Gdx.graphics.getHeight();

        // Scale factors
        float scaleX = currentWidth / baseWidth;
        float scaleY = currentHeight / baseHeight;

        // Adjusted mouse position
        float mouseX = Gdx.input.getX() / scaleX;
        float mouseY = (Gdx.graphics.getHeight() - Gdx.input.getY()) / scaleY;

        if (bounds.contains(mouseX, mouseY)) {
            game.batch.draw(hoverTexture, bounds.x, bounds.y, bounds.width, bounds.height);
        } else {
            game.batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta)
    {
        
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        // Render the main menu
        game.camera.setToOrtho(false, 800, 600);
        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw buttons based on hover state
        drawButton(playButtonBounds, playButtonTexture, playButtonHoverTexture);
        drawButton(tutorialButtonBounds, tutorialButtonTexture, tutorialButtonHoverTexture);
        drawButton(leaderboardButtonBounds, leaderboardButtonTexture, leaderboardButtonHoverTexture);
        drawButton(exitButtonBounds, exitButtonTexture, exitButtonHoverTexture);

        game.batch.end();
    }
    
    @Override
    public void dispose()
    {
        inputHandler = null;
        backgroundTexture.dispose();

        exitButtonTexture.dispose();
        exitButtonHoverTexture.dispose();
        tutorialButtonTexture.dispose();
        tutorialButtonHoverTexture.dispose();
        playButtonTexture.dispose();
        playButtonHoverTexture.dispose();
        leaderboardButtonTexture.dispose();
        leaderboardButtonHoverTexture.dispose();
    }
    
    private void playButtonClicked()
    {
        System.out.println("Play button clicked!");
        dispose();
        if (SignIn.getUsername() == null) {
            game.setScreen(new SignInScreen(game));
        } else {
            game.setScreen(new GameScreen(game));
        }
        dispose();
    }

    private void tutorialButtonClicked() {
        System.out.println("Tutorial button clicked!");
        game.setScreen(new TutorialScreen(game));
        dispose();
        game.setScreen(new TutorialScreen(game));
    }

    private void leaderboardButtonClicked() {
        System.out.println("Leaderboard button clicked!");
        SignIn.readScores();
    }

    private void exitButtonClicked() {
        System.out.println("Exit button clicked!");
        dispose();
        Gdx.app.exit(); // Exit the application
    }

    private class InputHandler extends InputAdapter {
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            Vector3 touchPoint = new Vector3(screenX, screenY, 0);
            game.camera.unproject(touchPoint);

            if (playButtonBounds.contains(touchPoint.x, touchPoint.y)) {
                playButtonClicked();
            } else if (tutorialButtonBounds.contains(touchPoint.x, touchPoint.y)) {
                tutorialButtonClicked();
            } else if (leaderboardButtonBounds.contains(touchPoint.x, touchPoint.y)) {
                leaderboardButtonClicked();
            } else if (exitButtonBounds.contains(touchPoint.x, touchPoint.y)) {
                exitButtonClicked();
            }
            return true;
        }
    }

    @Override
    public void resize(int width, int height) {
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
