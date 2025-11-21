package ttt_game_service.domain.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.*;

import ttt_game_service.domain.Game;
import ttt_game_service.domain.InvalidJoinException;
import ttt_game_service.domain.TTTSymbol;
import ttt_game_service.domain.UserId;

@DisplayName("Game Logic Test Case")
public class GameTest {

	private Game myGame;
	
	@BeforeEach
	public void setUp() {
		myGame = new Game("game-01");
	}
	
	@Test
	@DisplayName("Game should start after two players join the game")
	public void testJoinWithDifferentSymbols() {
		UserId user1 = new UserId("user-01");
		UserId user2 = new UserId("user-02");		
		assertThrows(InvalidJoinException.class,
	            () -> {
	            	myGame.joinGame(user1, TTTSymbol.X);
	    			myGame.joinGame(user2, TTTSymbol.X);
	            },
	            "Second player joining with the same symbol, should raise an exception");
	}
	
	@Test
	@DisplayName("Game should start after two players join the game")
	public void testStartWhenTwoPlayers() {
		UserId user1 = new UserId("user-01");
		UserId user2 = new UserId("user-02");		
		try {
			myGame.joinGame(user1, TTTSymbol.X);
			myGame.joinGame(user2, TTTSymbol.O);
			assertEquals(myGame.isReadyToStart(), true, "When 2 users joins the game, the game should be ready to start");
			myGame.startGame();
			assertEquals(myGame.isStarted(), true, "The game should start after the explicit request");
		} catch (Exception ex) {
			fail("Wrong invalid join.");
		}
	}
}
