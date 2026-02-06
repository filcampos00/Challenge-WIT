package witsoftware.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import witsoftware.common.OperationEnum;
import witsoftware.common.dtos.CalculationRequest;
import witsoftware.common.dtos.CalculationResponse;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class CalculationService {
    private final ConcurrentHashMap<String, CompletableFuture<CalculationResponse>> pendingCalculations = new ConcurrentHashMap<>();
    private final CalculationRequestsProducer producer;

    public CalculationService(CalculationRequestsProducer producer) {
        this.producer = producer;
    }

    public CompletableFuture<CalculationResponse> sendRequest(BigDecimal a, BigDecimal b, OperationEnum op) {
        String requestId = UUID.randomUUID().toString();
        CalculationRequest request = new CalculationRequest(requestId, a, b, op);

        log.debug("Calculating requestId={}, operand 1={}, operation={}, operand 2={}", requestId, a, op, b);

        CompletableFuture<CalculationResponse> future = new CompletableFuture<>();
        future.orTimeout(20, TimeUnit.SECONDS);
        pendingCalculations.put(requestId, future);

        producer.send(request)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        pendingCalculations.remove(requestId);
                        future.completeExceptionally(ex);
                    }
                });
        return future;
    }

    public void completeRequest(String requestId, CalculationResponse response) {
        CompletableFuture<CalculationResponse> future = pendingCalculations.remove(requestId);
        if (future != null) {
            log.debug("Completing requestId={}, total={}, error={}",
                    requestId, response.total(), response.errorMessage());

            future.complete(response);
        }
        else  {
            log.warn("No pending calculation found for requestId={}", requestId);
        }
    }
}
