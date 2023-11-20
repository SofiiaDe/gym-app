package com.xstack.gymapp.actuator;

import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@AllArgsConstructor
public class ApiHealthIndicator implements HealthIndicator {

  private static final String MESSAGE = "message";
  
  private final RestTemplate restTemplate;

  @Override
  public Health health() {
    try {
      String apiResponse = restTemplate.getForObject("http://localhost:8080/api/auth/login",
          String.class);

      if ("API is healthy".equals(apiResponse)) {
        return Health.up().withDetail(MESSAGE, "API is healthy").build();
      } else {
        return Health.down().withDetail(MESSAGE, "API response is not as expected").build();
      }
    } catch (Exception e) {
      return Health.down().withDetail(MESSAGE, "API is not reachable").build();
    }
  }

}
