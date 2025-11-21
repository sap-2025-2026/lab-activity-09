package ttt_game_system.end_to_end_tests.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.*;
import io.vertx.core.json.JsonObject;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;

/**
 * 
 * Game Service System - A user journey from registration to join 
 * 
 */
public class StepsDefinition {

	private final static String API_GATEWAY_URI = "http://localhost:8080";	
	
	
	static final String API_VERSION = "v1";
	static final String ACCOUNTS_RESOURCE_PATH = "/api/" + API_VERSION + "/accounts";
	// static final String ACCOUNT_RESOURCE_PATH = "/api/" + API_VERSION + "/accounts/:accountId";
	// static final String CHECK_PWD_RESOURCE_PATH = "/api/" + API_VERSION + "/accounts/:accountId/check-pwd";
	
	/* for lobby */
	static final String LOGIN_RESOURCE_PATH = 			"/api/" + API_VERSION + "/lobby/login";
	static final String USER_SESSIONS_RESOURCE_PATH = 	"/api/" + API_VERSION + "/lobby/user-sessions";
	// static final String CREATE_GAME_RESOURCE_PATH = 	"/api/" + API_VERSION + "/lobby/user-sessions/:sessionId/create-game";
	// static final String JOIN_GAME_RESOURCE_PATH = 		"/api/" + API_VERSION + "/lobby/user-sessions/:sessionId/join-game";

	/* for game */
	static final String GAMES_RESOURCE_PATH = "/api/" + API_VERSION + "/games";
	static final String GAME_RESOURCE_PATH =  GAMES_RESOURCE_PATH +   "/:gameId";
	static final String PLAYER_MOVE_RESOURCE_PATH = GAME_RESOURCE_PATH + "/:playerSessionId/move";
	static final String WS_EVENT_CHANNEL_PATH = "/api/" + API_VERSION + "/events";

	private HttpResponse<String> response;
	private String userId;
	private String userSessionId;
	private String playerSessionId;
	
	public StepsDefinition(){
	}
		
    /* Scenario: A full journey from registration to join */
    
	@Before
	public void doSomethingBefore() {
		// run command: "docker compose up --detach"
		/*
		try {
			var DOCKER_PATH = "/usr/local/bin/docker";
			Process process = Runtime.getRuntime().exec(DOCKER_PATH + " compose up --detach");	
			int exitCode = process.waitFor();
			assertEquals(0, exitCode);
			
			// give time docker compose up do start containers 
			Thread.sleep(10000);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("docker compose up failed.");
		}*/
	}
	

    @After
    public void doSomethingAfter(Scenario scenario){
		// run command: "docker compose up --detach"
		/*
    	try {	
			var DOCKER_PATH = "/usr/local/bin/docker";
			Process process = Runtime.getRuntime().exec(DOCKER_PATH + " compose down");	
			int exitCode = process.waitFor();
			assertEquals(0, exitCode);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("docker compose down failed.");
		}*/
    }
    
    
    
    @Given("The TTT game service system is running")
    public void game_service_system_running() {
    	// [TBI] make a call to the API Gateway health check 
    }

    @Given("The TTT game service system has no users with username {string}")
    public void no_existing_user(String userName) {
    	// [TBI] check for existing userName
    }

    @Given("The TTT game service system has no games called {string}")
    public void no_existing_game(String gameId) {
    	// [TBI] check for existing gameId
    }

    
    @When("I register with a unique username {string} and a valid password {string}")
    public void I_register(String userName, String passwd) {
		try {
			JsonObject body = new JsonObject();
	        body.put("userName", userName);
	        body.put("password", passwd);	        
			response = doPost(API_GATEWAY_URI + ACCOUNTS_RESOURCE_PATH, body);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Registration failed.");
		}
    }

    @Then("I should see a confirmation that my account was created")
    public void register_ok() {
    	assertEquals(response.statusCode(), 200);
    }
    
    @When("I login with username {string} and a valid password {string}")
    public void I_login(String userName, String passwd) {
		try {
			JsonObject body = new JsonObject();
	        body.put("userName", userName);
	        body.put("password", passwd);	        
			response = doPost(API_GATEWAY_URI + LOGIN_RESOURCE_PATH, body);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Login failed.");
		}
    }

    
    @Then("I should see a confirmation that I logged in")
    public void login_ok() {
    	assertEquals(response.statusCode(), 200);
    	try {
			var reply = new JsonObject(response.body());
			userSessionId = reply.getString("sessionId");
    	} catch (Exception ex) {
			fail("Login failed - invalid session id");
    	}
    }

    @When("I request to create a game called {string}")
    public void I_create(String gameId) {
		try {
			JsonObject body = new JsonObject();
	        body.put("gameId", gameId);
			response = doPost(API_GATEWAY_URI + USER_SESSIONS_RESOURCE_PATH + "/" + userSessionId + "/create-game", body);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Game creation failed.");
		}
    }

    @Then("I should see a confirmation that the game has been created")
    public void create_ok() {
    	assertEquals(response.statusCode(), 200);
    }

    @When("I request to join the game {string} playing with symbol {string}")
    public void I_join(String gameId, String symbol) {
		try {
			JsonObject body = new JsonObject();
	        body.put("gameId", gameId);
	        body.put("symbol", symbol);
			response = doPost(API_GATEWAY_URI + USER_SESSIONS_RESOURCE_PATH + "/" + userSessionId + "/join-game", body);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Join game failed.");
		}
    }
    
    @Then("I should see a confirmation that I joined the game with player session id equals to {string}")
    public void join_ok(String playerSessionId) {
		assertEquals(response.statusCode(), 200);			
		var reply = new JsonObject(response.body());
		var sid = reply.getString("playerSessionId");
		assertEquals(playerSessionId, sid);			
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
