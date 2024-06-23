package pl.npesystem.models.records;

import pl.npesystem.data.Role;

public record FuckedPropInfo(String clazz, Role[] view, Role[] edit, Role[] delete, String title,
                             String[] defaultColumn) {
}
