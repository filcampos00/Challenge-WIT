package witsoftware.calculator;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import witsoftware.common.dtos.CalculationRequest;
import witsoftware.common.dtos.CalculationResponse;

@Slf4j
@Component
public class CalculationRequestsConsumer {
    private final CalculatorService calculatorService;
    private final CalculationResultsProducer producer;

    public CalculationRequestsConsumer(CalculatorService calculatorService, CalculationResultsProducer producer) {
        this.calculatorService = calculatorService;
        this.producer = producer;
    }

    @KafkaListener(topics = "${KAFKA_TOPIC_REQUEST}")
    public void consume(ConsumerRecord<String, CalculationRequest> record) {
        CalculationRequest request = record.value();

        log.debug("Received calculation request from Kafka: topic={}, requestId={}",
                 record.topic(), request.requestId());

        CalculationResponse response = calculatorService.calculate(request);
        producer.send(response);
    }
}