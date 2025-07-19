package ai.shreds.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface InfrastructureSKUJpaRepository extends JpaRepository<InfrastructureSKUJpaEntity, UUID> {

    boolean existsByCode(String code);

    List<InfrastructureSKUJpaEntity> findByProductId(UUID productId);

    void deleteByProductId(UUID productId);

    /**
     * Find SKU by product ID and variant combination using JSONB containment.
     * This checks if the stored variant_combination JSON contains all the key-value pairs
     * from the provided variantCombination parameter.
     */
    @Query("SELECT s FROM InfrastructureSKUJpaEntity s WHERE s.productId = :productId AND s.variantCombination = :variantCombination")
    List<InfrastructureSKUJpaEntity> findByProductIdAndVariantCombination(
            @Param("productId") UUID productId, 
            @Param("variantCombination") Map<String, String> variantCombination
    );

    /**
     * Check if a variant combination already exists for a product using native JSONB query.
     */
    @Query(value = "SELECT COUNT(*) > 0 FROM sku WHERE product_id = :productId AND variant_combination @> CAST(:variantCombination AS jsonb)", nativeQuery = true)
    boolean existsByProductIdAndVariantCombination(
            @Param("productId") UUID productId, 
            @Param("variantCombination") String variantCombination
    );

    /**
     * Find SKUs by status for a specific product.
     */
    List<InfrastructureSKUJpaEntity> findByProductIdAndStatus(UUID productId, String status);

    /**
     * Count SKUs for a product.
     */
    long countByProductId(UUID productId);

    /**
     * Find all SKUs with specific status.
     */
    List<InfrastructureSKUJpaEntity> findByStatus(String status);
}