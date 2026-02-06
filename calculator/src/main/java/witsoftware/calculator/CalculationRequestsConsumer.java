package witsoftware.calculator;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import witsoftware.common.dtos.CalculationRequest;
import witsoftware.common.dtos.CalculationResponse;

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
        CalculationResponse response = calculatorService.calculate(record.value());
        producer.send(response);
    }
}