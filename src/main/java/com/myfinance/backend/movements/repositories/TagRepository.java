package com.myfinance.backend.movements.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.myfinance.backend.movements.entities.AppTag;

public interface TagRepository extends CrudRepository<AppTag, Long> {

    List<AppTag> findByUserId(Long userId);

    List<AppTag> findByIsGlobal(boolean isGlobal);

}
