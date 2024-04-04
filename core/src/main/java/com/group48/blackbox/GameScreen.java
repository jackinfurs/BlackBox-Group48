package com.group48.blackbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.HashSet;
import java.util.Set;

enum Tile {
    BLACK(1), GREEN(2);
    private final int value;
    
    Tile(int value)
    {
        this.value = value;
    }
    
    public int getValue()
    {
        return value;
    }
}

// TODO get atoms.excludedCoords() in here
class TileCoordinates {
    
    private Set<String> edgeCoords, cornerCoords;
    
    public TileCoordinates()
    {
        this.edgeCoords = getEdgeCoords();
        this.cornerCoords = getCornerCoords();
    }
    
    public Set<String> getEdgeCoords()
    {
        if (edgeCoords == null) {
            edgeCoords = new HashSet<>();
            edgeCoords.add("2,0");
            edgeCoords.add("3,0");
            edgeCoords.add("4,0");
            edgeCoords.add("5,0");
            edgeCoords.add("6,0");
            edgeCoords.add("1,1");
            edgeCoords.add("6,1");
            edgeCoords.add("1,2");
            edgeCoords.add("7,2");
            edgeCoords.add("0,3");
            edgeCoords.add("7,3");
            edgeCoords.add("0,4");
            edgeCoords.add("8,4");
            edgeCoords.add("0,5");
            edgeCoords.add("7,5");
            edgeCoords.add("1,6");
            edgeCoords.add("7,6");
            edgeCoords.add("1,7");
            edgeCoords.add("6,7");
            edgeCoords.add("2,8");
            edgeCoords.add("3,8");
            edgeCoords.add("4,8");
            edgeCoords.add("5,8");
            edgeCoords.add("6,8");
        }
        return edgeCoords;
    }
    
    public boolean isEdge(int x, int y)
    {
        return edgeCoords.contains(x + "," + y);
    }
    
    public Set<String> getCornerCoords()
    {
        if (cornerCoords == null) {
            cornerCoords = new HashSet<>();
            cornerCoords.add("2,0");
            cornerCoords.add("6,0");
            cornerCoords.add("0,4");
            cornerCoords.add("8,4");
            cornerCoords.add("2,8");
            cornerCoords.add("6,8");
        }
        return cornerCoords;
    }
}

class CoordCell {
    private final TiledMapTileLayer.Cell selectedTile;
    private final int x;
    private final int y;
    
    public CoordCell(TiledMapTileLayer.Cell selectedTile, int x, int y)
    {
        this.selectedTile = selectedTile;
        this.x = x;
        this.y = y;
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }
}

public class GameScreen extends SignIn implements Screen {
    final BlackBox game;
    
    private GameBoard tiledMap;
    
    private boolean gameFinished;
    
    public GameScreen(BlackBox game)
    {
        this.game = game;
    }
    
    @Override
    public void show()
    {
        tiledMap = new GameBoard();
        tiledMap.placeAtoms();
    }
    
    // TODO implement direction + proper tile selection
    @Override
    public void render(float delta)
    {
        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);
        game.camera.update();
        tiledMap.renderer.render();
        
        // TODO redo this please
        TextButton endButton = new TextButton("End game", new Skin(Gdx.files.internal("uiskin.json")));
        endButton.setPosition(500, 150);
        Rectangle endButtonBounds = new Rectangle(endButton.getX(), endButton.getY(), endButton.getWidth(), endButton.getHeight());
        TextButton exitButton = new TextButton("Exit to main menu", new Skin(Gdx.files.internal("uiskin.json")));
        exitButton.setPosition(500, 50);
        Rectangle exitButtonBounds = new Rectangle(exitButton.getX(), exitButton.getY(), exitButton.getWidth(), exitButton.getHeight());
        
        game.camera.position.set(360, 110, 0);
        game.camera.update();
        tiledMap.getRenderer().setView(game.camera);
        game.batch.setProjectionMatrix(game.camera.combined);
        
        game.batch.begin();
        endButton.draw(game.batch, 1f);
        exitButton.draw(game.batch, 1f);
        
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            // Check for button presses
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(mousePos);
            
            if (endButtonBounds.contains(mousePos.x, mousePos.y)) {
                gameFinished = true;
            }
            if (exitButtonBounds.contains(mousePos.x, mousePos.y)) game.setScreen(new MainMenuScreen(game));
            
            tiledMap.selectTile(mousePos);
        }
        // if selected tile is invalid, deselect all tiles
        
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            // Check for button presses
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(mousePos);
            
            tiledMap.addGuessAtom(mousePos);
        }
        
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            System.out.println("ESC button clicked!");
            game.setScreen(new MainMenuScreen(game));
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            System.out.println("SPACEBAR clicked!");
            gameFinished = true;
        }
        
        // not my proudest work.
        if (gameFinished) {
            tiledMap.getAtoms().setGameFinished();
            for (String s : tiledMap.getAtoms().getAtomCoordinates()) {
                String[] temp = s.split(",");
                if (Integer.parseInt(temp[1]) % 2 == 1)
                    game.batch.draw(tiledMap.getCoiTexture(), (Integer.parseInt(temp[0]) * 32) + 8, (Integer.parseInt(temp[1]) * 25) - 7);
                else
                    game.batch.draw(tiledMap.getCoiTexture(), (Integer.parseInt(temp[0]) * 32) - 8, (Integer.parseInt(temp[1]) * 25) - 7);
                tiledMap.getRenderer().render();
            }
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
}
