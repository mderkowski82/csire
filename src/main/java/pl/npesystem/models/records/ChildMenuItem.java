package pl.npesystem.models.records;

import com.vaadin.hilla.Nonnull;

public record ChildMenuItem(@Nonnull long position, @Nonnull String label, @Nonnull FuckedPropInfo entity) {}