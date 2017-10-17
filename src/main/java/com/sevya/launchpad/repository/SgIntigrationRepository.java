package com.sevya.launchpad.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sevya.launchpad.model.SgIntegration;

@Repository
public interface SgIntigrationRepository extends CrudRepository<SgIntegration, Long> {

}
