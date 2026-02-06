package witsoftware.rest.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import witsoftware.common.dtos.CalculationRequest;
import witsoftware.common.dtos.CalculationResponse;
import witsoftware.common.enumerations.OperationEnum;
import witsoftware.rest.services.kafka.CalculationRequestsProducer;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculationServiceTest {
    @Mock
    private CalculationRequestsProducer producer;
    @InjectMocks
    private CalculationService calculationService;


    @DisplayName("Test sendRequest and validate the content sent to the producer")
    @Test
    void sendRequest_ValidatesRequestContentSentToProducer() {
        // Arrange
        BigDecimal a = new BigDecimal("1");
        BigDecimal b = new BigDecimal("2");
        OperationEnum op = OperationEnum.ADD;

        // Mock the producer to return a completed future when send is called
        when(producer.send(any(CalculationRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        // Act
        var future = calculationService.sendRequest(a, b, op);

        // Assert
        assertNotNull(future);
        assertFalse(future.isDone());

        // Capture the CalculationRequest sent to the producer
        ArgumentCaptor<CalculationRequest> captor = ArgumentCaptor.forClass(CalculationRequest.class);
        // Verify that producer.send was called once with the captured argument
        verify(producer, times(1)).send(captor.capture());

        // Assert that the captured CalculationRequest has the expected values
        CalculationRequest sent = captor.getValue();
        assertEquals(a, sent.a());
        assertEquals(b, sent.b());
        assertEquals(op, sent.op());
        assertNotNull(sent.requestId());
    }

    @DisplayName("Test sendRequest when producer fails and ensure the future completes exceptionally")
    @Test
    void sendRequest_ProducerFailureCompletesFutureExceptionally() {
        // Arrange
        BigDecimal a = new BigDecimal("1");
        BigDecimal b = new BigDecimal("2");
        OperationEnum op = OperationEnum.ADD;

        // Mock the producer to return a future that completes exceptionally
        when(producer.send(any(CalculationRequest.class)))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("Producer error")));

        // Act
        var future = calculationService.sendRequest(a, b, op);

        // Assert
        assertNotNull(future);
        assertTrue(future.isCompletedExceptionally());

        // Verify that producer.send was called once
        verify(producer, times(1)).send(any(CalculationRequest.class));
    }


    @DisplayName("completeRequest completes the pending future")
    @Test
    void completeRequest_CompletesPendingFuture() {
        // Arrange
        BigDecimal a = new BigDecimal("1");
        BigDecimal b = new BigDecimal("2");
        OperationEnum op = OperationEnum.ADD;

        // Mock the producer to return a completed future from kafka send
        when(producer.send(any(CalculationRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(null));
        var future = calculationService.sendRequest(a, b, op);

        // Capture the CalculationRequest sent to the producer to get the requestId
        ArgumentCaptor<CalculationRequest> captor = ArgumentCaptor.forClass(CalculationRequest.class);
        verify(producer).send(captor.capture());
        String requestId = captor.getValue().requestId();

        CalculationResponse response = new CalculationResponse(
                requestId, new BigDecimal("3"), null);

        // Act
        boolean completed = calculationService.completeRequest(response);

        // Assert
        assertTrue(completed);
        assertTrue(future.isDone());
        // The future completed should return the same response we passed to completeRequest
        var receivedResponse = future.join();
        assertEquals(response, receivedResponse);
    }

    @DisplayName("completeRequest with unknown requestId does not throw")
    @Test
    void completeRequest_NoPending_DoesNotThrow() {
        // Arrange
        String requestId = "requestId";
        CalculationResponse response = new CalculationResponse(requestId, BigDecimal.ONE, null);

        // Act
        boolean completed = calculationService.completeRequest(response);

        // Assert
        assertFalse(completed);
    }
}