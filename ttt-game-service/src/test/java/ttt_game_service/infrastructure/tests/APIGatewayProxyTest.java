package ttt_game_service.infrastructure.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.logging.Logger;
import org.junit.jupiter.api.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import ttt_api_gateway.infrastructure.GameServiceProxy;
import ttt_game_service.application.GameService;
import ttt_game_service.infrastructure.GameServiceController;

import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Game Service Proxy for API Gateway Test case")
public class APIGatewayProxyTest {

	static Logger logger = Logger.getLogger("[APIGatewayProxyTest]");
	private GameService gameService;
	private GameServiceController gameServiceController;
	private Vertx vertx;
	
	private final static String GAME_SERVICE_ADDRESS = "http://localhost";
	private final static int GAME_SERVICE_PORT = 9001;

	private GameServiceProxy proxyForAPIGateway;
	
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
		
		proxyForAPIGateway = new GameServiceProxy(GAME_SERVICE_ADDRESS + ":" + GAME_SERVICE_PORT);
		
		logger.info("setup completed.");
	}
	
	@Test
	@DisplayName("Testing retrieval of game info")
	public void testGetGameInfo() {		
		try {
			var game = proxyForAPIGateway.getGameInfo("super-game");
			/* info about a game with no still no players */
			assertEquals(game.gameId(), "super-game");
			assertEquals(game.gameState(), "waiting-for-players");
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Game info retrieval failed.");
		}	
	}

	/* here include also tests for the other proxy methods */
	
    @AfterEach
	public void tearDown() {
    	vertx.undeploy(gameServiceController.deploymentID());		
	}
	
}
