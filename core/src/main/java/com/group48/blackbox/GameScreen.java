package com.group48.blackbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Objects;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class GameScreen extends SignIn implements Screen {
    final BlackBox game;
    private final Stage stage;
    private OrthographicCamera tiledMapCamera;
    private GameBoard tiledMap;
    private Label text;
    private Label scoreText;
    private TextBox textBox;
    private Score score;

    public GameScreen(BlackBox game)
    {
        this.game = game;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), game.camera));
    }

    @Override
    public void show()
    {
        boolean cheats = false;
        textBox = TextBox.EMPTY;
        System.out.println("\n--- GAME SCREEN ---");
        Gdx.input.setInputProcessor(stage);
        if (Objects.equals(SignIn.getUsername(), "sv_cheats 1")) cheats = true;

        Skin skin = game.assets.get("uiskin.json");

        Texture backgroundTex = game.assets.get("MainMenuScreen/vaporBackground.png");
        Image background = new Image(backgroundTex);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Texture coiTex = game.assets.get("GameScreen/circle.png");
        Image[] circles = new Image[6];

        tiledMap = new GameBoard(game);
        tiledMapCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        tiledMapCamera.position.set(280, 120, 0);
        tiledMapCamera.update();

        tiledMap.getRenderer().setView(tiledMapCamera);
        tiledMap.placeAtoms();

        TextButton endButton = new TextButton("End game", skin);
        endButton.setPosition(stage.getWidth() - 280, stage.getHeight() - 240);
        final boolean[] isClicked = { false };
        endButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if (!isClicked[0]) {
                    // end game
                    if (tiledMap.getAtoms().getGuessAtomsCount() == 6) {
                        textBox = TextBox.END_GAME;
                        tiledMap.setFinished(true);
                        score.gamefinished = true;
                        game.assets.get("Sound/gameEnd.wav", Sound.class).play();
                        int i = 0;
                        for (String s : tiledMap.getAtoms().getAtomCoordinates()) {
                            String[] temp = s.split(",");
                            circles[i] = new Image(coiTex);
                            if (Integer.parseInt(temp[1]) % 2 == 1) {
                                circles[i].setPosition((Integer.parseInt(temp[0]) * 32) + 8, (Integer.parseInt(temp[1]) * 25) - 7);
                            } else {
                                circles[i].setPosition((Integer.parseInt(temp[0]) * 32) - 8, (Integer.parseInt(temp[1]) * 25) - 7);
                            }
                            circles[i].moveBy(120, 179);
                            stage.addActor(circles[i]);
                            i++;
                        }
                        tiledMap.getRenderer().render();
                        isClicked[0] = true;
                    } else textBox = TextBox.GUESS_INCOMPLETE;
                }
            }
        });

        TextButton exitButton = new TextButton("Exit to main menu", skin);
        exitButton.setPosition(stage.getWidth() - 280, stage.getHeight() - 360);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                System.out.println("back to the main menu");
                game.assets.get("Sound/clickBack.wav", Sound.class).play();
                game.setScreen(game.mainMenuScreen);
            }
        });

        if (cheats) {
            tiledMap.getAtoms().revealAtoms();
            textBox = TextBox.CHEATER;
            game.assets.get("Sound/yousuck.wav", Sound.class).play();
        } else game.assets.get("Sound/gameStart.wav", Sound.class).play();

        text = new Label("", skin);
        text.setPosition(50, 70);
        text.setFontScaleX(0.85f);

        scoreText = new Label("Score: 0", skin);
        scoreText.setPosition(50, 30);
        scoreText.setFontScaleX(0.85f);

        stage.addActor(background);
        stage.addActor(endButton);
        stage.addActor(exitButton);
        stage.addActor(text);
        stage.addActor(scoreText);
        background.addAction(alpha(0.5f));
        stage.addAction(sequence(alpha(0f), fadeIn(0.5f)));

        score = new Score(tiledMap.getAtoms());
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        update(delta);

        game.camera.update();

        stage.draw();
        tiledMap.getRenderer().render();

        game.batch.begin();

        Vector3 mousePos;
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            textBox = TextBox.EMPTY;

            mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            tiledMapCamera.unproject(mousePos);

            if (tiledMap.selectTile(mousePos) == -1) {
                game.assets.get("Sound/clickInvalid.wav", Sound.class).play();
                textBox = TextBox.INVALID_TILE;
            } else {
                game.assets.get("Sound/clickConfirm.wav", Sound.class).play();
                textBox = TextBox.SELECT_TILE;
            }
        }
        int currentScore = score.calculateScore();
        scoreText.setText("Score: " + currentScore);

        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            // Check for button presses
            mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            tiledMapCamera.unproject(mousePos);

            tiledMap.addGuessAtom(mousePos);
            if (tiledMap.getAtoms().getGuessAtomsCount() < 6)
                game.assets.get("Sound/clickConfirm.wav", Sound.class).play();
            else
                game.assets.get("Sound/clickInvalid.wav", Sound.class).play();
            textBox = TextBox.ATOM_GUESS;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            System.out.println("back to the main menu");
            game.assets.get("Sound/clickBack.wav", Sound.class).play();
            game.setScreen(game.mainMenuScreen);
        }

        tiledMap.getRenderer().render();
        game.batch.end();

        switch (textBox) {
            case EMPTY -> text.setText("");
            case INVALID_TILE -> text.setText("Invalid tile selection.");
            case END_GAME -> text.setText("Game over.");
            case SELECT_TILE -> text.setText("Tile selected.");
            case RAY_HIT -> text.setText("Ray has hit an Atom.");
            case RAY_REFLECT -> text.setText("Ray has reflected from an Atom.");
            case RAY_DEFLECT -> text.setText("Ray has deflected an Atom.");
            case RAY_MISS -> text.setText("Ray has missed an Atom.");
            case ATOM_GUESS -> text.setText("Guess atom #" + tiledMap.getAtoms().getGuessAtomsCount() + ".");
            case GUESS_INCOMPLETE -> text.setText("You must place six guess atoms to end the game.");
            case CHEATER -> text.setText("Cheats enabled.");
        }
    }

    @Override
    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height, false);
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
        stage.clear();
    }

    @Override
    public void dispose()
    {
        stage.dispose();
        if (!Objects.isNull(tiledMap)) tiledMap.dispose();
    }

    public void update(float delta)
    {
        stage.act(delta);
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

        TextBox(int value)
        {
        }
    }
}
