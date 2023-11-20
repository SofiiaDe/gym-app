package com.xstack.gymapp.trainermicroservice.service;


import com.xstack.gymapp.trainermicroservice.model.payload.TrainerWorkloadRequest;

public interface TrainerWorkloadService {

  void updateTrainerWorkload(TrainerWorkloadRequest request);

}
