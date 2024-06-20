package pl.npesystem.models.dto;

import com.vaadin.hilla.Nonnull;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class FilterRequestDTO {

    @Nonnull private String entityName;
    @Nonnull private List<@Nonnull FilterCriteriaDTO> filters;
    @Nonnull private PageRequest pageRequest; // stronicowanie i sortowanie

    // Getters and Setters

    @Data
    public static class FilterCriteriaDTO {
        @Nonnull private String fieldName;
        @Nonnull private Operation operation;
        @Nonnull private List<@Nonnull Object> values;
        @Nonnull private LogicOperator logicOperator;  // operator logiczny
        @Nonnull private String wildcard; // New addition
        private DateRange dateRange;  // New addition

        // Getters and Setters
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

    public enum LogicOperator {
        AND,
        OR
    }

    @Data
    public static class PageRequest {
        @Nonnull private Integer page;
        @Nonnull private Integer size;
        @Nonnull private Sort sort;

        // Getters and Setters
    }

    @Data
    public static class Sort {
        @Nonnull private Direction direction;
        @Nonnull private String property;

        // Getters and Setters
    }

    public enum Direction {
        ASC,
        DESC
    }
}