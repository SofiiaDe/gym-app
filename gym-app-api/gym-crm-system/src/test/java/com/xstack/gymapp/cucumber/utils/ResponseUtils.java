package com.xstack.gymapp.cucumber.utils;

import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.ValidatableResponse;
import java.util.List;

public class ResponseUtils {

  public static ValidatableResponse getResponse() {
    return RequestUtils.getResponse();
  }

  public static int getIntFromResponse(String path) {
    return getResponse()
        .extract()
        .jsonPath()
        .getInt(path);
  }

  public static String getStringFromResponse(String path) {
    return getResponse()
        .extract()
        .jsonPath()
        .getString(path);
  }

  public static <T> List<T> getListFromResponse(String path, Class<T> type) {
    return getResponse()
        .extract()
        .jsonPath()
        .getList(path, type);
  }

  public static int getStatusCodeFromResponse() {
    return getResponse()
        .extract()
        .statusCode();
  }

  public static <T> T getObjectFromResponse(String path, Class<T> type) {
    return getResponse()
        .extract()
        .jsonPath()
        .getObject(path, type);
  }

  public static void validateResponseAgainstJSONSchema(String filepath) {
    JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory
        .newBuilder()
        .setValidationConfiguration(ValidationConfiguration
            .newBuilder()
            .setDefaultVersion(SchemaVersion.DRAFTV4)
            .freeze())
        .freeze();
    getResponse()
        .assertThat()
        .body(JsonSchemaValidator
            .matchesJsonSchemaInClasspath(filepath)
            .using(jsonSchemaFactory));
  }

}
