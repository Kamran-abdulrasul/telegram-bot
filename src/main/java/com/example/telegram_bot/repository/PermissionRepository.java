package com.example.telegram_bot.repository;

import com.example.telegram_bot.entity.PermissionIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionIdEntity,Long> {

}
