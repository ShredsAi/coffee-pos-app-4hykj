package ai.shreds.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InfrastructureProductJpaRepository extends JpaRepository<InfrastructureProductJpaEntity, UUID>, JpaSpecificationExecutor<InfrastructureProductJpaEntity> {

    boolean existsByNameAndCategoryId(String name, UUID categoryId);

    List<InfrastructureProductJpaEntity> findByCategoryId(UUID categoryId);

    List<InfrastructureProductJpaEntity> findByNameContainingIgnoreCase(String name);
}
