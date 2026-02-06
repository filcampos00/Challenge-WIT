package witsoftware.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import witsoftware.common.dtos.CalculationRequest;

import java.util.concurrent.CompletableFuture;

@Component
public class CalculationRequestsProducer {
    private final KafkaTemplate<String, CalculationRequest> kafkaTemplate;
    private final String topicName;

    public CalculationRequestsProducer(KafkaTemplate<String, CalculationRequest> kafkaTemplate,
                                       @Value("${KAFKA_TOPIC_REQUEST}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public CompletableFuture<SendResult<String, CalculationRequest>> send(CalculationRequest request) {
        return kafkaTemplate.send(topicName, request);
    }
}
