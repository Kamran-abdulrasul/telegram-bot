package com.example.telegram_bot.repository;

import com.example.telegram_bot.entity.PaymentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentsRepository extends JpaRepository<PaymentsEntity,Long> {

    PaymentsEntity findByMessageId (Long messageId);


}
