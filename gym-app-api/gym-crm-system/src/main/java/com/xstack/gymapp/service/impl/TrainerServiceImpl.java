package com.xstack.gymapp.service.impl;

import com.xstack.gymapp.exception.EntityNotFoundException;
import com.xstack.gymapp.exception.TraineeProcessingException;
import com.xstack.gymapp.exception.TrainerProcessingException;
import com.xstack.gymapp.message.MessageProducer;
import com.xstack.gymapp.model.dto.TraineeDto;
import com.xstack.gymapp.model.dto.TrainerDto;
import com.xstack.gymapp.model.dto.TrainingTypeDto;
import com.xstack.gymapp.model.dto.UserDto;
import com.xstack.gymapp.model.entity.Trainer;
import com.xstack.gymapp.model.entity.TrainingType;
import com.xstack.gymapp.model.entity.User;
import com.xstack.gymapp.model.mapper.CycleAvoidingMappingContext;
import com.xstack.gymapp.model.mapper.TrainerMapper;
import com.xstack.gymapp.model.mapper.UserMapper;
import com.xstack.gymapp.model.payload.*;
import com.xstack.gymapp.repository.TrainerRepository;
import com.xstack.gymapp.repository.UserRepository;
import com.xstack.gymapp.service.TrainerService;
import com.xstack.gymapp.service.TrainingTypeService;
import com.xstack.gymapp.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@AllArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private static final String TRAINER_NOT_FOUND_BY_ID = "Cannot find the trainer with ID=";
    private static final String TRAINER_NOT_FOUND_BY_USERNAME = "Cannot find the trainer with username = ";
    private static final String USER_NOT_FOUND = "Cannot find the user with username = ";
    private static final String UPDATE_EXCEPTION = "Cannot update the trainer with username = ";

    private TrainerRepository trainerRepository;
    private UserRepository userRepository;
    private TrainingTypeService trainingTypeService;
    private TrainerMapper trainerMapper;
    private UserMapper userMapper;
    private UserService userService;
    private final PasswordEncoder passwordEncoder;
    private MessageProducer messageProducer;

    @Override
    @Transactional
    public RegistrationResponse createTrainer(TrainerRegistrationRequest request) {

        String username = userService.generateUsername(request.getFirstName(),
                request.getLastName());
        String password = userService.generatePassword();

        UserDto userDto = UserDto.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(username)
                .password(passwordEncoder.encode(password))
                .isActive(true)
                .build();

        TrainingTypeDto trainingTypeDto = trainingTypeService.getTrainingTypeById(
                request.getSpecialization());

        TrainerDto trainerDto = TrainerDto.builder()
                .user(userDto)
                .specialization(trainingTypeDto)
                .build();

        trainerRepository.save(trainerMapper.toEntity(trainerDto, new CycleAvoidingMappingContext()));

        return RegistrationResponse.builder()
                .username(username)
                .password(password)
                .build();
    }

    public TrainerDto updateTrainer(TrainerDto trainerDto) {
        String errorMessage = UPDATE_EXCEPTION + trainerDto.getId();
        if (!trainerRepository.existsById(trainerDto.getId())) {
            log.error(errorMessage);
            throw new TrainerProcessingException(errorMessage);
        }
        Trainer trainerToBeUpdated = trainerMapper.toEntity(trainerDto,
                new CycleAvoidingMappingContext());
        try {
            Trainer trainer = trainerRepository.save(trainerToBeUpdated);
            return trainerMapper.toDto(trainer, new CycleAvoidingMappingContext());
        } catch (Exception e) {
            log.error(errorMessage);
            throw new TrainerProcessingException(errorMessage);
        }
    }

    @Override
    public UpdateTrainerProfileResponse updateTrainer(UpdateTrainerProfileRequest updateRequest) {
        User user = userRepository.findByUsername(updateRequest.getUsername())
                .orElseThrow(
                        () -> new EntityNotFoundException(USER_NOT_FOUND + updateRequest.getUsername()));
        Trainer trainer = trainerRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException(
                        TRAINER_NOT_FOUND_BY_USERNAME + updateRequest.getUsername()));
        try {
            if (updateRequest.getUsername() != null) {
                trainer.getUser().setUsername(updateRequest.getUsername());
            }
            if (updateRequest.getFirstName() != null) {
                trainer.getUser().setFirstName(updateRequest.getFirstName());
            }
            if (updateRequest.getLastName() != null) {
                trainer.getUser().setLastName(updateRequest.getLastName());
            }
            if (updateRequest.getSpecialization() != null) {
                trainer.setSpecialization(TrainingType.builder()
                        .trainingTypeName(updateRequest.getSpecialization())
                        .build());
            }

            trainer.getUser().setActive(updateRequest.isActive());

            Trainer updatedTrainer = trainerRepository.save(trainer);
            List<TraineeShortInfo> traineesShortInfo = getTraineesShortInfo(
                    trainerMapper.toDto(updatedTrainer, new CycleAvoidingMappingContext()));

            return UpdateTrainerProfileResponse.builder()
                    .username(trainer.getUser().getUsername())
                    .firstName(trainer.getUser().getFirstName())
                    .lastName(trainer.getUser().getLastName())
                    .specialization(trainer.getSpecialization().getTrainingTypeName())
                    .isActive(trainer.getUser().isActive())
                    .trainees(traineesShortInfo)
                    .build();
        } catch (Exception e) {
            log.error(UPDATE_EXCEPTION + updateRequest.getUsername());
            throw new TraineeProcessingException(UPDATE_EXCEPTION + updateRequest.getUsername());
        }
    }

    @Override
    public TrainerDto getTrainerById(Long trainerId) {
        return trainerMapper.toDto(trainerRepository.findById(trainerId)
                        .orElseThrow(() -> new EntityNotFoundException(TRAINER_NOT_FOUND_BY_ID + trainerId)),
                new CycleAvoidingMappingContext());
    }

    @Override
    public TrainerDto getTrainerByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND + username));
        return trainerMapper.toDto(trainerRepository.findByUser(user)
                        .orElseThrow(() -> new EntityNotFoundException(TRAINER_NOT_FOUND_BY_USERNAME + username)),
                new CycleAvoidingMappingContext());
    }

    @Override
    public void activateTrainer(Long trainerId) {
        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new EntityNotFoundException(TRAINER_NOT_FOUND_BY_ID + trainerId));
        trainer.getUser().setActive(true);
        trainerRepository.save(trainer);
        messageProducer.sendMessage("change-active-status-topic", buildMessageAboutTrainerActiveStatusChange(trainer));
    }

    @Override
    public void deactivateTrainer(Long trainerId) {
        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new EntityNotFoundException(TRAINER_NOT_FOUND_BY_ID + trainerId));
        trainer.getUser().setActive(false);
        trainerRepository.save(trainer);
        messageProducer.sendMessage("change-active-status-topic", buildMessageAboutTrainerActiveStatusChange(trainer));
    }

    @Override
    public void changeTrainerActiveStatus(ChangeUserActiveStatusRequest request) {
        TrainerDto trainerDto = getTrainerByUsername(request.getUsername());
        Trainer trainer = trainerMapper.toEntity(trainerDto, new CycleAvoidingMappingContext());
        trainer.getUser().setActive(request.isActive());
        userRepository.save(trainer.getUser());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainerDto> getAllTrainersByIsActive(boolean isActive) {
        List<User> users = userRepository.findAllByIsActive(isActive);
        return trainerRepository.findAllByUserIn(users).stream()
                .map((Trainer entity) -> trainerMapper.toDto(entity, new CycleAvoidingMappingContext()))
                .toList();
    }

    @Override
    public TrainerProfile getTrainerProfile(String username) {
        TrainerDto trainer = getTrainerByUsername(username);

        List<TraineeShortInfo> traineesShortInfo = getTraineesShortInfo(trainer);

        return TrainerProfile.builder()
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .specialization(trainer.getSpecialization().getTrainingTypeName())
                .isActive(trainer.getUser().isActive())
                .trainees(traineesShortInfo)
                .build();
    }

    @Override
    public boolean isTrainerByUserDto(UserDto userDto) {
        User user = userMapper.toEntity(userDto, new CycleAvoidingMappingContext());
        return trainerRepository.findByUser(user).isPresent();
    }

    @Override
    public List<TrainerShortInfo> getActiveTrainers(String traineeUsername) {
        List<Trainer> allActiveTrainers = trainerRepository.findAllByUserIsActive(true);
        return allActiveTrainers.stream().map(trainer -> TrainerShortInfo.builder()
                .username(trainer.getUser().getUsername())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .specialization(trainer.getSpecialization().getTrainingTypeName().toLowerCase())
                .build()).toList();
    }

    private List<TraineeShortInfo> getTraineesShortInfo(TrainerDto trainer) {
        List<TraineeShortInfo> traineeShortInfoList = new ArrayList<>();
        if (trainer.getTrainees() == null || trainer.getTrainees().isEmpty()) {
            traineeShortInfoList = Collections.emptyList();
        } else {
            for (TraineeDto trainee : trainer.getTrainees()) {
                TraineeShortInfo traineeShortInfo = TraineeShortInfo.builder()
                        .username(trainee.getUser().getUsername())
                        .firstName(trainee.getUser().getFirstName())
                        .lastName(trainee.getUser().getLastName())
                        .isActive(trainee.getUser().isActive())
                        .build();
                traineeShortInfoList.add(traineeShortInfo);
            }
        }
        return traineeShortInfoList;
    }

    private String buildMessageAboutTrainerActiveStatusChange(Trainer trainer) {
        StringBuilder sb = new StringBuilder();
        sb.append("The profile of the trainer ")
                .append(trainer.getUser().getFirstName())
                .append(" ")
                .append(trainer.getUser().getLastName())
                .append(" (ID=")
                .append(trainer.getId())
                .append(") was ")
                .append(trainer.getUser().isActive() ? "activated." : "deactivated.");
        return sb.toString();
    }
}
