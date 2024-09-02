package Volt.example.Volt.ContentManagement.Domain.Repositories;

import Volt.example.Volt.ContentManagement.Domain.Entities.Channel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChannelRepository<S extends  Channel> extends JpaRepository<Channel, Integer> {


    @EntityGraph(attributePaths = {"user", "categories"})
    Optional<Channel> findChannelByUser_Email(@Param("email")String email);

    boolean existsChannelByUser_Id(UUID id);

    @EntityGraph(attributePaths = {"user", "categories"})
    Optional<Channel> findChannelByUser_Id(UUID id);

    boolean existsByNameIgnoreCase(String name);

    Optional<Channel> findByNameIgnoreCase(String name);

    @EntityGraph(attributePaths = "categories")
    @Query("SELECT c FROM Channel c " +
            "INNER JOIN c.categories cat " +
            "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :channelName, '%')) " +
            "AND cat.id = :categoryId")
    Page<Channel> findAllByCategoryIdAndNameContainingIgnoreCase(
            @Param("categoryId") int categoryId,
            @Param("channelName") String channelName, Pageable pageable);

    @EntityGraph(attributePaths = "categories")
    @Query("SELECT c FROM Channel c " +
            "INNER JOIN c.categories cat " +
            "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :channelName, '%')) " +
            "AND cat.id = :categoryId")
    List<Channel> findAllByCategoryIdAndNameContainingIgnoreCaseList(
            @Param("categoryId") int categoryId,
            @Param("channelName") String channelName);
}
