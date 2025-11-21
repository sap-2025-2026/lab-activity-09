package ttt_game_service.infrastructure.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.logging.Logger;

import org.junit.jupiter.api.*;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import ttt_game_service.application.GameService;
import ttt_game_service.domain.Game;
import ttt_game_service.domain.InvalidJoinException;
import ttt_game_service.domain.TTTSymbol;
import ttt_game_service.domain.UserId;
import ttt_game_service.infrastructure.GameServiceController;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Game Service Controller Test case")
public class GameServiceControllerTest {

	static Logger logger = Logger.getLogger("[GameServiceControllerTest]");
	private GameService gameService;
	private GameServiceController gameServiceController;
	private Vertx vertx;
	
	private final static String GAME_SERVICE_ADDRESS = "http://localhost";
	private final static int GAME_SERVICE_PORT = 9002;
	
	private final static String GAME_CREATION_ENDPOINT = GAME_SERVICE_ADDRESS + ":" + GAME_SERVICE_PORT + "/api/v1/games";
	private final static String GAME_JOIN_ENDPOINT = GAME_SERVICE_ADDRESS + ":" + GAME_SERVICE_PORT + "/api/v1/games/super-game/join";
		
	@BeforeEach
	public void setUp() {
		var sync = new Synchroniser();
		
		vertx = Vertx.vertx();
		
		/* in integration test, the service is a mock */
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
	}
	
	@Test
	@DisplayName("Game creation")
	public void testGameCreation() {		
		try {
			JsonObject body = new JsonObject();
            body.put("gameId", "super-game");
			var res = doPost(GAME_CREATION_ENDPOINT, body);
			assertEquals(res.statusCode(), 200, "Status code should be 200");			
		} catch (Exception ex) {
			fail("Game creation failed.");
		}	
	}

	@Test
	@DisplayName("Joining a game")
	public void testJoiningGame() {		
		try {
			JsonObject body = new JsonObject();
	        body.put("userId", "user-01");
	        body.put("symbol", "X");
			var res = doPost(GAME_JOIN_ENDPOINT, body);
			assertEquals(res.statusCode(), 200, "Status code should be 200");			

			var reply = new JsonObject(res.body());
			var playerSessionId = reply.getString("playerSessionId");
			assertEquals(playerSessionId, "player-session-01", "Player session id should be correct");	
		} catch (Exception ex) {
			fail("Game join failed.");
		}	
	}
	
	
    @AfterEach
	public void tearDown() {
    	vertx.undeploy(gameServiceController.deploymentID());		
	}

    /* aux */

	private HttpResponse<String> doPost(String uri, JsonObject body) throws Exception {
	    HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Accept", "application/json")
                .POST(BodyPublishers.ofString(body.toString()))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());		
	}
	
	

}
