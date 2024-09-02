package Volt.example.Volt.ContentManagement.Domain.Repositories;

import Volt.example.Volt.ContentManagement.Domain.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);
}
