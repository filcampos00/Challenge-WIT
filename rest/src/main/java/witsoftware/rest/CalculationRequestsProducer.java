package witsoftware.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import witsoftware.common.dtos.CalculationRequest;

import java.util.concurrent.CompletableFuture;

@Slf4j
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
        log.debug("Sending request to Kafka: topic={}, requestId={}", topicName, request.requestId());

        return kafkaTemplate.send(topicName, request)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send request to Kafka: topic={}, requestId={}",
                                topicName, request.requestId(), ex);
                    } else {
                        log.debug("Request sent to Kafka successfully: topic={}, requestId={}",
                                topicName, request.requestId());
                    }
                });
    }
}
