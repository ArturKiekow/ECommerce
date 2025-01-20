package com.projetoartur.ecommerce.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderSummaryDto(Long orderId,
                              BigDecimal totalValue,
                              LocalDateTime orderDate,
                              UUID userId) {
}
