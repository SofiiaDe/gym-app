package com.xstack.gymapp.service.impl;

import com.xstack.gymapp.exception.EntityNotFoundException;
import com.xstack.gymapp.model.dto.TraineeDto;
import com.xstack.gymapp.model.dto.TrainerDto;
import com.xstack.gymapp.model.dto.TrainingDto;
import com.xstack.gymapp.model.entity.Trainee;
import com.xstack.gymapp.model.entity.Trainer;
import com.xstack.gymapp.model.entity.Training;
import com.xstack.gymapp.model.entity.TrainingType;
import com.xstack.gymapp.model.entity.User;
import com.xstack.gymapp.model.enumeration.ActionType;
import com.xstack.gymapp.model.mapper.CycleAvoidingMappingContext;
import com.xstack.gymapp.model.mapper.TraineeMapper;
import com.xstack.gymapp.model.mapper.TrainerMapper;
import com.xstack.gymapp.model.mapper.TrainingMapper;
import com.xstack.gymapp.model.payload.AddTrainingRequest;
import com.xstack.gymapp.model.payload.TraineeTrainingInfoDto;
import com.xstack.gymapp.model.payload.TrainerShortInfo;
import com.xstack.gymapp.model.payload.TrainerTrainingInfoDto;
import com.xstack.gymapp.model.payload.TrainerWorkloadRequest;
import com.xstack.gymapp.repository.*;
import com.xstack.gymapp.search.TraineeTrainingSearchCriteria;
import com.xstack.gymapp.search.TrainerTrainingSearchCriteria;
import com.xstack.gymapp.service.TokenService;
import com.xstack.gymapp.service.TraineeService;
import com.xstack.gymapp.service.TrainerService;
import com.xstack.gymapp.service.TrainingService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Log4j2
public class TrainingServiceImpl implements TrainingService {

    private static final String TRAINING_NOT_FOUND_BY_ID = "Cannot find the training with ID=";
    private static final String SERVICE_NAME = "gym-app-crm";
    private static final String TRAINEE_NOT_FOUND_BY_USERNAME = "Cannot find the trainee with username = ";
    private static final String TRAINER_NOT_FOUND_BY_USERNAME = "Cannot find the trainer with username = ";
    private static final String USER_NOT_FOUND_BY_USERNAME = "Cannot find the user with username = ";
    private final TrainingRepository trainingRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final TrainingMapper trainingMapper;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TraineeMapper traineeMapper;
    private final TokenService tokenService;
    private final TrainerMapper trainerMapper;

    private static int trainingAddedId = 0;
    private final Counter trainingCounter;
    private final RestTemplate restTemplate;

//  @Autowired
//  @Lazy
//  private EurekaClient eurekaClient;

//  @Value("${spring.application.name}")
//  private String appName;

    public TrainingServiceImpl(TrainingRepository trainingRepository,
                               TrainingTypeRepository trainingTypeRepository,
                               UserRepository userRepository,
                               TrainerRepository trainerRepository,
                               TraineeRepository traineeRepository,
                               TrainingMapper trainingMapper,
                               TraineeService traineeService,
                               TrainerService trainerService,
                               TokenService tokenService,
                               TraineeMapper traineeMapper,
                               TrainerMapper trainerMapper,
                               CompositeMeterRegistry meterRegistry,
                               RestTemplate restTemplate) {
        this.trainingRepository = trainingRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.userRepository = userRepository;
        this.trainerRepository = trainerRepository;
        this.traineeRepository = traineeRepository;
        this.trainingMapper = trainingMapper;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.tokenService = tokenService;
        this.traineeMapper = traineeMapper;
        this.trainerMapper = trainerMapper;
        trainingCounter = meterRegistry.counter("add.training");
        AtomicInteger activeUsers = meterRegistry.gauge("number.of.active.users", new AtomicInteger(0));
        Random random = new Random();
        activeUsers.set(random.nextInt());
        this.restTemplate = restTemplate;
    }

    public void addTraining(AddTrainingRequest request) {
        String traineeUsername = request.getTraineeUsername();
        User traineeUser = userRepository.findByUsername(traineeUsername)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_BY_USERNAME + traineeUsername));
        Trainee trainee = traineeRepository.findByUser(traineeUser)
                .orElseThrow(() -> new EntityNotFoundException(TRAINEE_NOT_FOUND_BY_USERNAME + traineeUsername));

        String trainerUsername = request.getTrainerUsername();
        User trainerUser = userRepository.findByUsername(trainerUsername)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_BY_USERNAME + trainerUsername));
        Trainer trainer = trainerRepository.findByUser(trainerUser)
                .orElseThrow(() -> new EntityNotFoundException(TRAINER_NOT_FOUND_BY_USERNAME + trainerUsername));

        trainer.getTrainees().add(trainee);
        trainee.getTrainers().add(trainer);

        Training training = Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingName(request.getTrainingName())
                .trainingType(trainingTypeRepository.findTrainingTypeByTrainingTypeName(
                        request.getTrainingType()))
                .trainingDate(request.getTrainingDate())
                .trainingDuration(request.getTrainingDuration())
                .build();
        Training savedTraining = trainingRepository.save(training);
        trainerRepository.save(trainer);
        traineeRepository.save(trainee);

        trainingAddedId += 1;
        trainingCounter.increment();
        log.info("Added new training with ID = " + trainingAddedId);

        updateTrainerWorkload(createTrainerWorkloadRequest(savedTraining, ActionType.ADD));
    }

    public TrainingDto getTraining(Long trainingId) {
        return trainingMapper.toDto(trainingRepository.findById(trainingId)
                        .orElseThrow(() -> new EntityNotFoundException(TRAINING_NOT_FOUND_BY_ID + trainingId)),
                new CycleAvoidingMappingContext());
    }

    @Transactional(readOnly = true)
    public List<TraineeTrainingInfoDto> getTraineeTrainingsList(
            TraineeTrainingSearchCriteria searchCriteria) {
        TraineeDto traineeDto = traineeService.getTraineeByUsername(searchCriteria.getUsername());
        Trainee trainee = traineeMapper.toEntity(traineeDto, new CycleAvoidingMappingContext());

        Specification<Training> spec = (root, query, cb) -> {
            Predicate predicate = cb.equal(root.get("trainee"), trainee);

            if (searchCriteria.getPeriodFrom() != null) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("trainingDate"), searchCriteria.getPeriodFrom()));
            }
            if (searchCriteria.getPeriodTo() != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("trainingDate"), searchCriteria.getPeriodTo()));
            }
            if (searchCriteria.getTrainerName() != null) {
                Join<Training, Trainer> trainerJoin = root.join("trainer");
                Join<Trainer, User> userJoin = trainerJoin.join("user");
                predicate = cb.and(predicate, cb.like(userJoin.get("firstName"),
                        "%" + searchCriteria.getTrainerName() + "%"));
            }
            if (searchCriteria.getTrainerName() != null) {
                Join<Training, Trainer> trainerJoin = root.join("trainer");
                Join<Trainer, User> userJoin = trainerJoin.join("user");
                predicate = cb.and(predicate, cb.like(userJoin.get("lastName"),
                        "%" + searchCriteria.getTrainerName() + "%"));
            }
            if (searchCriteria.getTrainerName() != null) {
                Join<Training, Trainer> trainerJoin = root.join("trainer");
                Join<Trainer, User> userJoin = trainerJoin.join("user");
                predicate = cb.and(predicate, cb.like(userJoin.get("username"),
                        "%" + searchCriteria.getTrainerName() + "%"));
            }
            if (searchCriteria.getTrainingType() != null) {
                Join<Training, TrainingType> trainingTypeJoin = root.join("trainingType");
                predicate = cb.and(predicate,
                        cb.equal(trainingTypeJoin.get("trainingType"), searchCriteria.getTrainingType()));
            }
            return predicate;
        };

        List<Training> trainings = trainingRepository.findAll(spec);

        return trainings.stream()
                .map(this::mapToTrainingInfoDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TrainerTrainingInfoDto> getTrainerTrainingsList(
            TrainerTrainingSearchCriteria searchCriteria) {
        TrainerDto trainerDto = trainerService.getTrainerByUsername(searchCriteria.getUsername());
        Trainer trainer = trainerMapper.toEntity(trainerDto, new CycleAvoidingMappingContext());

        Specification<Training> spec = (root, query, cb) -> {
            Predicate predicate = cb.equal(root.get("trainer"), trainer);

            if (searchCriteria.getPeriodFrom() != null) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("trainingDate"), searchCriteria.getPeriodFrom()));
            }
            if (searchCriteria.getPeriodTo() != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("trainingDate"), searchCriteria.getPeriodTo()));
            }
            if (searchCriteria.getTraineeName() != null) {
                Join<Training, Trainee> traineeJoin = root.join("trainee");
                Join<Trainee, User> userJoin = traineeJoin.join("user");
                predicate = cb.and(predicate, cb.like(userJoin.get("firstName"),
                        "%" + searchCriteria.getTraineeName() + "%"));
            }
            if (searchCriteria.getTraineeName() != null) {
                Join<Training, Trainee> traineeJoin = root.join("trainee");
                Join<Trainee, User> userJoin = traineeJoin.join("user");
                predicate = cb.and(predicate, cb.like(userJoin.get("lastName"),
                        "%" + searchCriteria.getTraineeName() + "%"));
            }
            if (searchCriteria.getTraineeName() != null) {
                Join<Training, Trainee> traineeJoin = root.join("trainee");
                Join<Trainee, User> userJoin = traineeJoin.join("user");
                predicate = cb.and(predicate, cb.like(userJoin.get("username"),
                        "%" + searchCriteria.getTraineeName() + "%"));
            }
            return predicate;
        };

        List<Training> trainings = trainingRepository.findAll(spec);

        return trainings.stream()
                .map(this::mapToTrainerTrainingInfoDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TrainerShortInfo> getUnassignedActiveTrainersForTrainee(String traineeUsername) {
        TraineeDto trainee = traineeService.getTraineeByUsername(traineeUsername);
        List<TrainerDto> allActiveTrainers = trainerService.getAllTrainersByIsActive(true);

        Set<TrainerDto> assignedTrainers = new HashSet<>(trainee.getTrainers());

        List<TrainerDto> unassignedActiveTrainers = allActiveTrainers.stream()
                .filter(trainer -> !assignedTrainers.contains(trainer))
                .toList();

        return unassignedActiveTrainers.stream()
                .map(this::mapToTrainerInfoDto)
                .toList();
    }

    @Override
    public void deleteTraining(Long trainingId) {
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new EntityNotFoundException(TRAINING_NOT_FOUND_BY_ID + trainingId));
        trainingRepository.deleteById(training.getId());
        updateTrainerWorkload(createTrainerWorkloadRequest(training, ActionType.DELETE));
    }

    @CircuitBreaker(name = SERVICE_NAME, fallbackMethod = "fallbackMethod")
    public void updateTrainerWorkload(TrainerWorkloadRequest request) {
        String updateTrainerWorkloadUrl = "http://localhost:8081/api/trainer-workload/update";

        TrainerDto trainerDto = trainerService.getTrainerByUsername(request.getUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization",
                "Bearer " + tokenService.getValidJWTTokenByUserId(trainerDto.getUser().getId()));
        HttpEntity<TrainerWorkloadRequest> requestEntity = new HttpEntity<>(request, headers);

        restTemplate.exchange(
                updateTrainerWorkloadUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }

    public void fallbackMethod() {
        log.error("Trainer Workload Service is not responding. Request failed!");
    }

    private TrainerShortInfo mapToTrainerInfoDto(TrainerDto trainerDto) {
        return TrainerShortInfo.builder()
                .username(trainerDto.getUser().getUsername())
                .firstName(trainerDto.getUser().getFirstName())
                .lastName(trainerDto.getUser().getLastName())
                .specialization(trainerDto.getSpecialization().getTrainingTypeName())
                .build();
    }

    private TraineeTrainingInfoDto mapToTrainingInfoDto(Training training) {
        return TraineeTrainingInfoDto.builder()
                .trainingName(training.getTrainingName())
                .trainingDate(training.getTrainingDate())
                .trainingType(training.getTrainingType().getTrainingTypeName())
                .trainingDuration(training.getTrainingDuration())
                .trainerName(training.getTrainer().getUser().getFirstName() + " " +
                        training.getTrainer().getUser().getLastName())
                .build();
    }

    private TrainerTrainingInfoDto mapToTrainerTrainingInfoDto(Training training) {
        return TrainerTrainingInfoDto.builder()
                .trainingName(training.getTrainingName())
                .trainingDate(training.getTrainingDate())
                .trainingType(training.getTrainingType().getTrainingTypeName())
                .trainingDuration(training.getTrainingDuration())
                .traineeName(training.getTrainee().getUser().getFirstName() + " " +
                        training.getTrainer().getUser().getLastName())
                .build();
    }

    private TrainerWorkloadRequest createTrainerWorkloadRequest(Training training,
                                                                ActionType actionType) {
        User user = training.getTrainer().getUser();
        return TrainerWorkloadRequest.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .isActive(user.isActive())
                .trainingDate(training.getTrainingDate())
                .trainingDuration(training.getTrainingDuration())
                .actionType(actionType)
                .build();
    }

}
