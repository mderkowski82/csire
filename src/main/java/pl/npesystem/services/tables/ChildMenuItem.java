package pl.npesystem.services.tables;

import com.vaadin.hilla.Nonnull;
import pl.npesystem.services.records.FuckedPropInfo;

public record ChildMenuItem(@Nonnull long position, @Nonnull String label, @Nonnull FuckedPropInfo entity) {}