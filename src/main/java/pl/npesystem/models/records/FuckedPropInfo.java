package pl.npesystem.models.records;

import com.vaadin.hilla.Nonnull;
import pl.npesystem.data.Role;
import pl.npesystem.services.records.ColumnProp;

import java.util.List;

public record FuckedPropInfo(String clazz, Role[] view, Role[] edit, Role[] delete, String title,
                             @Nonnull List<@Nonnull ColumnProp> defaultColumn) {
}
