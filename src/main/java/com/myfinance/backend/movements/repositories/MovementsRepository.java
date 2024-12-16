package com.myfinance.backend.movements.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.myfinance.backend.movements.entities.ViewAppMovements;

public interface MovementsRepository extends CrudRepository<ViewAppMovements, Long> {

    List<ViewAppMovements> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE ViewAppMovements v SET v.tag = NULL WHERE v.tag.id = :tagId")
    void unlinkTagFromMovements(@Param("tagId") Long tagId);
}
