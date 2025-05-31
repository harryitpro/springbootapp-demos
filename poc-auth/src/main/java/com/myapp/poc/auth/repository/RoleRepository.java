package com.myapp.poc.auth.repository;

import com.myapp.poc.auth.entity.Role;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Profile("custom")
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}