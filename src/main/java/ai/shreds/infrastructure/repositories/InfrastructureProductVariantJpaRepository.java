package ai.shreds.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InfrastructureProductVariantJpaRepository extends JpaRepository<InfrastructureProductVariantJpaEntity, UUID> {

    /**
     * Find all variants for a product
     */
    List<InfrastructureProductVariantJpaEntity> findByProductId(UUID productId);
    
    /**
     * Find all active variants for a product
     */
    List<InfrastructureProductVariantJpaEntity> findByProductIdAndIsActiveTrue(UUID productId);
    
    /**
     * Find all variants for a product ordered by type and display order
     */
    List<InfrastructureProductVariantJpaEntity> findByProductIdOrderByVariantTypeAscDisplayOrderAsc(UUID productId);
    
    /**
     * Find variants for a product of a specific type
     */
    List<InfrastructureProductVariantJpaEntity> findByProductIdAndVariantTypeOrderByDisplayOrderAsc(UUID productId, String variantType);
    
    /**
     * Check if a variant type+value combination already exists for a product
     */
    boolean existsByProductIdAndVariantTypeAndVariantValue(UUID productId, String variantType, String variantValue);
    
    /**
     * Delete all variants for a product
     */
    void deleteByProductId(UUID productId);
    
    /**
     * Get distinct variant types for a product
     */
    @Query("SELECT DISTINCT v.variantType FROM InfrastructureProductVariantJpaEntity v WHERE v.productId = :productId")
    List<String> findDistinctVariantTypesByProductId(@Param("productId") UUID productId);
    
    /**
     * Count variants by product ID and variant type
     */
    long countByProductIdAndVariantType(UUID productId, String variantType);
    
    /**
     * Count all variants for a product
     */
    long countByProductId(UUID productId);
    
    /**
     * Get the maximum display order for a variant type in a product
     */
    @Query("SELECT MAX(v.displayOrder) FROM InfrastructureProductVariantJpaEntity v WHERE v.productId = :productId AND v.variantType = :variantType")
    Integer findMaxDisplayOrderByProductIdAndVariantType(
        @Param("productId") UUID productId, 
        @Param("variantType") String variantType
    );
}