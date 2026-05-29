package org.example.authspringbootsecurity.repository;

import org.example.authspringbootsecurity.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
