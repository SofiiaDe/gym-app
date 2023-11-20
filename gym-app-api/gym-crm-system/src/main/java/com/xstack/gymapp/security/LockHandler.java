package com.xstack.gymapp.security;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Component;

@Component
public class LockHandler {

  private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
  private static final String LOCKED_RESP_MSG_FORMAT = "Locked until: %s (UTC+0) due to exceeded the limit of login attempts: %s";

  private final Map<String, LockInfo> lockInfoCache = new HashMap<>();

  public void loginFailed(String username) {

    LockInfo lockInfo = getLockInfo(username);
    lockInfo.updateLoginAttemptsAndLoginDateTime();
    lockInfoCache.put(username, lockInfo);

    if (lockInfo.isReachedMaxLoginAttempts()) {
      throw new LockedException(createResponseMessage(username));
    }
  }

  private String createResponseMessage(String username) {
    LockInfo lockInfo = lockInfoCache.get(username);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
    String formattedTime = lockInfo.getLockedUntilDateTime().format(formatter);

    return String.format(LOCKED_RESP_MSG_FORMAT, formattedTime, lockInfo.getLoginAttempts());
  }

  public LockInfo getLockInfo(String username) {
    return lockInfoCache.containsKey(username) ?
        lockInfoCache.get(username) : new LockInfo(username, LocalDateTime.now(Clock.systemUTC()), 0);
  }

}
