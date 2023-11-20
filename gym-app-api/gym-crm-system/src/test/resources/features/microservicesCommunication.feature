Feature: Microservices communication feature for gym-app

  Background:
    Given Setup Rest Assured for gym-crm-system
    And I have authenticated as username "Henry.Smith" with password "hbE3nQjCGP"

  Scenario: Trainer adds a new training with valid credentials
    When Trainer adds a new training
    Then Status code should be 201
    And Success message "Training added successfully" is returned