package com.example.telegram_bot;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class GetPaymentDto {

    private Long id;

    private BigDecimal amount;

    private String currency;

    private String messageText;

    private LocalDateTime dateTime;

    private String status;

}
