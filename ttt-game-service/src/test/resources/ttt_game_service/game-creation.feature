Feature: Creating a game 
  As a logged user
  I want to create a new game 
  So that I can join the game and play with other users

  Scenario: Successful game creation
    Given I logged in
    When I request to create a new game called "super-game"
    Then I should see a confirmation that the game was created


 