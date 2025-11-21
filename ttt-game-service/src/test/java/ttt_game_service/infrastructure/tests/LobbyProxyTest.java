package ttt_game_service.infrastructure.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.logging.Logger;
import org.junit.jupiter.api.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import ttt_game_service.application.GameService;
import ttt_game_service.infrastructure.GameServiceController;
import ttt_lobby_service.domain.TTTSymbol;
import ttt_lobby_service.domain.UserId;
import ttt_lobby_service.infrastructure.GameServiceProxy;

import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Game Service Proxy for Lobby Service Test case")
public class LobbyProxyTest {

	static Logger logger = Logger.getLogger("[LobbyProxyTest]");
	private GameService gameService;
	private GameServiceController gameServiceController;
	private Vertx vertx;
	
	private final static String GAME_SERVICE_ADDRESS = "http://localhost";
	private final static int GAME_SERVICE_PORT = 9001;

	private GameServiceProxy proxyForLobby;
	
	@BeforeEach
	public void setUp() {
		var sync = new Synchroniser();

		vertx = Vertx.vertx();
		gameService = new GameServiceMock();
		gameServiceController = new GameServiceController(gameService, GAME_SERVICE_PORT);
		vertx
		.deployVerticle(gameServiceController)
		.onSuccess((res) -> {
			sync.notifySync();
		});		
		
		try {
			sync.awaitSync();
			logger.info("setup completed.");
		} catch (Exception ex) {
			logger.info("sync failed.");
			ex.printStackTrace();
		}
		
		/* creating the proxy */
		
		proxyForLobby = new GameServiceProxy(GAME_SERVICE_ADDRESS + ":" + GAME_SERVICE_PORT);
		
		logger.info("setup completed.");
	}
	
	@Test
	@DisplayName("Creating a game")
	public void testGameCreation() {		
		try {
			proxyForLobby.createNewGame("super-game");
		} catch (Exception ex) {
			fail("Game creation failed.");
		}	
	}

	@Test
	@DisplayName("Joining a game")
	public void testJoiningGame() {		
		try {
			var sessionId = proxyForLobby.joinGame(new UserId("user-01"), "super-game", TTTSymbol.X);
			assertEquals(sessionId, "player-session-01");
		} catch (Exception ex) {
			fail("Game join failed.");
		}	
	}
    @AfterEach
	public void tearDown() {
    	vertx.undeploy(gameServiceController.deploymentID());		
	}
	
}
