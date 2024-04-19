package com.group48.blackbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Objects;

// TODO get atoms.excludedCoords() in here

public class GameScreen extends SignIn implements Screen {
    final BlackBox game;
    private final int CAMERAOFFSET_X = 360, CAMERAOFFSET_Y = 110;
    private final int FONT_X = 20, FONT_Y = -80;
    private GameBoard tiledMap;
    private boolean gameFinished, cheats;
    private TextBox textBox = TextBox.EMPTY;
    
    public GameScreen(BlackBox game)
    {
        this.game = game;
    }
    
    @Override
    public void show()
    {
        if (Objects.equals(SignIn.getUsername(), "sv_cheats 1")) this.cheats = true;
        
        tiledMap = new GameBoard(game);
        tiledMap.placeAtoms();
        if (cheats) {
            tiledMap.getAtoms().revealAtoms();
            textBox = TextBox.CHEATER;
            game.assets.get("Sound/yousuck.wav", Sound.class).play();
        } else game.assets.get("Sound/gameStart.wav", Sound.class).play();
    }
    
    // TODO implement direction + proper tile selection
    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);
        game.camera.update();
        tiledMap.getRenderer().render();
        
        // TODO redo this please
        TextButton endButton = new TextButton("End game", new Skin(Gdx.files.internal("uiskin.json")));
        endButton.setPosition(500, 150);
        Rectangle endButtonBounds = new Rectangle(endButton.getX(), endButton.getY(), endButton.getWidth(), endButton.getHeight());
        TextButton exitButton = new TextButton("Exit to main menu", new Skin(Gdx.files.internal("uiskin.json")));
        exitButton.setPosition(500, 50);
        Rectangle exitButtonBounds = new Rectangle(exitButton.getX(), exitButton.getY(), exitButton.getWidth(), exitButton.getHeight());
        
        game.camera.position.set(CAMERAOFFSET_X, CAMERAOFFSET_Y, 0);
        game.camera.update();
        tiledMap.getRenderer().setView(game.camera);
        game.batch.setProjectionMatrix(game.camera.combined);
        
        game.batch.begin();
        endButton.draw(game.batch, 1f);
        exitButton.draw(game.batch, 1f);
        game.font.getData().setScale(1.2f, 1.2f);
        
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            textBox = TextBox.EMPTY;
            // Check for button presses
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(mousePos);
            
            if (exitButtonBounds.contains(mousePos.x, mousePos.y)) {
                dispose();
                game.setScreen(new MainMenuScreen(game));
            }
            
            if (tiledMap.selectTile(mousePos) == -1) textBox = TextBox.INVALID_TILE;
            else {textBox = TextBox.SELECT_TILE;}
            
            if (endButtonBounds.contains(mousePos.x, mousePos.y)) {
                if (tiledMap.getAtoms().getGuessAtomsCount() == 6) gameFinished = true;
                else textBox = TextBox.GUESS_INCOMPLETE;
            }
        }
        
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            // Check for button presses
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(mousePos);
            
            tiledMap.addGuessAtom(mousePos);
            textBox = TextBox.ATOM_GUESS;
        }
        
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            System.out.println("ESC button clicked!");
            dispose();
            game.setScreen(new MainMenuScreen(game));
        }
        
        // not my proudest work.
        if (gameFinished) {
            tiledMap.getAtoms().setGameFinished();
            textBox = TextBox.END_GAME;
            for (String s : tiledMap.getAtoms().getAtomCoordinates()) {
                String[] temp = s.split(",");
                if (Integer.parseInt(temp[1]) % 2 == 1)
                    game.batch.draw(tiledMap.getCoiTexture(), (Integer.parseInt(temp[0]) * 32) + 8, (Integer.parseInt(temp[1]) * 25) - 7);
                else
                    game.batch.draw(tiledMap.getCoiTexture(), (Integer.parseInt(temp[0]) * 32) - 8, (Integer.parseInt(temp[1]) * 25) - 7);
                tiledMap.getRenderer().render();
            }
        }
        
        switch (textBox) {
            case EMPTY -> game.font.draw(game.batch, "", FONT_X, FONT_Y);
            case INVALID_TILE -> game.font.draw(game.batch, "Invalid tile selection.", FONT_X, FONT_Y);
            case END_GAME -> game.font.draw(game.batch, "Game over.", FONT_X, FONT_Y);
            case SELECT_TILE -> game.font.draw(game.batch, "Tile selected.", FONT_X, FONT_Y);
            case RAY_HIT -> game.font.draw(game.batch, "Ray has hit an Atom.", FONT_X, FONT_Y);
            case RAY_REFLECT -> game.font.draw(game.batch, "Ray has reflected from an Atom.", FONT_X, FONT_Y);
            case RAY_DEFLECT -> game.font.draw(game.batch, "Ray has deflected an Atom.", FONT_X, FONT_Y);
            case RAY_MISS -> game.font.draw(game.batch, "Ray has missed an Atom.", FONT_X, FONT_Y);
            case ATOM_GUESS ->
                    game.font.draw(game.batch, "Guess atom #" + tiledMap.getAtoms().getGuessAtomsCount() + ".", FONT_X, FONT_Y);
            case GUESS_INCOMPLETE ->
                    game.font.draw(game.batch, "You must place six guess atoms to end the game.", FONT_X, FONT_Y);
            case CHEATER -> game.font.draw(game.batch, "Cheats enabled.", FONT_X, FONT_Y);
        }
        
        tiledMap.getRenderer().render();
        game.batch.end();
    }
    
    @Override
    public void resize(int width, int height)
    {
    
    }
    
    @Override
    public void pause()
    {
    
    }
    
    @Override
    public void resume()
    {
    
    }
    
    @Override
    public void hide()
    {
    
    }
    
    @Override
    public void dispose()
    {
        tiledMap.dispose();
    }
    
    enum TextBox {
        EMPTY(0),
        INVALID_TILE(1),
        END_GAME(2),
        SELECT_TILE(3),
        RAY_HIT(4),
        RAY_REFLECT(5),
        RAY_DEFLECT(6),
        RAY_MISS(7),
        ATOM_GUESS(8),
        GUESS_INCOMPLETE(9),
        CHEATER(10);
        
        private final int value;
        
        TextBox(int value)
        {
            this.value = value;
        }
    }
}
