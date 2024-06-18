package pl.npesystem.services.tables;

import java.util.List;

public record ParentMenuItem(long position, String label, List<ChildMenuItem> childs){}