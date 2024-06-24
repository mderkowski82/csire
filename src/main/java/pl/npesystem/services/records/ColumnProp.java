package pl.npesystem.services.records;

import com.vaadin.hilla.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ColumnProp {
    @Nonnull String fieldName;
    @Nonnull String label;
}
