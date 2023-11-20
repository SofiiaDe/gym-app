package com.xstack.gymapp.cucumber.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xstack.gymapp.cucumber.utils.RequestUtils;
import com.xstack.gymapp.cucumber.utils.ResponseUtils;
import com.xstack.gymapp.model.payload.LoginRequest;
import com.xstack.gymapp.model.payload.LoginResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
public class CommonStepsDef {

  private static final String BASE_URL_GYM_CRM_SYSTEM = "http://localhost:8080/";
  private static final String BASE_PATH = "api/";

  public static String accessToken;

  @Transactional
  @Given("Setup Rest Assured for gym-crm-system")
  public void setupRestAssuredForGymCRMSystem() {
    log.info("The BASE_URL_GYM_CRM_SYSTEM is {}", BASE_URL_GYM_CRM_SYSTEM);
    log.info("The BASE_PATH is {}", BASE_PATH);
    RestAssured.baseURI = BASE_URL_GYM_CRM_SYSTEM;
    RestAssured.basePath = BASE_PATH;
  }

  @Given("I have authenticated as username {string} with password {string}")
  public LoginResponse login(String username, String password) throws JsonProcessingException {

    if (accessToken != null && !accessToken.isEmpty()) {
      return LoginResponse.builder()
          .accessToken(accessToken)
          .isLoggedIn(true)
          .build();
    }
    LoginRequest loginRequest = LoginRequest.builder()
        .username(username)
        .password(password)
        .build();

    RequestUtils.get("auth/login", loginRequest);
    String loginResponseStr = ResponseUtils.getResponse().extract().body().asPrettyString();
    ObjectMapper objectMapper = new ObjectMapper();
    LoginResponse loginResponse = objectMapper.readValue(loginResponseStr, LoginResponse.class);
    accessToken = loginResponse.getAccessToken();
    return loginResponse;
  }

  @Then("Status code should be {int}")
  public void statusCodeShouldBe(int expectedStatusCode) {
    int actualStatusCode = ResponseUtils.getStatusCodeFromResponse();
    assertEquals(expectedStatusCode, actualStatusCode,"Response should be " + expectedStatusCode);
  }

}
