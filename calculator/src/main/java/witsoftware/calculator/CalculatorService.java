package witsoftware.calculator;

import org.springframework.stereotype.Service;
import witsoftware.calculator.operations.*;
import witsoftware.common.OperationEnum;
import witsoftware.common.dtos.CalculationRequest;
import witsoftware.common.dtos.CalculationResponse;

import java.math.BigDecimal;
import java.util.Map;

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
            IOperation operation = operations.get(request.op());
            BigDecimal total = operation.calculate(request.a(), request.b());
            return new CalculationResponse(request.requestId(), total, null);
        } catch (Exception e) {
            return new CalculationResponse(request.requestId(), null, e.getMessage());
        }
    }
}
