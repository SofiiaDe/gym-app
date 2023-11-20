Feature: Is username generated?
  User wants to have correct message generated at runtime

  Scenario Outline: Generate username
    When I provide required values at runtime by map
      | firstName | lastName |
      | Adam      | Smith    |
      | Ron       | Berry    |
      | Tom       | JavaDev  |
      | Anna      | Fent     |
    Then I should receive correctly generated username "<username>"
    Examples:
      | username    |
      | Adam.Smith  |
      | Ron.Berry   |
      | Tom.JavaDev |
      | Anna.Fent   |
