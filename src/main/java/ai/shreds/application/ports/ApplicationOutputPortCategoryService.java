package ai.shreds.application.ports;

import ai.shreds.domain.entities.DomainCategoryEntity;
import ai.shreds.domain.value_objects.CreateCategoryCommand;
import java.util.List;

public interface ApplicationOutputPortCategoryService {
    DomainCategoryEntity createCategory(CreateCategoryCommand command);
    List<DomainCategoryEntity> getCategoryTree();
}
