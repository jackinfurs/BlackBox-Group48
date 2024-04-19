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

public class TutorialScreen implements Screen {
    
    final BlackBox game;
    private final Stage stage;
    private String[] Dialogue;
    private GameBoard tiledMap;
    private OrthographicCamera fake;
    private Skin skin;
    private Label text;
    private int dialogueN;
    private Vector3 mousePos;
    
    public TutorialScreen(final BlackBox game)
    {
        this.game = game;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), game.camera));
    }
    
    @Override
    public void show()
    {
        System.out.println("\n--- TUTORIAL SCREEN ---");
        Gdx.input.setInputProcessor(stage);
        
        Dialogue = initDialogue();
        skin = game.assets.get("uiskin.json");
        
        Texture backgroundTex = game.assets.get("signinBackground.png");
        Image background = new Image(backgroundTex);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        tiledMap = new GameBoard(game);
        fake = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        fake.position.set(120, 100, 0);
        fake.update();
        
        tiledMap.getRenderer().setView(fake);
        dialogueN = 0;
        
        TextButton exitButton = new TextButton("Exit to main menu", skin);
        exitButton.setPosition(stage.getWidth() - 280, stage.getHeight() - 100);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                System.out.println("back to the main menu");
                game.assets.get("Sound/clickBack.wav", Sound.class).play();
                game.setScreen(game.mainMenuScreen);
            }
        });
        
        text = new Label(Dialogue[dialogueN], skin);
        text.setPosition(35, 70);
        text.setFontScaleX(0.75f);
        
        stage.addActor(background);
        stage.addActor(exitButton);
        stage.addActor(text);
        background.addAction(alpha(0.3f));
        stage.addAction(sequence(alpha(0f), fadeIn(0.5f)));
    }
    
    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        update(delta);
        
        game.batch.begin();
        
        stage.draw();
        
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            switch (dialogueN) {
                case 3: // wait for right click on center of board (disable left click)
                    game.assets.get("Sound/clickInvalid.wav", Sound.class).play();
                    break;
                case 8: // user becomes experimenter; remove guess atom and hide new atom
                    game.assets.get("Sound/clickConfirm.wav", Sound.class).play();
                    tiledMap.removeTutorialAtom();
                    tiledMap.getAtoms().placeRandomAtom();
                    text.setText(Dialogue[++dialogueN]);
                    break;
                case 10: // wait for ray to be sent in
                    mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                    fake.unproject(mousePos);
                    if (tiledMap.selectTile(mousePos) == -1)
                        game.assets.get("Sound/clickInvalid.wav", Sound.class).play();
                    else game.assets.get("Sound/clickConfirm.wav", Sound.class).play();
                    break;
                default:
                    game.assets.get("Sound/clickConfirm.wav", Sound.class).play();
                    text.setText(Dialogue[++dialogueN]);
            }
            
            if (dialogueN >= 12) {
                game.setScreen(game.mainMenuScreen);
            }
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            switch (dialogueN) {
                case 3: // wait for right click on centre of atom
                    mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                    fake.unproject(mousePos);
                    if (tiledMap.getTileXCoord(mousePos) == 4 && tiledMap.getTileYCoord(mousePos) == 4) {
                        tiledMap.getAtoms().addAtom(4, 4);
                        game.assets.get("Sound/clickConfirm.wav", Sound.class).play();
                        text.setText(Dialogue[++dialogueN]);
                    } else game.assets.get("Sound/clickInvalid.wav", Sound.class).play();
                    break;
                case 10: // wait for ray to be sent in
                    mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                    fake.unproject(mousePos);
                    if (tiledMap.addGuessAtom(mousePos) == -1) {
                        game.assets.get("Sound/clickInvalid.wav", Sound.class).play();
                    } else {
                        game.assets.get("Sound/clickConfirm.wav", Sound.class).play();
                        tiledMap.getAtoms().setGameFinished();
                        text.setText(Dialogue[++dialogueN]);
                    }
                    // if correct guess, dialogueN = 11
                    
                    // if incorrect, dialogueN = 12
                    break;
                default:
                    game.assets.get("Sound/clickInvalid.wav", Sound.class).play();
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            System.out.println("back to the main menu");
            game.assets.get("Sound/clickBack.wav", Sound.class).play();
            game.setScreen(game.mainMenuScreen);
        }
        
        tiledMap.getRenderer().render();
        game.batch.end();
    }
    
    public void update(float delta)
    {
        stage.act(delta);
    }
    
    @Override
    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height, false);
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
    
    private String[] initDialogue()
    {
        String[] dialogue = new String[13];
        dialogue[0] = """
                Welcome to BlackBox+, a match between the Setter and the Experimenter.
                
                Click to continue.""";
        dialogue[1] = """
                The goal of the Experimenter is to deduce the locations of six hidden, randomly
                placed Atoms by sending Rays into the Black Box and observing how they are affected.
                Click to continue.""";
        dialogue[2] = """
                In BlackBox+, you usually play the role of the Experimenter.\s
                However, for now, you will play as the Setter.
                Click to continue.""";
        dialogue[3] = "Right click on the center of the board to place an Atom.";
        dialogue[4] = """
                You have placed an Atom that Rays can react with.
                Upon direct hit, the Ray is destroyed.
                Click to continue.""";
        dialogue[5] = """
                When approached indirectly, the Ray is deflected\s
                at an angle relative to the position of the Atom.
                Click to continue.""";
        dialogue[6] = """
                If the Atom is placed at the edge of the Black Box,
                the Ray is reflected, and never enters the Black Box.
                Click to continue.""";
        dialogue[7] = """
                If the Ray exits the board, the exit point is marked.
                
                Click to continue.""";
        dialogue[8] = """
                Taking a Ray's travel into account, a Guess Atom can be placed.
                
                Click to continue.""";
        dialogue[9] = """
                You will now play the role of the Experimenter.
                One Atom has been hidden in the Black Box.
                Click to continue.""";
        dialogue[10] = "Click on an edge to start sending in Rays.\n" +
                "Once you have deduced the location, Right Click to place a Guess Atom.";
        dialogue[11] = "Well done! You are ready to play Black Box+.\n\n" + // correct guess
                "Click to exit to the Main Menu.";
        dialogue[12] = "Not quite! You'll get it with enough practice.\n\n" + // incorrect guess
                "Click to exit to the Main Menu.";
        return dialogue;
    }
    
    @Override
    public void pause()
    {
    
    }
    
    @Override
    public void resume()
    {
    
    }
}
