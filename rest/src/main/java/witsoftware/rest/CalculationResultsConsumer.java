package witsoftware.rest;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import witsoftware.common.dtos.CalculationResponse;

@Component
public class CalculationResultsConsumer {
    private final CalculationService calculationService;

    public CalculationResultsConsumer(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @KafkaListener(topics = "${KAFKA_TOPIC_RESPONSE}")
    public void consume(ConsumerRecord<String, CalculationResponse> record) {
        CalculationResponse response = record.value();
        calculationService.completeRequest(response.requestId(), response);
    }
}