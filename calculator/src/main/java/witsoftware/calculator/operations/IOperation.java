package witsoftware.calculator.operations;

import java.math.BigDecimal;

public interface IOperation {
    BigDecimal calculate(BigDecimal a, BigDecimal b);
}
