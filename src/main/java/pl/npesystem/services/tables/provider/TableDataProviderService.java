package pl.npesystem.services.tables.provider;

import com.vaadin.hilla.Nonnull;
import org.springframework.stereotype.Service;
import pl.npesystem.models.dto.FilterRequestDTO;

@Service
public class TableDataProviderService {
    public Object getTableDataFiltered(@Nonnull FilterRequestDTO filter) {
        return null;
    }
}
