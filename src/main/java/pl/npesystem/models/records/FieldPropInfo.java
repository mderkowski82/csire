package pl.npesystem.models.records;

import pl.npesystem.data.Role;
import pl.npesystem.data.enums.FormTab;
import pl.npesystem.data.enums.RendererType;

public record FieldPropInfo(String fieldName, int position, String label, RendererType renderer, boolean editable, FormTab tab) {
}
