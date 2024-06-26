package pl.npesystem.services;

import pl.npesystem.models.dto.*;
import java.util.List;
import java.util.ArrayList;

public class FilterBuilder {
    private List<FilterRequestDTO.FilterCriteriaDTO> filters;

    private FilterBuilder() {
        this.filters = new ArrayList<>();
    }

    public static FilterBuilder use() {
        return new FilterBuilder();
    }

    public SimpleSQLFilterBuilder path(String path) {
        return new SimpleSQLFilterBuilder(this, path);
    }

    public FilterRequestDTO build(String entityName, FilterRequestDTO.PageRequest pageRequest) {
        FilterRequestDTO filterRequestDTO = new FilterRequestDTO();
        filterRequestDTO.setEntityName(entityName);
        filterRequestDTO.setFilters(this.filters);
        filterRequestDTO.setPageRequest(pageRequest);
        return filterRequestDTO;
    }

    public static class SimpleSQLFilterBuilder {
        private FilterBuilder parentBuilder;
        private String fieldName;

        public SimpleSQLFilterBuilder(FilterBuilder parentBuilder, String fieldName) {
            this.parentBuilder = parentBuilder;
            this.fieldName = fieldName;
        }

        public FilterBuilder equal(Object value) {
            FilterRequestDTO.FilterCriteriaDTO filter = new FilterRequestDTO.FilterCriteriaDTO();
            filter.setFieldName(this.fieldName);
            filter.setValues(List.of(value));
            filter.setOperation(FilterRequestDTO.Operation.EQUALS);
            parentBuilder.filters.add(filter);
            return parentBuilder;
        }

        public FilterBuilder notEqual(Object value) {
            FilterRequestDTO.FilterCriteriaDTO filter = new FilterRequestDTO.FilterCriteriaDTO();
            filter.setFieldName(this.fieldName);
            filter.setValues(List.of(value));
            filter.setOperation(FilterRequestDTO.Operation.NOT_EQUALS);
            parentBuilder.filters.add(filter);
            return parentBuilder;
        }

        public FilterBuilder greaterThan(Object value) {
            FilterRequestDTO.FilterCriteriaDTO filter = new FilterRequestDTO.FilterCriteriaDTO();
            filter.setFieldName(this.fieldName);
            filter.setValues(List.of(value));
            filter.setOperation(FilterRequestDTO.Operation.GREATER_THAN);
            parentBuilder.filters.add(filter);
            return parentBuilder;
        }

        public FilterBuilder lessThan(Object value) {
            FilterRequestDTO.FilterCriteriaDTO filter = new FilterRequestDTO.FilterCriteriaDTO();
            filter.setFieldName(this.fieldName);
            filter.setValues(List.of(value));
            filter.setOperation(FilterRequestDTO.Operation.LESS_THAN);
            parentBuilder.filters.add(filter);
            return parentBuilder;
        }

        // Dodaj tu inne operacje.
    }
}