package game.screen;

import java.awt.event.KeyEvent;

import org.junit.Test;
import org.mockito.Mockito;

import game.asciiPanel.AsciiFont;
import game.asciiPanel.AsciiPanel;

public class SnakeGameScreenTest {
    @Test 
    public void testSnakeGameScreen() {
        SnakeGameScreen snakeGameScreen = new SnakeGameScreen();
        snakeGameScreen.registerSnake(1);
        AsciiPanel asciiPanel = new AsciiPanel(40, 40, AsciiFont.gold);
        snakeGameScreen.displayOutput(asciiPanel, 1);
        snakeGameScreen.synchronize();
        
        // simulate snake's move
        snakeGameScreen.respondToUserInput(KeyEvent.VK_LEFT, 1);
        snakeGameScreen.respondToUserInput(KeyEvent.VK_UP, 1);
        snakeGameScreen.respondToUserInput(KeyEvent.VK_RIGHT, 1);
        snakeGameScreen.respondToUserInput(KeyEvent.VK_DOWN, 1);
        snakeGameScreen.respondToUserInput(KeyEvent.VK_A, 1);
        snakeGameScreen.respondToUserInput(KeyEvent.VK_W, 1);
        snakeGameScreen.respondToUserInput(KeyEvent.VK_S, 1);
        snakeGameScreen.respondToUserInput(KeyEvent.VK_D, 1);
        KeyEvent keyEvent = Mockito.mock(KeyEvent.class);
        snakeGameScreen.respondToUserInput(keyEvent, 1);

        snakeGameScreen.removeSnake(1);
    }
}
