package com.group48.blackbox.Test;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class BlackBoxTest {

    private BlackBox game;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        game = new BlackBox();
        game.create();
    }

    @Test
    public void testGameInitialization() {
        assertNotNull(game.getScreen());
        assertTrue(game.getScreen() instanceof GameScreen);
    }
}
