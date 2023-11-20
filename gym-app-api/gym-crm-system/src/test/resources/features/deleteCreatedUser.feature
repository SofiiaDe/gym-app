Feature: Delete user
  Scenario: Delete created user saved to database
    Given I have created users which I want to delete from database
    When I delete user from list of created users once it is deleted from DB
    Then I should have 0 users in the list of created users