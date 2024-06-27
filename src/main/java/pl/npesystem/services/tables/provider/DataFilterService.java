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
import pl.npesystem.services.jpa.PredicateBuilder;

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
            switch (filter.getOperation()) {
                case EQUALS -> predicateBuilder.equal(filter.getFieldName(), filter.getValues().toArray());
                case NOT_EQUALS -> predicateBuilder.nonEqual(filter.getFieldName(), filter.getValues().toArray());
                case GREATER_THAN -> predicateBuilder.greater(filter.getFieldName(), (Comparable<?>) filter.getValues().get(0));
                case LESS_THAN -> predicateBuilder.less(filter.getFieldName(), (Comparable<?>) filter.getValues().get(0));
                case GREATER_THAN_EQUAL_TO -> predicateBuilder.greaterOrEqual(filter.getFieldName(), (Comparable<?>) filter.getValues().get(0));
                case LESS_THAN_EQUAL_TO -> predicateBuilder.lessOrEqual(filter.getFieldName(), (Comparable<?>) filter.getValues().get(0));
                case BETWEEN -> predicateBuilder.between(filter.getFieldName(), (Comparable<?>) filter.getValues().get(0), (Comparable<?>) filter.getValues().get(1));
                case IN -> predicateBuilder.in(filter.getFieldName(), (Collection<?>) filter.getValues().get(0));
                case LIKE -> predicateBuilder.like(filter.getFieldName(), filter.getWildcard());
                case NOT_LIKE -> predicateBuilder.notLike(filter.getFieldName(), filter.getWildcard());
                case NULL -> predicateBuilder.isNull(filter.getFieldName());
                case NOT_NULL -> predicateBuilder.isNotNull(filter.getFieldName());
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

}