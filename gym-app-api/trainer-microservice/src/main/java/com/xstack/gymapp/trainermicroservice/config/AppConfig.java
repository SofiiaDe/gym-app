package com.xstack.gymapp.trainermicroservice.config;

import jakarta.jms.Queue;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableEurekaServer
public class AppConfig {

  @Bean
  public Queue queue(){
    return new ActiveMQQueue("microservicesCommunicationQueue");
  }
}
