package org.example.authspringbootsecurity.repository;

import org.example.authspringbootsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
