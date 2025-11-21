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
public class StepsDefinitionForUserJourney2 {

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
	
	public StepsDefinitionForUserJourney2(){
	}
		
	@Given("a first user registered with username {string} and password {string}")
	public void a_first_user_registered_with_username_and_password(String string, String string2) {
	    // Write code here that turns the phrase above into concrete actions
	    // throw new io.cucumber.java.PendingException();
	}
	@Given("a second user registered with username {string} and password {string}")
	public void a_second_user_registered_with_username_and_password(String string, String string2) {
	    // Write code here that turns the phrase above into concrete actions
	    // throw new io.cucumber.java.PendingException();
	}
	@Given("the first user logged in")
	public void the_first_user_logged_in() {
	    // Write code here that turns the phrase above into concrete actions
	    // throw new io.cucumber.java.PendingException();
	}
	@Given("the second user logged in")
	public void the_second_user_logged_in() {
	    // Write code here that turns the phrase above into concrete actions
	    // throw new io.cucumber.java.PendingException();
	}
	@Given("the first logged user created a game called {string}")
	public void the_first_logged_user_created_a_game_called(String string) {
	    // Write code here that turns the phrase above into concrete actions
	    // throw new io.cucumber.java.PendingException();
	}
	@Given("the first logged user joined the game {string} with symbol {string}")
	public void the_first_logged_user_joined_the_game_with_symbol(String string, String string2) {
	    // Write code here that turns the phrase above into concrete actions
	    // throw new io.cucumber.java.PendingException();
	}
	@Given("the second logged user joined the game {string} with symbol {string}")
	public void the_second_logged_user_joined_the_game_with_symbol(String string, String string2) {
	    // Write code here that turns the phrase above into concrete actions
	    // throw new io.cucumber.java.PendingException();
	}
	@When("the first user requests to make a move in x: {string} y: {string}")
	public void the_first_user_requests_to_make_a_move_in_x_y(String string, String string2) {
	    // Write code here that turns the phrase above into concrete actions
	    // throw new io.cucumber.java.PendingException();
	}
	@Then("the first user should see that the move is accepted")
	public void the_first_user_should_see_that_the_move_is_accepted() {
	    // Write code here that turns the phrase above into concrete actions
	    // throw new io.cucumber.java.PendingException();
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
