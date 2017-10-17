package com.sevya.launchpad.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sevya.launchpad.model.LoginAttempts;

@Repository
public interface LoginAttemptsRepository extends CrudRepository<LoginAttempts,Long> {

}
