Feature: Joining a game 
  As a logged user
  I want to join a game 
  So that I can play the game

  Scenario: Successful game join
    Given I logged in
    And There exists a game called "super-game", waiting for players
    When I request to join the new game 
    Then I should see a confirmation that I joined the game

 