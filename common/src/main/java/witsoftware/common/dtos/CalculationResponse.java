package witsoftware.common.dtos;

import java.math.BigDecimal;

public record CalculationResponse(
        String requestId,
        BigDecimal result,
        String errorMessage
) {
}
