package witsoftware.calculator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import witsoftware.calculator.services.CalculatorService;
import witsoftware.common.dtos.CalculationRequest;
import witsoftware.common.dtos.CalculationResponse;
import witsoftware.common.enumerations.OperationEnum;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorServiceTest {
    private final CalculatorService calculationService = new CalculatorService();

    private static Stream<Arguments> validCalculationsProvider() {
        return Stream.of(
                Arguments.of("id-1", new BigDecimal("2"), new BigDecimal("3")),
                Arguments.of("id-1", new BigDecimal("4.5123"), new BigDecimal("-10.06022026")),
                Arguments.of("id-1", new BigDecimal("0"), new BigDecimal("-3.14")),
                Arguments.of("id-1", new BigDecimal("0"), new BigDecimal("0.00"))
        );
    }

    private static void assertResponse(CalculationRequest request, CalculationResponse response, BigDecimal expectedTotal) {
        assertAll(
                () -> assertEquals(request.requestId(), response.requestId()),
                // use compareTo to handle scale differences (e.g., 2.0 vs 2.00)
                () -> assertEquals(0, expectedTotal.compareTo(response.total())),
                () -> assertNull(response.errorMessage())
        );
    }

    @DisplayName("Test addition with valid operands")
    @ParameterizedTest(name = "RequestId={0}, operand 1={1}, operand 2={2}")
    @MethodSource("validCalculationsProvider")
    void calculateAdditionWithValidOperands(String requestId, BigDecimal a, BigDecimal b) {
        // Arrange
        CalculationRequest request = new CalculationRequest(requestId, a, b, OperationEnum.ADD);
        BigDecimal expectedTotal = a.add(b);

        // Act
        CalculationResponse response = calculationService.calculate(request);

        // Assert
        assertResponse(request, response, expectedTotal);
    }

    @DisplayName("Test subtraction with valid operands")
    @ParameterizedTest(name = "RequestId={0}, operand 1={1}, operand 2={2}")
    @MethodSource("validCalculationsProvider")
    void calculateSubtractionWithValidOperands(String requestId, BigDecimal a, BigDecimal b) {
        // Arrange
        CalculationRequest request = new CalculationRequest(requestId, a, b, OperationEnum.SUBTRACT);
        BigDecimal expectedTotal = a.subtract(b);

        // Act
        CalculationResponse response = calculationService.calculate(request);

        // Assert
        assertResponse(request, response, expectedTotal);
    }

    @DisplayName("Test multiplication with valid operands")
    @ParameterizedTest(name = "RequestId={0}, operand 1={1}, operand 2={2}")
    @MethodSource("validCalculationsProvider")
    void calculateMultiplicationWithValidOperands(String requestId, BigDecimal a, BigDecimal b) {
        // Arrange
        CalculationRequest request = new CalculationRequest(requestId, a, b, OperationEnum.MULTIPLY);
        BigDecimal expectedTotal = a.multiply(b);

        // Act
        CalculationResponse response = calculationService.calculate(request);

        // Assert
        assertResponse(request, response, expectedTotal);
    }

    @DisplayName("Test division with valid operands")
    @ParameterizedTest(name = "RequestId={0}, operand 1={1}, operand 2={2}")
    @CsvSource({
        "id-1, 2, 3",
            "id-1, 4.5123, -10.06022026",
            "id-1, 0, -3.14",
    })
    void calculateDivisionWithValidOperands(String requestId, BigDecimal a, BigDecimal b) {
        // Arrange
        CalculationRequest request = new CalculationRequest(requestId, a, b, OperationEnum.DIVIDE);
        BigDecimal expectedTotal = a.divide(b, MathContext.DECIMAL128);

        // Act
        CalculationResponse response = calculationService.calculate(request);

        // Assert
        assertResponse(request, response, expectedTotal);
    }

    @DisplayName("Test division by zero")
    @ParameterizedTest(name = "RequestId={0}, operand 1={1}, operand 2={2}")
    @CsvSource({
        "id-1, 1, 0",
    })
    void calculateDivisionByZero(String requestId, BigDecimal a, BigDecimal b) {
        // Arrange
        CalculationRequest request = new CalculationRequest(requestId, a, b, OperationEnum.DIVIDE);

        // Act
        CalculationResponse response = calculationService.calculate(request);

        // Assert
        assertAll(
                () -> assertEquals(requestId, response.requestId()),
                () -> assertNull(response.total()),
                () -> assertNotNull(response.errorMessage())
        );
    }
}