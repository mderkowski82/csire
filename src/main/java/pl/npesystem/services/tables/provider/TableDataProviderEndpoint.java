package pl.npesystem.services.tables.provider;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.Endpoint;
import com.vaadin.hilla.Nonnull;
import org.springframework.stereotype.Service;
import pl.npesystem.data.entities.User;
import pl.npesystem.models.dto.FilterRequestDTO;

@Endpoint
@AnonymousAllowed
public class TableDataProviderEndpoint {
    private final DataFilterService dataFilterService;

    public TableDataProviderEndpoint(DataFilterService dataFilterService) {
        this.dataFilterService = dataFilterService;
    }

    public Object getTableDataFiltered(@Nonnull FilterRequestDTO filter) {
        return dataFilterService.getFilteredData(filter, User.class);
    }
}
