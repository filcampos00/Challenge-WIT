//package witsoftware.rest;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.stereotype.Component;
//import witsoftware.common.OperationEnum;
//import witsoftware.common.dtos.CalculationRequest;
//
//import java.math.BigDecimal;
//import java.util.concurrent.CompletableFuture;
//
//@Component
//public class CalculationRequestsProducer {
//    private final String topicName;
//    private KafkaTemplate<String, CalculationRequest> kafkaTemplate;
//
//    public CalculationRequestsProducer(
//            KafkaTemplate<String, CalculationRequest> kafkaTemplate,
//            @Value("${KAFKA_TOPIC_REQUEST}") String topicName) {
//        this.kafkaTemplate = kafkaTemplate;
//        this.topicName = topicName;
//    }
//
//    public CompletableFuture<SendResult<String, CalculationRequest>> sendMessage(CalculationRequest request) {
//        CompletableFuture<SendResult<String, CalculationRequest>> future = kafkaTemplate.send(topicName, request);
//
//        future.whenComplete((sendResult, ex) -> {
//            if (ex != null) {
//                System.out.println("Error sending message: " + ex.getMessage());
//            } else {
//                System.out.println("Message sent successfully: " + sendResult.getProducerRecord().value());
//            }
//        });
//        return future;
//    }
//}
