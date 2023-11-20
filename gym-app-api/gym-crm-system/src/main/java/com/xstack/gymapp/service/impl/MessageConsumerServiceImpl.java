package com.xstack.gymapp.service.impl;

import com.xstack.gymapp.exception.MessageProcessingException;
import com.xstack.gymapp.model.Message;
import com.xstack.gymapp.service.MessageConsumerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@EnableJms
public class MessageConsumerServiceImpl implements MessageConsumerService {

  @JmsListener(destination = "microservicesCommunicationQueue")
  public void receiveMessage(Message message) {
    log.info("Received message: " + message.getContent());

    if (message.getContent().equals("error")) {
      throw new MessageProcessingException("Cannot retrieve message content.");
    }
  }

}
