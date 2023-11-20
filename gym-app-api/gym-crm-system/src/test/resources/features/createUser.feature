Feature: Create user
  Scenario: Create user and save to database
    Given I create an user with firstname "Ben" and last name "Netty" and store to database
    When I search for that user by the isActive true
    Then I should find at least one result