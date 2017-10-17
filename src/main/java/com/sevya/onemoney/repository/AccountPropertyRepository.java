package com.sevya.onemoney.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sevya.onemoney.model.AccountProperty;

@Repository
public interface AccountPropertyRepository extends CrudRepository<AccountProperty, Long> {

	
}
