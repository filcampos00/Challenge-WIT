package witsoftware.calculator.operations;

import java.math.BigDecimal;

public class Subtract implements IOperation
{
    @Override
    public BigDecimal calculate(BigDecimal a, BigDecimal b) {
        return a.subtract(b);
    }
}
