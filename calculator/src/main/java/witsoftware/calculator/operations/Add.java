package witsoftware.calculator.operations;

import java.math.BigDecimal;

public class Add implements IOperation {
    @Override
    public BigDecimal calculate(BigDecimal a, BigDecimal b) {
        return a.add(b);
    }
}
