package org.example.authspringbootsecurity.repository;

import org.example.authspringbootsecurity.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRespository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    Iterable<Category> findByActiveTrue();
    Iterable<Category> findByDescriptionContainingIgnoreCase(String description);
}
