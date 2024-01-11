package com.xstack.gymapp.message;

import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class KafkaMessageConsumer {

    @KafkaListener(topics = "change-active-status-topic", groupId = "gym-group-id")
    public void listen(String message) {
        log.info("Received message: " + message);
    }
}
