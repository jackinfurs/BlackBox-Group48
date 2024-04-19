package com.group48.blackbox.Test;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;

public class GameScreenTest {

    @Mock
    private GameScreen gameScreen;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        gameScreen = new GameScreen(gameBoard);
    }

    @Test
    public void testGameStart() {
        gameScreen.start();
        verify(gameBoard, times(1)).reset();
    }
}
