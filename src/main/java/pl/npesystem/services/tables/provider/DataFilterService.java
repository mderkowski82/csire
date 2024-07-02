package pl.npesystem.services.tables.provider;

import jakarta.persistence.criteria.Predicate;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.npesystem.data.AbstractEntity;
import pl.npesystem.data.repositories.GenericRepository;
import pl.npesystem.data.repositories.TestEntityRepository;
import pl.npesystem.models.dto.FilterRequestDTO;
import pl.npesystem.models.records.FieldPropInfo;
import pl.npesystem.services.jpa.PredicateBuilder;
import pl.npesystem.services.utils.ReflectionUtils;

import java.util.Collection;
import java.util.List;

@Service
public class DataFilterService {

    private final ApplicationContext applicationContext;

    public DataFilterService(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @SuppressWarnings("unchecked")
    public <T extends AbstractEntity> List<T> getFilteredEntity(FilterRequestDTO filter) throws ClassNotFoundException {
        Class<?> aClass = Class.forName("pl.npesystem.data.entities." + filter.getEntityName());
        return getFilteredEntity(filter, (Class<T>) aClass);
    }

    @SneakyThrows
    protected <T extends AbstractEntity> List<T> getFilteredEntity(FilterRequestDTO filterRequest, Class<T> type) {
        PredicateBuilder<T> predicateBuilder = new PredicateBuilder<>(Predicate.BooleanOperator.AND);
        for (FilterRequestDTO.FilterCriteriaDTO filter : filterRequest.getFilters()) {
            List<FieldPropInfo> fieldPropInfo = ReflectionUtils.toFieldPropInfo(type);

            validateFilter(filter);
//            fieldPropInfo.stream()
//                    .filter(field -> !field.fieldName().contains(".")) // nested fields
//                    .filter(field -> field.fieldName().equals(filter.getFieldName()))
//                    .findFirst()
//                    .orElseThrow(() -> new IllegalArgumentException("Field " + filter.getFieldName() + " not found"));


            switch (filter.getOperation()) {
                case EQUALS -> predicateBuilder.equal(filter.getFieldName(), filter.getValues().toArray());
                case NOT_EQUALS -> predicateBuilder.nonEqual(filter.getFieldName(), filter.getValues().toArray());
                case GREATER_THAN -> predicateBuilder.greater(filter.getFieldName(), (Comparable<?>) filter.getValues().get(0));
                case LESS_THAN -> predicateBuilder.less(filter.getFieldName(), (Comparable<?>) filter.getValues().get(0));
                case GREATER_THAN_EQUAL_TO -> predicateBuilder.greaterOrEqual(filter.getFieldName(), (Comparable<?>) filter.getValues().get(0));
                case LESS_THAN_EQUAL_TO -> predicateBuilder.lessOrEqual(filter.getFieldName(), (Comparable<?>) filter.getValues().get(0));
                case BETWEEN -> predicateBuilder.between(filter.getFieldName(), (Comparable<?>) filter.getValues().get(0), (Comparable<?>) filter.getValues().get(1));
                case IN -> predicateBuilder.in(filter.getFieldName(), (Collection<?>) filter.getValues());
                case LIKE -> predicateBuilder.like(filter.getFieldName(), filter.getValues().get(0).toString());
                case NOT_LIKE -> predicateBuilder.notLike(filter.getFieldName(), filter.getValues().get(0).toString());
                case NULL -> predicateBuilder.isNull(filter.getFieldName());
                case NOT_NULL -> predicateBuilder.isNotNull(filter.getFieldName());
                case MEMBER -> predicateBuilder.isMember(filter.getFieldName(), filter.getValues());
                default -> throw new IllegalArgumentException("Unsupported operation: " + filter.getOperation());
            }
        }

        Specification<T> specification = predicateBuilder.build();
        GenericRepository<T> genericRepository = (GenericRepository<T>) applicationContext.getBean(TestEntityRepository.class);

        List<T> all = genericRepository.findAll(
                specification,
                PageRequest.of(
                        filterRequest.getPageRequest().getPage(),
                        filterRequest.getPageRequest().getSize(),
                        filterRequest.getPageRequest().getSort().getDirection() == FilterRequestDTO.Direction.ASC ?
                                org.springframework.data.domain.Sort.by(filterRequest.getPageRequest().getSort().getProperty()).ascending() :
                                org.springframework.data.domain.Sort.by(filterRequest.getPageRequest().getSort().getProperty()).descending()
                        )).getContent();

        return all;

    }

    private static void validateFilter(FilterRequestDTO.FilterCriteriaDTO filter) {
        switch (filter.getOperation()) {
            case EQUALS, NOT_EQUALS, GREATER_THAN, LESS_THAN, GREATER_THAN_EQUAL_TO, LESS_THAN_EQUAL_TO, LIKE, NOT_LIKE -> {
                if (filter.getValues().size() != 1) {
                    throw new IllegalArgumentException("Operation " + filter.getOperation() + " requires exactly one value");
                }
            }
            case BETWEEN -> {
                if (filter.getValues().size() != 2) {
                    throw new IllegalArgumentException("Operation " + filter.getOperation() + " requires exactly two values");
                }
            }
            case NULL, NOT_NULL -> {
                if (!filter.getValues().isEmpty()) {
                    throw new IllegalArgumentException("Operation " + filter.getOperation() + " requires no values");
                }
            }
            case IN, MEMBER -> {
                if (filter.getValues().isEmpty()) {
                    throw new IllegalArgumentException("Operation " + filter.getOperation() + " requires at least one value");
                }
            }
        }
    }

}