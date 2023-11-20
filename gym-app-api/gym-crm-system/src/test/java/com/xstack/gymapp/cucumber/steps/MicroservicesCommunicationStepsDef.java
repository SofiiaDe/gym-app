package com.xstack.gymapp.cucumber.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.xstack.gymapp.cucumber.utils.RequestUtils;
import com.xstack.gymapp.cucumber.utils.ResponseUtils;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MicroservicesCommunicationStepsDef {

  @When("Trainer adds a new training")
  public void trainerAddsANewTraining() {
    String body = "{\n"
        + "    \"traineeUsername\": \"Mark.Willson\",\n"
        + "    \"trainerUsername\": \"Henry.Smith\",\n"
        + "    \"trainingName\": \"Evening Fitness\",\n"
        + "    \"trainingType\": \"Fitness\",\n"
        + "    \"trainingDate\": \"2023-09-30\",\n"
        + "    \"trainingDuration\": 60\n"
        + "}";
    RequestUtils.post("training/add", body);
  }

  @Then("Success message {string} is returned")
  public void statusCodeShouldBe(String expectedMessage) {
    String actualMessage = ResponseUtils.getResponse().extract().body().asPrettyString();
    assertEquals(expectedMessage, actualMessage, "Response should be " + expectedMessage);
  }
}
