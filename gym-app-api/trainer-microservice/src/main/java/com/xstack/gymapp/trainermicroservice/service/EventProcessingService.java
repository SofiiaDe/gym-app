package com.xstack.gymapp.trainermicroservice.service;

import com.xstack.gymapp.trainermicroservice.model.payload.EventRequest;

public interface EventProcessingService {

  void processEvent(EventRequest eventRequest);

}
