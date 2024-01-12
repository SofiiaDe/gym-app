package com.xstack.gymapp.kafka;

import com.xstack.gymapp.message.KafkaMessageConsumer;
import com.xstack.gymapp.service.TrainerService;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;


@EmbeddedKafka
@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TrainerKafkaConsumerTest {

    private final String TOPIC_NAME = "change-active-status-topic";

    private Producer<String, String> producer;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @SpyBean
    private KafkaMessageConsumer kafkaMessageConsumer;

    @MockBean
    private TrainerService trainerService;

    @Captor
    private ArgumentCaptor<String> topicArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> messageArgumentCaptor;

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:h2:mem:test");
        registry.add("spring.datasource.driverClassName", () -> "org.h2.Driver");
        registry.add("spring.datasource.username", () -> "root");
        registry.add("spring.datasource.password", () -> "secret");
        registry.add("spring.flyway.enabled", () -> "false");
    }

    @BeforeAll
    void setUp() {
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        producer = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new StringSerializer()).createProducer();
    }

    @Test
    void listenKafkaMessages() {
        // Write a message (John Wick user) to Kafka using a test producer
        String uuid = "11111";
        String message = "The profile of the trainer John Wick (ID=1) was deactivated.";
        producer.send(new ProducerRecord<>(TOPIC_NAME, 0, uuid, message));
        producer.flush();

        // Read the message and assert its properties
        verify(kafkaMessageConsumer, timeout(10000).times(1))
                .listen(messageArgumentCaptor.capture(), topicArgumentCaptor.capture());
        assertEquals(TOPIC_NAME, topicArgumentCaptor.getValue());
        assertEquals(message, messageArgumentCaptor.getValue());
    }

    @AfterAll
    void shutdown() {
        producer.close();
    }
}
