package witsoftware.rest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import witsoftware.common.enumerations.OperationEnum;
import witsoftware.rest.services.CalculationService;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/calculator")
public class CalculatorController {
    private final CalculationService calculationService;

    public CalculatorController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @Operation(summary = "Sum Endpoint", description = "Calculates the sum of two numbers.")
    @GetMapping("/sum")
    public CompletableFuture<ResponseEntity<String>> sum(
            @RequestParam @NotNull BigDecimal a,
            @RequestParam @NotNull BigDecimal b) {
        return calculate(a, b, OperationEnum.ADD);
    }

    @Operation(summary = "Subtract Endpoint", description = "Calculates the subtraction of two numbers.")
    @GetMapping("/subtract")
    public CompletableFuture<ResponseEntity<String>> subtract(
            @RequestParam @NotNull BigDecimal a,
            @RequestParam @NotNull BigDecimal b) {
        return calculate(a, b, OperationEnum.SUBTRACT);
    }

    @Operation(summary = "Multiply Endpoint", description = "Calculates the multiplication of two numbers.")
    @GetMapping("/multiply")
    public CompletableFuture<ResponseEntity<String>> multiply(
            @RequestParam @NotNull BigDecimal a,
            @RequestParam @NotNull BigDecimal b) {
        return calculate(a, b, OperationEnum.MULTIPLY);
    }

    @Operation(summary = "Divide Endpoint", description = "Calculates the division of two numbers.")
    @GetMapping("/divide")
    public CompletableFuture<ResponseEntity<String>> divide(
            @RequestParam @NotNull BigDecimal a,
            @RequestParam @NotNull BigDecimal b) {
        return calculate(a, b, OperationEnum.DIVIDE);
    }

    private CompletableFuture<ResponseEntity<String>> calculate(BigDecimal a, BigDecimal b, OperationEnum op) {
        log.info("Received request to calculate: operand 1={}, operation={}, operand 2={}", a, op, b);

        return calculationService.sendRequest(a, b, op)
                .thenApply(response -> {
                    if (response.errorMessage() != null) {
                        log.warn("Error calculating requestId={}, returning http status bad request, error={}",
                                response.requestId(), response.errorMessage());
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Error: " + response.errorMessage());
                    }
                    log.info("Successfully calculated requestId={}, returning http status ok, total={}",
                            response.requestId(), response.total());
                    return ResponseEntity.ok("Result: " + response.total());
                })
                .exceptionally(ex -> {
                    log.error("Error during calculation, returning http status internal server error", ex);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error during calculation: " + ex.getMessage());
                });
    }
}