package com.sevya.launchpad.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sevya.launchpad.model.UserNotifications;

@Repository
public interface UserNotificationsRepository extends CrudRepository<UserNotifications, Long> {

}
