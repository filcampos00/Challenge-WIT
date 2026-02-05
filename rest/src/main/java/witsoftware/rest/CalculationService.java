package witsoftware.rest;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import witsoftware.common.OperationEnum;
import witsoftware.common.dtos.CalculationRequest;
import witsoftware.common.dtos.CalculationResponse;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CalculationService {
    private final String requestTopic;
    private final ConcurrentHashMap<String, CompletableFuture<CalculationResponse>> pendingCalculations;
    private final KafkaTemplate<String, CalculationRequest> template;

    public CalculationService(KafkaTemplate<String, CalculationRequest> template,
                              @Value("${KAFKA_TOPIC_REQUEST}") String requestTopic) {
        this.requestTopic = requestTopic;
        this.template = template;
        pendingCalculations = new ConcurrentHashMap<>();
    }

    public CompletableFuture<CalculationResponse> sendRequest(BigDecimal a, BigDecimal b, OperationEnum op) {
        String requestId = java.util.UUID.randomUUID().toString();
        CalculationRequest request = new CalculationRequest(requestId, a, b, op);

        System.out.println("IM IN THE CALCULATION SERVICE, A: " + a + " B: " + b + " OP: " + op);
        System.out.println("TRYING TO SEND MESSAGE" );

        CompletableFuture<CalculationResponse> future = new CompletableFuture<>();
        pendingCalculations.put(requestId, future);

        template.send(requestTopic, requestId, request)
                .whenComplete((sendResult, ex) -> {
                    if (ex != null) {
                        future.completeExceptionally(ex);
                        pendingCalculations.remove(requestId);
                    } else {
                        System.out.println("Message sent successfully: " + sendResult.getProducerRecord().value());
                    }
                });
        return future;
    }

    @KafkaListener(topics = "${KAFKA_TOPIC_RESPONSE}")
    public void listenForResults(ConsumerRecord<String, CalculationResponse> record) {
        CalculationResponse result = record.value();
        String requestId = result.requestId();

        System.out.println("IM IN THE CALCULATION SERVICE, RECEIVED RESULT");

        CompletableFuture<CalculationResponse> future = pendingCalculations.remove(requestId);
        if (future != null) {
            future.complete(result);
        }
    }
}
