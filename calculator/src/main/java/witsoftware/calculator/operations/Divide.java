package witsoftware.calculator.operations;

import java.math.BigDecimal;
import java.math.MathContext;

public class Divide implements IOperation {
    @Override
    public BigDecimal calculate(BigDecimal a, BigDecimal b) {
        if (b.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Division by zero is not allowed.");
        }
        try {
            return a.divide(b);
        }
        catch (ArithmeticException e) {
            // 34 significant digits should be enough for most calculations
            return a.divide(b, MathContext.DECIMAL128);
        }
    }
}
