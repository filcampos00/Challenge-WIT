package witsoftware.calculator;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import witsoftware.calculator.operations.*;
import witsoftware.common.OperationEnum;
import witsoftware.common.dtos.CalculationRequest;
import witsoftware.common.dtos.CalculationResponse;

import java.math.BigDecimal;

@Service
public class CalculatorService {
    private final KafkaTemplate<String, CalculationResponse> template;
    private final String responseTopic;

    public CalculatorService(
            KafkaTemplate<String, CalculationResponse> template,
            @Value("${KAFKA_TOPIC_RESPONSE}") String responseTopic) {
        this.template = template;
        this.responseTopic = responseTopic;
    }

    @KafkaListener(topics = "${KAFKA_TOPIC_REQUEST}")
    public void listenForRequests(ConsumerRecord<String, CalculationRequest> record) {
        CalculationRequest request = record.value();
        String requestId = request.requestId();

        System.out.println("IM IN THE CALCULATOR SERVICE, RECEIVED REQUEST");

        CalculationResponse response;
        try {
            IOperation operation = getOperation(request.op());
            BigDecimal result = operation.calculate(request.a(), request.b());
            response = new CalculationResponse(requestId, result, null);
        } catch (Exception e) {
            // Catch division by zero, illegal arguments, etc.
            response = new CalculationResponse(requestId, null, e.getMessage());
        }

        System.out.println("IM IN THE CALCULATOR SERVICE, SENDING RESPONSE");

        template.send(responseTopic, requestId, response)
                .whenComplete((sendResult, ex) -> {
                    if (ex != null) {
                        System.out.println("Error sending message: " + ex.getMessage());
                    } else {
                        System.out.println("Message sent successfully: " + sendResult.getProducerRecord().value());
                    }
                });
    }

    private IOperation getOperation(OperationEnum op){
        return switch (op) {
            case ADD -> new Add();
            case SUBTRACT -> new Subtract();
            case MULTIPLY -> new Multiply();
            case DIVIDE ->  new Divide();
        };
    }
}
