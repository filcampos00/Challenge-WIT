package witsoftware.common.dtos;

import witsoftware.common.OperationEnum;

import java.math.BigDecimal;

public record CalculationRequest(
        String requestId,
        BigDecimal a,
        BigDecimal b,
        OperationEnum op
) {
}
