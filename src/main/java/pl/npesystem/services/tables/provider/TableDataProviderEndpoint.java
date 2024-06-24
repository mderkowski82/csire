package pl.npesystem.services.tables.provider;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.Endpoint;
import com.vaadin.hilla.Nonnull;
import pl.npesystem.data.AbstractEntity;
import pl.npesystem.models.dto.FilterRequestDTO;
import pl.npesystem.services.utils.ReflectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Endpoint
@AnonymousAllowed
public class TableDataProviderEndpoint {
    private final DataFilterService dataFilterService;

    public TableDataProviderEndpoint(DataFilterService dataFilterService) {
        this.dataFilterService = dataFilterService;
    }

    public List<Map<String, Object>> getTableDataFiltered(@Nonnull FilterRequestDTO filter) {
        try {
            List<AbstractEntity> filteredEntity = dataFilterService.getFilteredEntity(filter);
            return filteredEntity.stream().map(ReflectionUtils::mapEntityToRecord).collect(Collectors.toList()).reversed();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
