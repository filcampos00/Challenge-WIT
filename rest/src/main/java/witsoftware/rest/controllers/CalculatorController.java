package witsoftware.rest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/calculator")
public class CalculatorController {
    private final String resultPrefix = "Result: ";

    @Operation(summary = "Sum Endpoint", description = "Calculates the sum of two numbers.")
    @GetMapping("/sum")
    public ResponseEntity<String> sum(
            @RequestParam @NotNull BigDecimal a,
            @RequestParam @NotNull BigDecimal b) {
        return ResponseEntity.ok(resultPrefix + a.add(b));
    }

    @Operation(summary = "Subtract Endpoint", description = "Calculates the subtraction of two numbers.")
    @GetMapping("/subtract")
    public ResponseEntity<String> subtract(
            @RequestParam @NotNull BigDecimal a,
            @RequestParam @NotNull BigDecimal b) {
        return ResponseEntity.ok(resultPrefix + a.subtract(b));
    }

    @Operation(summary = "Multiply Endpoint", description = "Calculates the multiplication of two numbers.")
    @GetMapping("/multiply")
    public ResponseEntity<String> multiply(
            @RequestParam @NotNull BigDecimal a,
            @RequestParam @NotNull BigDecimal b) {
        return ResponseEntity.ok(resultPrefix + a.multiply(b));
    }

    // does not handle edge cases but doesn't matter, all the logic will be placed in the calculator service later
    @Operation(summary = "Divide Endpoint", description = "Calculates the division of two numbers.")
    @GetMapping("/divide")
    public ResponseEntity<String> divide(
            @RequestParam @NotNull BigDecimal a,
            @RequestParam @NotNull BigDecimal b) {
        return ResponseEntity.ok(resultPrefix + a.divide(b));
    }
}

