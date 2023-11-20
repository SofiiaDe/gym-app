package com.xstack.gymapp.actuator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class UrlHealthIndicator implements HealthIndicator {

  private static final String TRAINEE_PROFILE_URL = "http://localhost:8080/api/trainee/profile";

  @Override
  public Health health() {

    try {
      URL url = new URL(TRAINEE_PROFILE_URL);
      int port = url.getPort();
      if (port == -1) {
        port = url.getDefaultPort();
      }

      try (Socket socket = new Socket(url.getHost(), port)) {
        log.info("Socket: " + socket);
      } catch (IOException e) {
        log.warn("Failed to open socket to " + TRAINEE_PROFILE_URL);
        return Health.down().withDetail("smoke test", e.getMessage()).build();
      }
    } catch (MalformedURLException e1) {
      log.warn("Malformed URL: " + TRAINEE_PROFILE_URL);
      return Health.down().withDetail("smoke test", e1.getMessage()).build();
    }

    return Health.up().build();
  }

}
