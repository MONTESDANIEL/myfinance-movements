package com.myfinance.backend.movements.entities;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AppUser {

    private Long id;
    private String idType;
    private String name;
    private String email;
    private Long phoneNumber;
    private LocalDate birthDate;
    private String password;
}
