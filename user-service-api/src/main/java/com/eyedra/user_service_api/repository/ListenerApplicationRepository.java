package com.eyedra.user_service_api.repository;

import com.eyedra.user_service_api.entity.ListenerApplication;
import com.eyedra.user_service_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ListenerApplicationRepository extends JpaRepository<ListenerApplication, Long> {
    Optional<ListenerApplication> findByUser(User user);
    List<ListenerApplication> findByStatus(ListenerApplication.ApplicationStatus status);
}
