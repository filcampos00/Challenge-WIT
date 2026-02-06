package witsoftware.common.dtos;

import java.math.BigDecimal;

public record CalculationResponse(
        String requestId,
        BigDecimal total,
        String errorMessage
) {
}
