package Volt.example.Volt.ContentManagement.Domain.Repositories;

import Volt.example.Volt.ContentManagement.Domain.Entities.Category;
import Volt.example.Volt.ContentManagement.Domain.Entities.Channel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);
    @EntityGraph(attributePaths = "categories")
    @Query("SELECT c FROM Channel c " +
            "INNER JOIN c.categories cat " +
            "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :channelName, '%')) " +
            "AND cat.id = :categoryId")
    Page<Channel> findAllByCategoryIdAndNameContainingIgnoreCase(
            @Param("categoryId") int categoryId,
            @Param("channelName") String channelName, Pageable pageable);

    Page<Category> findAllByNameContainingIgnoreCase(String categoryName, Pageable pageable);
}
