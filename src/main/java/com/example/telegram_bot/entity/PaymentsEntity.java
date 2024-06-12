package com.example.telegram_bot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class PaymentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    private String currency;

    private String messageText;

    private Long fromChatId;

    @Column(name = "date")
    private LocalDateTime dateTime;

    private Long messageId;

    private String status;
}
