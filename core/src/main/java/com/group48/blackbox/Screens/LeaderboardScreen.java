package com.group48.blackbox.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.group48.blackbox.BlackBox;
import com.group48.blackbox.ScoresManager;

import java.util.List;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class LeaderboardScreen extends InputAdapter implements Screen {
    
    final BlackBox game;
    private final Stage stage;
    
    public LeaderboardScreen(final BlackBox game)
    {
        this.game = game;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), game.camera));
    }
    
    @Override
    public void show()
    {
        System.out.println("\n--- LEADERBOARD SCREEN ---\n");
        Gdx.input.setInputProcessor(stage);
        
        Skin skin = game.assets.get("uiskin.json");
        
        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(50).left().padLeft(100);
        
        List<ScoresManager.ScoreEntry> scores = ScoresManager.loadScores();
        
        Label titleLabel = new Label("LEADERBOARD", skin);
        titleLabel.setFontScale(2.0f);
        table.add(titleLabel).padBottom(30).colspan(2).center();
        table.row();
        
        for (int i = 0 ; i < scores.size() ; i++) {
            ScoresManager.ScoreEntry entry = scores.get(i);
            String scoreText = String.format("%2d. %s - %d", i + 1, entry.getUsername(), entry.getScore());
            Label scoreLabel = new Label(scoreText, skin);
            table.add(scoreLabel).pad(8);
            table.row();
        }
        
        Texture backgroundTex = game.assets.get("signinBackground.png");
        Image background = new Image(backgroundTex);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        TextButton exitButton = new TextButton("Exit to main menu", skin);
        exitButton.setPosition(stage.getWidth() - 280, stage.getHeight() - 100);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                game.setScreen(game.mainMenuScreen);
            }
        });
        
        stage.addActor(background);
        stage.addActor(table);
        stage.addActor(exitButton);
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
        game.batch.end();
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.assets.get("Sound/clickBack.wav", Sound.class).play();
            game.setScreen(game.mainMenuScreen);
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
    }
    
    public void update(float delta)
    {
        stage.act(delta);
    }
}
