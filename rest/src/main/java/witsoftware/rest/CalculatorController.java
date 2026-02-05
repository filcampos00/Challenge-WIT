package witsoftware.rest;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import witsoftware.common.OperationEnum;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/calculator")
public class CalculatorController {
    private final String resultPrefix = "Result: ";
    private final CalculationService calculationService;

    public CalculatorController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @Operation(summary = "Sum Endpoint", description = "Calculates the sum of two numbers.")
    @GetMapping("/sum")
    public CompletableFuture<ResponseEntity<String>> sum(
            @RequestParam @NotNull BigDecimal a,
            @RequestParam @NotNull BigDecimal b) {

        System.out.println("IM IN THE CONTROLLER, A: " + a + " B: " + b);

        return calculationService.sendRequest(a, b, OperationEnum.ADD)
                .thenApply(response -> {
                    if (response.errorMessage() != null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Error: " + response.errorMessage());
                    }
                    return ResponseEntity.ok(resultPrefix + response.result());
                })
                .exceptionally(ex -> {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error during calculation: " + ex.getMessage());
                });
    }

    @Operation(summary = "Subtract Endpoint", description = "Calculates the subtraction of two numbers.")
    @GetMapping("/subtract")
    public CompletableFuture<ResponseEntity<String>> subtract(
            @RequestParam @NotNull BigDecimal a,
            @RequestParam @NotNull BigDecimal b) {

        return calculationService.sendRequest(a, b, OperationEnum.SUBTRACT)
                .thenApply(response -> {
                    if (response.errorMessage() != null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Error: " + response.errorMessage());
                    }
                    return ResponseEntity.ok(resultPrefix + response.result());
                })
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error during calculation: " + ex.getMessage()));
    }

    @Operation(summary = "Multiply Endpoint", description = "Calculates the multiplication of two numbers.")
    @GetMapping("/multiply")
    public CompletableFuture<ResponseEntity<String>> multiply(
            @RequestParam @NotNull BigDecimal a,
            @RequestParam @NotNull BigDecimal b) {

        return calculationService.sendRequest(a, b, OperationEnum.MULTIPLY)
                .thenApply(response -> {
                    if (response.errorMessage() != null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Error: " + response.errorMessage());
                    }
                    return ResponseEntity.ok(resultPrefix + response.result());
                })
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error during calculation: " + ex.getMessage()));
    }

    @Operation(summary = "Divide Endpoint", description = "Calculates the division of two numbers.")
    @GetMapping("/divide")
    public CompletableFuture<ResponseEntity<String>> divide(
            @RequestParam @NotNull BigDecimal a,
            @RequestParam @NotNull BigDecimal b) {

        return calculationService.sendRequest(a, b, OperationEnum.DIVIDE)
                .thenApply(response -> {
                    if (response.errorMessage() != null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Error: " + response.errorMessage());
                    }
                    return ResponseEntity.ok(resultPrefix + response.result());
                })
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error during calculation: " + ex.getMessage()));
    }
}