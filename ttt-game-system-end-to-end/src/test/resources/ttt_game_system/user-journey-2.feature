Feature: User registration, game creation and join
  As a player of a game
  I want to make moves
  so that I can win

  Scenario: Successful user playing
    Given The TTT game service system is running
    And The TTT game service system has no users with username "eugenio"
    And The TTT game service system has no games called "super-game"
	And a first user registered with username "eugenio" and password "Secret#123"
	And a second user registered with username "maria" and password "Secret#456"
    And the first user logged in
    And the second user logged in
    And the first logged user created a game called "super-game"
    And the first logged user joined the game "super-game" with symbol "X"
    And the second logged user joined the game "super-game" with symbol "Y"
    When the first user requests to make a move in x: "0" y: "0"
    Then the first user should see that the move is accepted

 