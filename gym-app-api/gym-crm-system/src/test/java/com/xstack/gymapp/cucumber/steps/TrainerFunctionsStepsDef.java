package com.xstack.gymapp.cucumber.steps;

import com.xstack.gymapp.cucumber.utils.RequestUtils;
import io.cucumber.java.en.When;

public class TrainerFunctionsStepsDef {

  @When("Trainer gets his profile")
  public void trainerGetsHisProfile() {
    var x = CommonStepsDef.accessToken;
    String body = "{\n"
        + "    \"username\": \"Henry.Smith\",\n"
        + "    \"password\": \"hbE3nQjCGP\"\n"
        + "}";
    RequestUtils.get("trainer/profile", body);
  }

  @When("Trainer updates his profile")
  public void trainerUpdatesHisProfile() {
    String body = "{\n"
        + "    \"username\": \"Henry.Smith\",\n"
        + "    \"password\": \"hbE3nQjCGP\",\n"
        + "    \"firstName\": \"Henry\",\n"
        + "    \"lastName\": \"Smith\",\n"
        + "    \"specialization\": \"Fitness\",\n"
        + "    \"isActive\": false\n"
        + "}";
    RequestUtils.put("trainer/update", body);
  }

  @When("Trainer gets a list of his trainings")
  public void adminUserGetsAListOfHisTrainings() {
    String body = "{\n"
        + "    \"username\": \"Henry.Smith\",\n"
        + "    \"password\": \"hbE3nQjCGP\",\n"
        + "    \"periodFrom\": \"2023-08-16\",\n"
        + "    \"periodTo\": \"2023-10-30\",\n"
        + "    \"traineeName\": null\n"
        + "}   ";
    RequestUtils.get("training/trainer/list", body);
  }

  @When("Trainer activates his status")
  public void trainerActivatesHisStatus() {
    String body = "{\n"
        + "    \"username\": \"Henry.Smith\",\n"
        + "    \"password\": \"hbE3nQjCGP\",\n"
        + "    \"isActive\": true\n"
        + "}";
    RequestUtils.patch("trainer/active-status", body);
  }

}
