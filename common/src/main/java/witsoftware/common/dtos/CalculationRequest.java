package witsoftware.common.dtos;

import witsoftware.common.OperationEnum;

import java.math.BigDecimal;
import java.util.UUID;

public record CalculationRequest(
        //UUID requestId,   // not sure if should be here | removed for now
        BigDecimal a,
        BigDecimal b,
        OperationEnum op
) {
}
