/*package com.group48.blackbox;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;


public class TestCases {

    private MainMenuScreen mainMenuScreen;
    private GameScreen gameScreen;

    @Before
    public void setUp() {
        mainMenuScreen = new MainMenuScreen(null);
        BlackBox game = new BlackBox();
        gameScreen = new GameScreen(game);
        gameScreen.show();
    }

    @Test
    public void testMainMenuPlayButtonClicked() {
        assertFalse(mainMenuScreen.playButtonClicked);
        mainMenuScreen.touchDown(400, 310);
        assertTrue(mainMenuScreen.playButtonClicked);
    }

    @Test
    public void testMainMenuTutorialButtonClicked() {
        assertFalse(mainMenuScreen.tutorialButtonClicked);
        mainMenuScreen.touchDown(400, 210);
        assertTrue(mainMenuScreen.tutorialButtonClicked);
    }

    @Test
    public void testMainMenuLeaderboardButtonClicked() {
        assertFalse(mainMenuScreen.leaderboardButtonClicked);
        mainMenuScreen.touchDown(400, 410);
        assertTrue(mainMenuScreen.leaderboardButtonClicked);
    }

    @Test
    public void testMainMenuExitButtonClicked() {
        assertFalse(mainMenuScreen.exitButtonClicked);
        mainMenuScreen.touchDown(400, 110);
        assertTrue(mainMenuScreen.exitButtonClicked);
    }

    //  @Test
    // public void testGameScreenSelectTile() {
    //     assertNull(gameScreen.selectTile(gameScreen.tiledMap, 0, 0));
    //    assertNotNull(gameScreen.selectTile(gameScreen.tiledMap, 2, 2));
    //     assertEquals(Tile.GREEN.getValue(), ((TiledMapTileLayer)gameScreen.tiledMap.getLayers().get("Base"))
    //        .getCell(2, 2).getTile().getId());
    //  }

    ////  @Test
    //   public void testGameScreenDeselectTiles() {
    //       gameScreen.selectTile(gameScreen.tiledMap, 3, 3);
    //       assertNotNull(gameScreen.selectedTile);
    ///       gameScreen.deselectTiles(gameScreen.tiledMap);
    //      assertNull(gameScreen.selectedTile);
    //   }
    @Test
    public void testGetUsername() {
        SignIn.username = "TestUser";
        assertEquals("TestUser", SignIn.getUsername());
    }

}
*/
