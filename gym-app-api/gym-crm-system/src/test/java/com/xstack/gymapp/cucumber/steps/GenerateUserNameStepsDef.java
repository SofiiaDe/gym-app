package com.xstack.gymapp.cucumber.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.xstack.gymapp.service.UserService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

public class GenerateUserNameStepsDef {

  @Autowired
  private UserService userService;

  private Map<String, String> valueTagMap;

  @Before
  public void setUp() {
    valueTagMap = new HashMap<>();
  }

  @When("I provide required values at runtime by map")
  public void i_provide_required_values_at_runtime(DataTable dataTable) {

    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    for (Map<String, String> columns : rows) {
      String firstName = columns.get("firstName");
      String lastName = columns.get("lastName");
      valueTagMap.put(firstName, lastName);
    }
  }

  @Then("I should receive correctly generated username {string}")
  public void i_should_receive_correctly_generated_username(String expectedUsername) {

    valueTagMap.forEach(
        (key, value) -> {
          if (expectedUsername.startsWith(key)) {
            assertEquals(expectedUsername, userService.generateUsername(key, value));
          }
        });
  }

}