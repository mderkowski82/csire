package pl.npesystem.services.tables;

import com.vaadin.hilla.Nonnull;

import java.util.List;

public record ParentMenuItem(long position, String label, List<@Nonnull ChildMenuItem> childs){}