package com.xstack.gymapp.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Log4j2
public class LoggingInterceptor implements HandlerInterceptor {

  private static final String TRANSACTION_ID = "transactionId";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws IOException {
    String transactionId = UUID.randomUUID().toString();
    String requestBody = request.getReader().lines().collect(
        Collectors.joining(System.lineSeparator()));
    MDC.put(TRANSACTION_ID, transactionId);
    log.info("TransactionId: {}. Request received for endpoint: {}", transactionId,
        request.getRequestURI());
    log.debug("TransactionId: {}. Request Body: {}", transactionId, requestBody);
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) {
    String transactionId = MDC.get(TRANSACTION_ID);
    String responseStatus = Integer.toString(response.getStatus());

    if (response instanceof ContentCachingResponseWrapper) {
      ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
      byte[] responseBody = responseWrapper.getContentAsByteArray();

      log.info("TransactionId: {}. Response sent for endpoint: {}. Status: {}",
          transactionId, request.getRequestURI(), responseStatus);
      log.debug("TransactionId: {}. Response Body: {}", transactionId, new String(responseBody));
    }

    log.info("TransactionId: {}. Response sent for endpoint: {}. Status: {}",
        transactionId, request.getRequestURI(), response.getStatus());

    MDC.remove(TRANSACTION_ID);
  }

}
