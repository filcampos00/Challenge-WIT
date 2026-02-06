package witsoftware.rest.services.kafka;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import witsoftware.common.dtos.CalculationResponse;
import witsoftware.rest.services.CalculationService;

@Slf4j
@Component
public class CalculationResultsConsumer {
    private final CalculationService calculationService;

    public CalculationResultsConsumer(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @KafkaListener(topics = "${KAFKA_TOPIC_RESPONSE}")
    public void consume(ConsumerRecord<String, CalculationResponse> record) {
        CalculationResponse response = record.value();

        log.debug("Received response from Kafka: topic={}, requestId={}",
                 record.topic(), response.requestId());

        calculationService.completeRequest(response.requestId(), response);
    }
}