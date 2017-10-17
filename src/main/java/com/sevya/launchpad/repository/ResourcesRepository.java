package com.sevya.launchpad.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sevya.launchpad.model.Resources;

@Repository
public interface ResourcesRepository extends CrudRepository<Resources, Long> {

}
