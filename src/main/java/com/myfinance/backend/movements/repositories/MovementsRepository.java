package com.myfinance.backend.movements.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.myfinance.backend.movements.entities.AppMovements;

public interface MovementsRepository extends CrudRepository<AppMovements, Long> {
    List<AppMovements> findByUserId(Long userId);
}
