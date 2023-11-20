package com.xstack.gymapp.service.impl;

import com.xstack.gymapp.exception.EntityNotFoundException;
import com.xstack.gymapp.exception.TraineeProcessingException;
import com.xstack.gymapp.model.dto.TraineeDto;
import com.xstack.gymapp.model.dto.TrainerDto;
import com.xstack.gymapp.model.dto.UserDto;
import com.xstack.gymapp.model.entity.Trainee;
import com.xstack.gymapp.model.entity.TrainingType;
import com.xstack.gymapp.model.entity.User;
import com.xstack.gymapp.model.mapper.CycleAvoidingMappingContext;
import com.xstack.gymapp.model.mapper.TraineeMapper;
import com.xstack.gymapp.model.payload.*;
import com.xstack.gymapp.repository.TraineeRepository;
import com.xstack.gymapp.repository.UserRepository;
import com.xstack.gymapp.service.TraineeService;
import com.xstack.gymapp.service.TrainerService;
import com.xstack.gymapp.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
@Service
@AllArgsConstructor
public class TraineeServiceImpl implements TraineeService {

    private static final String TRAINEE_NOT_FOUND_BY_ID = "Cannot find trainee with id = ";
    private static final String TRAINEE_NOT_FOUND_BY_USERNAME = "Cannot find the trainee with username = ";
    private static final String USER_NOT_FOUND = "Cannot find the user with username = ";
    private static final String UPDATE_EXCEPTION = "Cannot update the trainee with username = ";

    private TraineeRepository traineeRepository;
    private UserRepository userRepository;
    private TraineeMapper traineeMapper;
    private UserService userService;
    private TrainerService trainerService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public RegistrationResponse createTrainee(TraineeRegistrationRequest request) {

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

        TraineeDto traineeDto = TraineeDto.builder()
                .user(userDto)
                .address(request.getAddress())
                .birthDate(request.getBirthDate())
                .build();

        traineeRepository.save(traineeMapper.toEntity(traineeDto, new CycleAvoidingMappingContext()));

        return RegistrationResponse.builder()
                .username(username)
                .password(password)
                .build();
    }

    @Override
    public UpdateTraineeProfileResponse updateTrainee(UpdateTraineeProfileRequest updateRequest) {
        User user = userRepository.findByUsername(updateRequest.getUsername())
                .orElseThrow(
                        () -> new EntityNotFoundException(USER_NOT_FOUND + updateRequest.getUsername()));
        Trainee trainee = traineeRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException(
                        TRAINEE_NOT_FOUND_BY_USERNAME + updateRequest.getUsername()));
        try {
            if (updateRequest.getUsername() != null) {
                trainee.getUser().setUsername(updateRequest.getUsername());
            }
            if (updateRequest.getFirstName() != null) {
                trainee.getUser().setFirstName(updateRequest.getFirstName());
            }
            if (updateRequest.getLastName() != null) {
                trainee.getUser().setLastName(updateRequest.getLastName());
            }

            trainee.setBirthDate(updateRequest.getBirthDate());
            trainee.setAddress(updateRequest.getAddress());
            trainee.getUser().setActive(updateRequest.isActive());

            Trainee updatedTrainee = traineeRepository.save(trainee);
            List<TrainerShortInfo> trainersShortInfo = getTrainersShortInfo(
                    traineeMapper.toDto(updatedTrainee, new CycleAvoidingMappingContext()));

            return UpdateTraineeProfileResponse.builder()
                    .username(trainee.getUser().getUsername())
                    .firstName(trainee.getUser().getFirstName())
                    .lastName(trainee.getUser().getLastName())
                    .birthDate(trainee.getBirthDate())
                    .address(trainee.getAddress())
                    .isActive(trainee.getUser().isActive())
                    .trainers(trainersShortInfo)
                    .build();
        } catch (Exception e) {
            log.error(UPDATE_EXCEPTION + updateRequest.getUsername());
            throw new TraineeProcessingException(UPDATE_EXCEPTION + updateRequest.getUsername());
        }
    }

    @Override
    public void deleteTraineeById(Long traineeId) {
        Trainee trainee = traineeRepository.findById(traineeId)
                .orElseThrow(() -> new EntityNotFoundException(TRAINEE_NOT_FOUND_BY_ID + traineeId));
        traineeRepository.deleteById(trainee.getId());
    }

    @Override
    public void deleteTraineeByUsername(String username) {
        TraineeDto traineeDto = getTraineeByUsername(username);
        traineeRepository.delete(traineeMapper.toEntity(traineeDto, new CycleAvoidingMappingContext()));
    }

    @Override
    public TraineeDto getTraineeById(Long traineeId) {
        return traineeMapper.toDto(traineeRepository.findById(traineeId)
                        .orElseThrow(() -> new EntityNotFoundException(TRAINEE_NOT_FOUND_BY_ID + traineeId)),
                new CycleAvoidingMappingContext());
    }

    @Override
    public TraineeDto getTraineeByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND + username));
        Trainee trainee = traineeRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException(TRAINEE_NOT_FOUND_BY_USERNAME + username));
        return traineeMapper.toDto(trainee, new CycleAvoidingMappingContext());
    }

    @Override
    public void activateTrainee(Long traineeId) {
        Trainee trainee = traineeRepository.findById(traineeId)
                .orElseThrow(() -> new EntityNotFoundException(TRAINEE_NOT_FOUND_BY_ID + traineeId));
        trainee.getUser().setActive(true);
        traineeRepository.save(trainee);
    }

    @Override
    public void deactivateTrainee(Long traineeId) {
        Trainee trainee = traineeRepository.findById(traineeId)
                .orElseThrow(() -> new EntityNotFoundException(TRAINEE_NOT_FOUND_BY_ID + traineeId));

        trainee.getUser().setActive(false);
        traineeRepository.save(trainee);
    }

    @Override
    public void changeTraineeActiveStatus(ChangeUserActiveStatusRequest request) {
        TraineeDto traineeDto = getTraineeByUsername(request.getUsername());
        Trainee trainee = traineeMapper.toEntity(traineeDto, new CycleAvoidingMappingContext());
        trainee.getUser().setActive(request.isActive());
        userRepository.save(trainee.getUser());
    }

    @Override
    public TraineeProfile getTraineeProfile(String username) {
        TraineeDto trainee = getTraineeByUsername(username);

        List<TrainerShortInfo> trainersShortInfo = getTrainersShortInfo(trainee);

        return TraineeProfile.builder()
                .firstName(trainee.getUser().getFirstName())
                .lastName(trainee.getUser().getLastName())
                .birthDate(trainee.getBirthDate())
                .address(trainee.getAddress())
                .isActive(trainee.getUser().isActive())
                .trainers(trainersShortInfo)
                .build();
    }

    @Transactional
    @Override
    public List<TrainerShortInfo> updateTraineeTrainersList(UpdateTraineeTrainersListRequest request) {
        TraineeDto traineeDto = getTraineeByUsername(request.getTraineeUsername());

        try {
            List<TrainerDto> trainers = request.getTrainerUsernames().stream()
                    .map(trainerService::getTrainerByUsername)
                    .toList();

            traineeDto.setTrainers(trainers);
            Trainee updatedTrainee = traineeRepository.save(
                    traineeMapper.toEntity(traineeDto, new CycleAvoidingMappingContext()));

            return getTrainersShortInfo(
                    traineeMapper.toDto(updatedTrainee, new CycleAvoidingMappingContext()));
        } catch (Exception exception) {
            log.error(UPDATE_EXCEPTION + request.getTraineeUsername());
            throw new TraineeProcessingException(UPDATE_EXCEPTION + request.getTraineeUsername());
        }
    }

    private List<TrainerShortInfo> getTrainersShortInfo(TraineeDto trainee) {
        List<TrainerShortInfo> trainersShortInfo = new ArrayList<>();
        if (trainee.getTrainers() == null || trainee.getTrainers().isEmpty()) {
            trainersShortInfo = Collections.emptyList();
        } else {
            for (TrainerDto trainer : trainee.getTrainers()) {
                TrainerShortInfo trainerShortInfo = TrainerShortInfo.builder()
                        .username(trainer.getUser().getUsername())
                        .firstName(trainer.getUser().getFirstName())
                        .lastName(trainer.getUser().getLastName())
                        .specialization(trainer.getSpecialization().getTrainingTypeName())
                        .build();
                trainersShortInfo.add(trainerShortInfo);
            }
        }
        return trainersShortInfo;
    }

}
