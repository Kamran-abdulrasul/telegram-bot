package com.example.telegram_bot.entity;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "permissionid")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionIdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long chatId;

    private String name;

}

