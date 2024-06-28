package pl.npesystem.models.dto;

import com.vaadin.hilla.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class FilterRequestDTO {

    @Nonnull private String entityName;
    @Nonnull private List<@Nonnull FilterCriteriaDTO> filters;
    @Nonnull private PageRequest pageRequest; // stronicowanie i sortowanie

    @Data
    public static class FilterCriteriaDTO {
        @Nonnull private String fieldName;
        @Nonnull private Operation operation;
        @Nonnull private List<@Nonnull Object> values;
    }

    @Data
    public static class DateRange {
        @Nonnull private OffsetDateTime from;
        @Nonnull private OffsetDateTime to;
    }

    public enum Operation {
        EQUALS,
        NOT_EQUALS,
        GREATER_THAN,
        LESS_THAN,
        GREATER_THAN_EQUAL_TO,
        LESS_THAN_EQUAL_TO,
        BETWEEN,
        IN,
        NULL,
        NOT_NULL,
        LIKE,
        NOT_LIKE,
    }

    @Data
    @AllArgsConstructor
    public static class PageRequest {
        @Nonnull private Integer page;
        @Nonnull private Integer size;
        @Nonnull private Sort sort;

    }

    @Data
    @AllArgsConstructor
    public static class Sort {
        @Nonnull private Direction direction;
        @Nonnull private String property;

    }

    public enum Direction {
        ASC,
        DESC
    }
}