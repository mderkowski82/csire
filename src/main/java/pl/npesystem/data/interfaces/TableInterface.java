package pl.npesystem.data.interfaces;

import pl.npesystem.services.records.ColumnProp;

import java.util.List;

public interface TableInterface {
    public List<ColumnProp> getDefaultColumn();
}
