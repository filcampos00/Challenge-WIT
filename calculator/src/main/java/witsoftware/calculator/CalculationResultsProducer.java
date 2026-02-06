package witsoftware.calculator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import witsoftware.common.dtos.CalculationResponse;

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
        kafkaTemplate.send(topicName, response);
    }
}