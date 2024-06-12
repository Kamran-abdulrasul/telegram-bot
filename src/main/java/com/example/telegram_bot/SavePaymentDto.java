package com.example.telegram_bot;

import lombok.Builder;
import lombok.Data;


import java.math.BigDecimal;

@Data
@Builder
public class SavePaymentDto {

    private BigDecimal amount;

    private String currency;

    private String messageText;

}
