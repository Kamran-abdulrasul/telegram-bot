package com.example.telegram_bot.service;

import com.example.telegram_bot.GetPaymentDto;
import com.example.telegram_bot.SavePaymentDto;
import com.example.telegram_bot.entity.PaymentsEntity;
import com.example.telegram_bot.repository.PaymentsRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentsRepository paymentsRepository;


    public void savePayment(SavePaymentDto dto, Long fromChatId, Long messageId) {

        var payment = PaymentsEntity.builder()
                .amount(dto.getAmount())
                .currency(dto.getCurrency())
                .messageText(dto.getMessageText())
                .fromChatId(fromChatId)
                .messageId(messageId)
                .dateTime(LocalDateTime.now())
                .build();
        paymentsRepository.save(payment);

    }


    public void confirmPayment(Long messageId) throws RuntimeException {

        var payment = paymentsRepository.findByMessageId(messageId);
        payment.setStatus("OK");
        paymentsRepository.save(payment);


    }

    public List <GetPaymentDto> getPayments() {

        var payments = paymentsRepository.findAll()
                .stream().map(PaymentService::mapEntityToDto)
                .toList();
        //var payment = paymentsRepository.findById(4l).orElseThrow() ;


         return payments;


    }

    public static GetPaymentDto mapEntityToDto(PaymentsEntity entity) {
        return GetPaymentDto.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .messageText(entity.getMessageText())
                .dateTime(entity.getDateTime())
                .build();

    }




}
