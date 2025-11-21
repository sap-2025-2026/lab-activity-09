package ttt_game_service.application.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.Optional;
import org.junit.jupiter.api.*;
import ttt_game_service.domain.TTTSymbol;
import ttt_game_service.domain.UserId;
import ttt_game_service.application.*;

@DisplayName("Game Service Test Case")
public class GameServiceTest {

	private GameServiceImpl myGameService;
	
	@BeforeEach
	public void setUp() {
		myGameService = new GameServiceImpl();
		myGameService.bindGameRepository(new InMemoryGameRepository());
	}
	
	@Test
	@DisplayName("Game creation")
	public void testGameCreation() {		
		try {
			myGameService.createNewGame("super-game");		
			var g = myGameService.getGameInfo("super-game");
			assertEquals(g.getId(), "super-game");
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("game creation failed");
		}
	}

	@Test
	@DisplayName("Joining game")
	public void testJoinGame() {		
		testGameCreation();
		try { 
			PlayerSessionEventObserver obs = null;
			var ps = myGameService.joinGame(new UserId("user-1"), "super-game", TTTSymbol.X, 
							new PlayerSessionEventObserver() {
								public void enableEventNotification(String playerSessionId) {}
								public void gameStarted(String playerSessionId) {}
								public void newMove(String playerSessionId, String symbol, int x, int y) {}
								public void gameEnded(String playerSessionId, Optional<String> winner) {}
							});		
			assertEquals(ps.getId(), "player-session-1");
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Join game failed");
		}
	}

}
