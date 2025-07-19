package ai.shreds.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InfrastructureCategoryJpaRepository extends JpaRepository<InfrastructureCategoryJpaEntity, UUID> {

    /**
     * Find direct children of a category ordered by display order
     */
    List<InfrastructureCategoryJpaEntity> findByParentCategoryIdOrderByDisplayOrderAsc(UUID parentCategoryId);

    /**
     * Find direct children of a category ordered by display order that are active
     */
    List<InfrastructureCategoryJpaEntity> findByParentCategoryIdAndIsActiveTrueOrderByDisplayOrderAsc(UUID parentCategoryId);

    /**
     * Check if a category name is unique within siblings
     */
    boolean existsByParentCategoryIdAndName(UUID parentCategoryId, String name);
    
    /**
     * Find all root categories (parentCategoryId is null)
     */
    List<InfrastructureCategoryJpaEntity> findByParentCategoryIdIsNullOrderByDisplayOrderAsc();
    
    /**
     * Find all active categories
     */
    List<InfrastructureCategoryJpaEntity> findByIsActiveTrue();
    
    /**
     * Find category by name and parent ID
     */
    Optional<InfrastructureCategoryJpaEntity> findByNameAndParentCategoryId(String name, UUID parentCategoryId);
    
    /**
     * Find all categories by level ordered by path for building tree structures
     */
    List<InfrastructureCategoryJpaEntity> findAllByOrderByLevelAscPathAsc();
    
    /**
     * Find all categories in a subtree (categories whose path starts with the given path)
     */
    @Query("SELECT c FROM InfrastructureCategoryJpaEntity c WHERE c.path LIKE :pathPrefix% ORDER BY c.level ASC, c.displayOrder ASC")
    List<InfrastructureCategoryJpaEntity> findAllInSubtree(@Param("pathPrefix") String pathPrefix);
    
    /**
     * Check if a category has any direct children
     */
    boolean existsByParentCategoryId(UUID categoryId);
    
    /**
     * Check if a category has any active direct children
     */
    boolean existsByParentCategoryIdAndIsActiveTrue(UUID categoryId);
    
    /**
     * Count direct children of a category
     */
    long countByParentCategoryId(UUID categoryId);
    
    /**
     * Find highest display order in a parent category to help with ordering new categories
     */
    @Query("SELECT MAX(c.displayOrder) FROM InfrastructureCategoryJpaEntity c WHERE c.parentCategoryId = :parentId")
    Integer findMaxDisplayOrderByParentCategoryId(@Param("parentId") UUID parentId);
}
