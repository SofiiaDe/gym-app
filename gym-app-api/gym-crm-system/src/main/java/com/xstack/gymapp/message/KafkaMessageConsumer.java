package com.xstack.gymapp.message;

import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class KafkaMessageConsumer {

    @KafkaListener(topics = "change-active-status-topic", groupId = "gym-group-id")
    public void listen(String message,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Received message \"{}\" from {} topic.", message, topic);
    }
}
