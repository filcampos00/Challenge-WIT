package witsoftware.calculator.operations;

import java.math.BigDecimal;

public class Multiply implements IOperation {
    @Override
    public BigDecimal calculate(BigDecimal a, BigDecimal b) {
        return a.multiply(b);
    }
}
