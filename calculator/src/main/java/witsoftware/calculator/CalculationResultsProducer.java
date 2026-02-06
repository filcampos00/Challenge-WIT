package witsoftware.calculator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import witsoftware.common.dtos.CalculationResponse;

@Slf4j
@Component
public class CalculationResultsProducer {
    private final KafkaTemplate<String, CalculationResponse> kafkaTemplate;
    private final String topicName;

    public CalculationResultsProducer(KafkaTemplate<String, CalculationResponse> kafkaTemplate,
                                      @Value("${KAFKA_TOPIC_RESPONSE}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void send(CalculationResponse response) {
        log.debug("Sending response to Kafka: topic={}, requestId={}", topicName, response.requestId());

        kafkaTemplate.send(topicName, response)
                .whenComplete((r, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send response to Kafka: topic={}, requestId={}",
                                topicName, response.requestId(), ex);
                    } else {
                        log.debug("Response sent to Kafka successfully: topic={}, requestId={}",
                                topicName, response.requestId());
                    }
                });
    }
}