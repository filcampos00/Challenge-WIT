//package witsoftware.rest;
//
//
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//import witsoftware.common.dtos.CalculationResponse;
//
//@Component
//public class CalculationResultsConsumer {
//
//    public CalculationResultsConsumer(CalculationService calculationService) {
//        this.calculationService = calculationService;
//    }
//
//    @KafkaListener(topics = "${KAFKA_TOPIC_RESPONSE}")
//    public void consume(CalculationResponse response) {
//
//    }
//}
