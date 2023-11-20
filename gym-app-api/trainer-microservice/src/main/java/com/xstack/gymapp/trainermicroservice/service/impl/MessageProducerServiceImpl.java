package com.xstack.gymapp.trainermicroservice.service.impl;

import com.xstack.gymapp.trainermicroservice.model.payload.Message;
import com.xstack.gymapp.trainermicroservice.service.MessageProducerService;
import jakarta.jms.Queue;
import lombok.AllArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageProducerServiceImpl implements MessageProducerService {

  private final Queue queue;
  private final JmsTemplate jmsTemplate;

  public void sendMessage(String messageContent) {
    Message message = new Message();
    message.setContent(messageContent);

    jmsTemplate.convertAndSend(queue, message);
  }
}