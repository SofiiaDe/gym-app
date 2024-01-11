package com.xstack.gymapp.message;

public interface MessageProducer {

    void sendMessage(String topic, String message);
}
