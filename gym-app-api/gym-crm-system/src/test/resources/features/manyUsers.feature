Feature: Many users

  Background: Clean up

  Scenario: Create and find many users
    Given I create an user with firstname "John" and last name "Dev" and store to database
    Given I create an user with firstname "Kate" and last name "Enjee" and store to database
