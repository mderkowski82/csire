package pl.npesystem.services.tables;

import pl.npesystem.services.records.FuckedPropInfo;

public record ChildMenuItem(long position, String label, FuckedPropInfo entity) {}