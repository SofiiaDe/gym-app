package com.xstack.gymapp.cucumber.utils;

import com.xstack.gymapp.cucumber.steps.CommonStepsDef;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class RequestUtils {

  public static ValidatableResponse response;

  public static ValidatableResponse getResponse() {
    return response;
  }

  public static void get(String endpoint, Object body) {
    log.info(endpoint, body);
    boolean includeAccessToken =
        CommonStepsDef.accessToken != null && !CommonStepsDef.accessToken.isEmpty();

    RequestSpecification reqSpec = RestAssured
        .given()
        .spec(getRequestSpecification());

    if (includeAccessToken) {
      reqSpec
          .header("Authorization", "Bearer " + CommonStepsDef.accessToken);
    }
    response = reqSpec
        .body(body)
        .when()
        .get(endpoint)
        .then();
    logResponse(response);
  }

  public static void delete(String endpoint) {
    boolean includeAccessToken =
        CommonStepsDef.accessToken != null && !CommonStepsDef.accessToken.isEmpty();

    RequestSpecification reqSpec = RestAssured
        .given()
        .spec(getRequestSpecification());

    if (includeAccessToken) {
      reqSpec
          .header("Authorization", "Bearer " + CommonStepsDef.accessToken);
    }
    response = reqSpec
        .when()
        .delete(endpoint)
        .then();
    logResponse(response);
  }

  public static void post(String endpoint, Object body) {
    log.info(endpoint, body);

    boolean includeAccessToken =
        CommonStepsDef.accessToken != null && !CommonStepsDef.accessToken.isEmpty();

    RequestSpecification reqSpec = RestAssured
        .given()
        .spec(getRequestSpecification());

    if (includeAccessToken) {
      reqSpec
          .header("Authorization", "Bearer " + CommonStepsDef.accessToken);
    }
    response = reqSpec
        .body(body)
        .when()
        .post(endpoint)
        .then();
    logResponse(response);
  }

  public static void put(String endpoint, Object body) {
    log.info(endpoint, body);

    boolean includeAccessToken =
        CommonStepsDef.accessToken != null && !CommonStepsDef.accessToken.isEmpty();

    RequestSpecification reqSpec = RestAssured
        .given()
        .spec(getRequestSpecification());

    if (includeAccessToken) {
      reqSpec
          .header("Authorization", "Bearer " + CommonStepsDef.accessToken);
    }
    response = reqSpec
        .body(body)
        .when()
        .put(endpoint)
        .then();
    logResponse(response);
  }

  public static void patch(String endpoint, Object body) {
    log.info(endpoint, body);

    boolean includeAccessToken =
        CommonStepsDef.accessToken != null && !CommonStepsDef.accessToken.isEmpty();

    RequestSpecification reqSpec = RestAssured
        .given()
        .spec(getRequestSpecification());

    if (includeAccessToken) {
      reqSpec
          .header("Authorization", "Bearer " + CommonStepsDef.accessToken);
    }
    response = reqSpec
        .body(body)
        .when()
        .patch(endpoint)
        .then();
    logResponse(response);
  }

  private static RequestSpecification getRequestSpecification() {
    RequestSpecBuilder spec = new RequestSpecBuilder();
    return spec
        .setContentType(ContentType.JSON)
        .setAccept(ContentType.JSON)
        .build();
  }

  private static void logResponse(ValidatableResponse response) {
    log.info(response.extract().body().asPrettyString());
  }

}
