package com.myfinance.backend.movements.repositories;

import org.springframework.data.repository.CrudRepository;

import com.myfinance.backend.movements.entities.AppTag;

public interface TagRepository extends CrudRepository<AppTag, Long> {

}
