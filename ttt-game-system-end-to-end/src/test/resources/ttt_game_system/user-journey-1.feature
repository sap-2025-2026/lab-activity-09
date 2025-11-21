Feature: User registration, game creation and join
  As a user
  I want to register and login
  so that I can create a game

  Scenario: Successful user registration and game creation
    Given The TTT game service system is running
    And The TTT game service system has no users with username "eugenio"
    And The TTT game service system has no games called "super-game"
    When I register with a unique username "eugenio" and a valid password "Secret#123"
    Then I should see a confirmation that my account was created
    When I login with username "eugenio" and a valid password "Secret#123"
    Then I should see a confirmation that I logged in
    When I request to create a game called "super-game"
    Then I should see a confirmation that the game has been created
    When I request to join the game "super-game" playing with symbol "X"
    Then I should see a confirmation that I joined the game with player session id equals to "player-session-1" 

 