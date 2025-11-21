package ttt_game_service.component_tests.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import ttt_game_service.application.GameServiceImpl;
import ttt_game_service.infrastructure.GameServiceController;
import ttt_game_service.infrastructure.InMemoryGameRepository;
import ttt_game_service.infrastructure.tests.Synchroniser;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import java.net.http.HttpRequest.BodyPublishers;

/**
 * 
 * Game Service Component Test
 * 
 */
public class StepsDefinition {

	static final int GAME_SERVICE_PORT = 9002;

	private Vertx vertx;
	private GameServiceController controller;

	private final static String GAME_SERVICE_ADDRESS = "http://localhost";
	
	private final static String GAME_CREATION_ENDPOINT = GAME_SERVICE_ADDRESS + ":" + GAME_SERVICE_PORT + "/api/v1/games";
	private final static String GAME_JOIN_ENDPOINT = GAME_SERVICE_ADDRESS + ":" + GAME_SERVICE_PORT + "/api/v1/games/super-game/join";

	private HttpResponse<String> response;
	private String userId;
	
	public StepsDefinition(){
	}
	
	/* Game creation feature */
	
    /* Scenario: Successful creation */
    
	@Before
	public void doSomethingBefore() {
		var sync = new Synchroniser();
		var service = new GameServiceImpl();		
		service.bindGameRepository(new InMemoryGameRepository());		

		vertx = Vertx.vertx();
		controller = new GameServiceController(service, GAME_SERVICE_PORT);
		
		vertx
		.deployVerticle(controller)
		.onSuccess((res) -> {
			sync.notifySync();
		});		
		
		try {
			sync.awaitSync();
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
	
    @Given("I logged in")
    public void i_logged_in() {
    	userId = "user-1";
    }

    @When("I request to create a new game called {string}")
    public void i_request_to_create_a_new_game_called(String gameId) {
		try {
			JsonObject body = new JsonObject();
            body.put("gameId", gameId);
			response = doPost(GAME_CREATION_ENDPOINT, body);
		} catch (Exception ex) {
			fail("Game creation failed.");
		}	
    }

    @Then("I should see a confirmation that the game was created")
    public void i_should_see_a_confirmation_that_the_game_was_created() {
		assertEquals(response.statusCode(), 200);			
    }
    
    /* Joining game feature */
    
    @Given("There exists a game called {string}, waiting for players")
    public void there_exists_a_game_waiting_for_players(String gameId) {
		try {
			JsonObject body = new JsonObject();
            body.put("gameId", "super-game");
			var res = doPost(GAME_CREATION_ENDPOINT, body);
			assertEquals(res.statusCode(), 200, "Status code should be 200");			
		} catch (Exception ex) {
			fail("Game creation failed.");
		}	

    }
    
    @When("I request to join the new game")
    public void i_request_to_join_the_new_game() {
		try {
			JsonObject body = new JsonObject();
	        body.put("userId", "user-01");
	        body.put("symbol", "X");
			response = doPost(GAME_JOIN_ENDPOINT, body);
		} catch (Exception ex) {
			fail("Game creation failed.");
		}	
    }
    
    @Then("I should see a confirmation that I joined the game")
    public void i_should_see_a_confirmation_that_i_joined_the_game() {
		assertEquals(response.statusCode(), 200);			
		var st = response.body();
		var reply = new JsonObject(response.body());
		var playerSessionId = reply.getString("playerSessionId");
		assertEquals(playerSessionId, "player-session-1");			
    }
    
    
    @After
    public void doSomethingAfter(Scenario scenario){
    	vertx.undeploy(controller.deploymentID());
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
