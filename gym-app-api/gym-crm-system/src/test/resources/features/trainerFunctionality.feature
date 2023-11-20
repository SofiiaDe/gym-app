Feature: Trainer functionality feature for gym-app

  Background:
    Given Setup Rest Assured for gym-crm-system
    And I have authenticated as username "Henry.Smith" with password "hbE3nQjCGP"

  Scenario: Trainer gets his profile
    When Trainer gets his profile
    Then Status code should be 200

  Scenario: Trainer updates his profile
    When Trainer updates his profile
    Then Status code should be 200

  Scenario: Trainer gets a list of his trainings
    When Trainer gets a list of his trainings
    Then Status code should be 200

  Scenario: Trainer activates his status
    When Trainer activates his status
    Then Status code should be 200
