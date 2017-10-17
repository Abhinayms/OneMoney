package com.sevya.launchpad.repository;

import org.springframework.data.repository.CrudRepository;

import com.sevya.launchpad.model.ApiCredentials;

public interface ApiCredentialsRepository extends CrudRepository<ApiCredentials, Long> {

	public ApiCredentials findApiCredentialsByApiId(String apiId);

}
