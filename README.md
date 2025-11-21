#### Software Architecture and Platforms - a.y. 2025-2026

## Lab Activity #09 - 20251121  

v1.0.0-20251119

- **Testing microservices** 
  - TTT Game Server case study - focus on `TTT Game Service`
    - **Unit** tests
		- about the domain: `src/test/java/ttt_game_service.domain.tests`
          - `GameTest` test case for the `Game` class (partial)
        - about the application: `src/test/java/ttt_game_service.application.tests`
          - `GameServiceTest` test case for the `GameServiceImpl` class (partial)
		- about the infrastructure: `src/test/java/ttt_game_service.infrastructure.tests`
          - `GameServiceControllerTest` test case for the `GameServiceController` class (partial)
    - **Integration** tests
		- testing consumer proxies and adapters in `src/test/java/ttt_game_service.infrastructure.tests`
          - Testing API Gateway consumer proxy: `APIGatewayProxyTest`
          - Testing Lobby Service consumer proxy: `LobbyServiceProxyTest`
    - **Component** tests
    	- feature description in `src/test/resources/ttt_game_service`
        - test implementation in `src/test/java/ttt_game_service.component_tests`
           - main class: `ttt_game_service.component_tests.RunCucumberTest` 
           - steps defined in `ttt_game_service.component_tests.steps.StepsDefinition`
    - **End-to-End** test 
        - repo: `ttt-game-system-end-to-end`
    	    - user journeys description in `src/test/resources/ttt_game_system`
              - `user-journey-1.feature` is about a single user registrating, logging in, creating a game and joining the game
              - `user-journey-2.feature` is about two users playing
            - steps implementation in `src/test/java`
              - `ttt_game_system.end_to_end_tests.steps.StepsDefinitionForUserJourney1` class
              - `ttt_game_system.end_to_end_tests.steps.StepsDefinitionForUserJourney2` class (only skeleton)
        - To run the test:
          - first spawn the system with `docker compose up` from `lab-activity-09` root directory
          - then run `ttt_game_system.end_to_end_tests.RunCucumberTest`
