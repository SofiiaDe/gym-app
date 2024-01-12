package com.xstack.gymapp.database;

import com.xstack.gymapp.model.dto.TrainerDto;
import com.xstack.gymapp.model.dto.UserDto;
import com.xstack.gymapp.model.entity.Trainer;
import com.xstack.gymapp.model.entity.User;
import com.xstack.gymapp.model.payload.TrainerRegistrationRequest;
import com.xstack.gymapp.service.TrainerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
public class TrainerServiceContainerTest {

    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @Container
    static MySQLContainer mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0-debian"));

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("spring.datasource.url", () -> mySQLContainer.getJdbcUrl());
        registry.add("spring.datasource.driverClassName", () -> mySQLContainer.getDriverClassName());
        registry.add("spring.datasource.username", () -> mySQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> mySQLContainer.getPassword());
        registry.add("spring.flyway.enabled", () -> "true");
    }

    @Autowired
    private TrainerService trainerService;

    @Test
    void testCreateTrainer() {
        trainerService.createTrainer(TrainerRegistrationRequest.builder()
                .firstName("John")
                .lastName("McClane")
                .specialization(1L)
                .build());
        trainerService.createTrainer(TrainerRegistrationRequest.builder()
                .firstName("Chandler")
                .lastName("Bing")
                .specialization(2L)
                .build());
        trainerService.createTrainer(TrainerRegistrationRequest.builder()
                .firstName("Joey")
                .lastName("Tribbiani")
                .specialization(3L)
                .build());
        trainerService.createTrainer(TrainerRegistrationRequest.builder()
                .firstName("John")
                .lastName("Kennedy")
                .specialization(4L)
                .build());

        List<TrainerDto> trainers = trainerService.getAllTrainersByIsActive(true);

        assertNotNull(trainers);
        assertEquals(4, trainers.size());
        assertEquals("Kennedy", trainers.get(0).getUser().getLastName());
        assertEquals("McClane", trainers.get(1).getUser().getLastName());
        assertEquals("Rambo", trainers.get(2).getUser().getLastName());
        assertEquals("Wick", trainers.get(3).getUser().getLastName());
    }

//    @Test
//    void testCreateTrainerThrowsExceptionOnDuplicateId() {
//        assertThrows(DataIntegrityViolationException.class,
//                () -> trainerService.createTrainer(TrainerRegistrationRequest.builder()
//                        .firstName("John")
//                        .lastName("Kennedy")
//                        .specialization(4L)
//                        .build()),
//                "Duplicate entry 'John-Wick' for key 'users.uc_user_first_last_name");
//    }
}
