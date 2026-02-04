package witsoftware.rest;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import witsoftware.common.OperationEnum;
import witsoftware.common.dtos.CalculationRequest;

import java.math.BigDecimal;

// service or component?
@Service
public class CalculationRequestsProducer {
    private final String topicName;
    private KafkaTemplate<String, CalculationRequest> kafkaTemplate;

    public CalculationRequestsProducer() {
        this.topicName = "${KAFKA_CALCULATOR_TOPIC}";
    }

    // TODO: add error handling, logging, etc.

    public void sendMessage(BigDecimal a, BigDecimal b, OperationEnum op) {
        var request = new CalculationRequest(a, b, op);
        kafkaTemplate.send(topicName, request).
                thenRun(() -> System.out.println("Message sent to ${topicName}: " + request));
        // should probably be async and must return some kind of response to the controller
    }

    // TODO: create a central service, separate kafka producer and consumer
}
