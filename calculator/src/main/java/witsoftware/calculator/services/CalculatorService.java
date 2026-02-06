package witsoftware.calculator.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import witsoftware.calculator.operations.*;
import witsoftware.common.enumerations.OperationEnum;
import witsoftware.common.dtos.CalculationRequest;
import witsoftware.common.dtos.CalculationResponse;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Service
public class CalculatorService {
    private final Map<OperationEnum, IOperation> operations;

    public CalculatorService() {
        this.operations = Map.of(
                OperationEnum.ADD, new Add(),
                OperationEnum.SUBTRACT, new Subtract(),
                OperationEnum.MULTIPLY, new Multiply(),
                OperationEnum.DIVIDE, new Divide()
        );
    }

    public CalculationResponse calculate(CalculationRequest request) {
        try {
            log.info("Calculating requestId={}, operand 1={}, operation={}, operand 2={}",
                    request.requestId(), request.a(), request.op(), request.b());

            IOperation operation = operations.get(request.op());
            BigDecimal total = operation.calculate(request.a(), request.b());

            log.info("Calculated requestId={}, total={}", request.requestId(), total);
            return new CalculationResponse(request.requestId(), total, null);
        } catch (Exception e) {
            log.warn("Calculation error for requestId={}, operand 1={}, operation={}, operand 2={}, error={}",
                    request.requestId(), request.a(), request.op(), request.b(), e.getMessage());

            return new CalculationResponse(request.requestId(), null, e.getMessage());
        }
    }
}
