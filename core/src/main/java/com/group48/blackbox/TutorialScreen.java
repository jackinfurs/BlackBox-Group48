package com.group48.blackbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;

public class TutorialScreen implements Screen {
    
    final BlackBox game;
    private GameBoard tiledMap;
    private final String[] Dialogue = initDialogue();
    private int dialogueN = 0;
    
    private final int CAMERAOFFSET_X = 150, CAMERAOFFSET_Y = 60;
    
    public TutorialScreen(final BlackBox game)
    {
        this.game = game;
        //        Gdx.input.setInputProcessor(new InputHandler());
    }
    
    @Override
    public void show()
    {
        tiledMap = new GameBoard();
    }
    
    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);
        
        // draw tutorial title at 17,69
        
        // draw textbox at 11,582
        
        game.camera.position.set(CAMERAOFFSET_X, CAMERAOFFSET_Y, 0);
        game.camera.update();
        tiledMap.getRenderer().setView(game.camera);
        game.batch.setProjectionMatrix(game.camera.combined);
        
        // draw exit to main menu button
        TextButton exitButton = new TextButton("Exit to main menu", new Skin(Gdx.files.internal("uiskin.json")));
        exitButton.setPosition(300, 280);
        Rectangle exitButtonBounds = new Rectangle(exitButton.getX(), exitButton.getY(), exitButton.getWidth(), exitButton.getHeight());
        
        game.batch.begin();
        exitButton.draw(game.batch, 1f);
        
        tiledMap.getRenderer().render();
        
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            
            if(dialogueN < 13) System.out.println(Dialogue[dialogueN]);
            else {
                dispose();
                game.setScreen(new MainMenuScreen(this.game));
            }
            dialogueN++;
            
            // Check for button presses
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            System.out.println(mousePos.x + "," + mousePos.y);
            
            if (exitButtonBounds.contains(mousePos.x, mousePos.y)) {
                dispose();
                game.setScreen(new MainMenuScreen(game));
            }
        }
        
        // if ESC pressed, exit to main menu
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            System.out.println("ESC button clicked!");
            dispose();
            game.setScreen(new MainMenuScreen(game));
        }
        
        tiledMap.getRenderer().render();
        game.batch.end();
    }
    
    @Override
    public void dispose()
    {
        tiledMap.dispose();
    }
    
    @Override
    public void resize(int i, int i1)
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
    
    private String[] initDialogue()
    {
        String[] dialogue = new String[13];
        dialogue[0] = "Welcome to BlackBox+, a match between the Setter and the Experimenter.*";
        dialogue[1] = "The goal of the Experimenter is to deduce the locations of six hidden, randomly placed Atoms \n" +
                "by sending Rays into the Black Box and observing how they are affected.*";
        dialogue[2] = "In BlackBox+, you play the role of the Experimenter. \n" +
                "However, for now, you will play as the Setter.*";
        dialogue[3] = "Click on the center of the board to place an Atom.";
        dialogue[4] = "You have placed an Atom that Rays can react with.\n" +
                "Upon direct hit, the Ray is destroyed.*";
        dialogue[5] = "When approached indirectly, the Ray is deflected \n" +
                "at an angle relative to the position of the Atom.*";
        dialogue[6] = "If the Atom is placed at the edge of the Black Box,\n" +
                "the Ray is reflected, and never enters the Black Box.*";
        dialogue[7] = "If the Ray exits the board, the exit point is marked.*";
        dialogue[8] = "Taking a Ray's travel into account, a Guess Atom can be placed.*";
        dialogue[9] = "You will now play the role of the Experimenter.\n" +
                "One Atom has been hidden in the Black Box.*";
        dialogue[10] = "Click on an edge to start sending in Rays.\n" +
                "Once you have deduced the location, Right Click to place a Guess Atom.";
        dialogue[11] = "Well done! You are ready to play Black Box+.* (to exit to main menu)";
        dialogue[12] = "Not quite! You'll get it with enough practice.* (to exit to main menu)";
        return dialogue;
    }
}
