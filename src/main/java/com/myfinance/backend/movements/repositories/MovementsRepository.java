package com.myfinance.backend.movements.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.myfinance.backend.movements.entities.ViewAppMovements;

public interface MovementsRepository extends CrudRepository<ViewAppMovements, Long> {
    List<ViewAppMovements> findByUserId(Long userId);
}
