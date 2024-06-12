package com.example.telegram_bot;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ConfirmPaymentDto {

    private Long id;
    private String status;
}
