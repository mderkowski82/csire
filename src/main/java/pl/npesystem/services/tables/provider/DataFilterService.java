package pl.npesystem.services.tables.provider;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Service;
import pl.npesystem.data.AbstractEntity;
import pl.npesystem.models.dto.FilterRequestDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataFilterService {

    @PersistenceContext
    private EntityManager entityManager;


    @SuppressWarnings("unchecked")
    public <T extends AbstractEntity> List<T> getFilteredEntity(FilterRequestDTO filter) throws ClassNotFoundException {
        Class<?> aClass = Class.forName("pl.npesystem.data.entities." + filter.getEntityName());
        return getFilteredEntity(filter, (Class<T>) aClass);
    }

    private <T extends AbstractEntity> List<T> getFilteredEntity(FilterRequestDTO filterRequest, Class<T> type) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(type);

        Root<T> root = cq.from(type);
        List<Predicate> predicates = new ArrayList<>();

        for (FilterRequestDTO.FilterCriteriaDTO filter : filterRequest.getFilters()) {
            Path<Object> path = root.get(filter.getFieldName());
            switch (filter.getOperation()) {
                case EQUALS -> {
                    if (path.getJavaType().equals(BigDecimal.class)) {
                        BigDecimal filterValue = new BigDecimal(filter.getValues().get(0).toString());
                        predicates.add(cb.equal(path, filterValue));
                    } else {
                        predicates.add(cb.equal(cb.lower(path.as(String.class)), filter.getValues().get(0).toString().toLowerCase()));
                    }
                }
                case NOT_EQUALS -> {
                    if (path.getJavaType().equals(BigDecimal.class)) {
                        BigDecimal filterValue = new BigDecimal(filter.getValues().get(0).toString());
                        predicates.add(cb.notEqual(path, filterValue));
                    } else {
                        predicates.add(cb.notEqual(cb.lower(path.as(String.class)), filter.getValues().get(0).toString().toLowerCase()));
                    }
                }
                case GREATER_THAN -> {
                    if (path.getJavaType().equals(BigDecimal.class)) {
                        BigDecimal filterValue = new BigDecimal(filter.getValues().get(0).toString());
                        predicates.add(cb.greaterThan(path.as(BigDecimal.class), filterValue));
                    } else {
                        String string = filter.getValues().get(0).toString();
                        predicates.add(cb.greaterThan(cb.lower(path.as(String.class)), string.toLowerCase()));
                    }
                }
                case LESS_THAN -> {
                    if (path.getJavaType().equals(BigDecimal.class)) {
                        BigDecimal filterValue = new BigDecimal(filter.getValues().get(0).toString());
                        predicates.add(cb.lessThan(path.as(BigDecimal.class), filterValue));
                    } else {
                        String string = filter.getValues().get(0).toString();
                        predicates.add(cb.lessThan(cb.lower(path.as(String.class)), string.toLowerCase()));
                    }
                }
                case GREATER_THAN_EQUAL_TO -> {
                    if (path.getJavaType().equals(BigDecimal.class)) {
                        BigDecimal filterValue = new BigDecimal(filter.getValues().get(0).toString());
                        predicates.add(cb.greaterThanOrEqualTo(path.as(BigDecimal.class), filterValue));
                    } else {
                        String string = filter.getValues().get(0).toString();
                        predicates.add(cb.greaterThanOrEqualTo(cb.lower(path.as(String.class)), string.toLowerCase()));
                    }
                }
                case LESS_THAN_EQUAL_TO -> {
                    if (path.getJavaType().equals(BigDecimal.class)) {
                        BigDecimal filterValue = new BigDecimal(filter.getValues().get(0).toString());
                        predicates.add(cb.lessThanOrEqualTo(path.as(BigDecimal.class), filterValue));
                    } else {
                        String string = filter.getValues().get(0).toString();
                        predicates.add(cb.lessThanOrEqualTo(cb.lower(path.as(String.class)), string.toLowerCase()));
                    }
                }
                case BETWEEN -> {
                    if (path.getJavaType().equals(BigDecimal.class)) {
                        BigDecimal filterValueLower = new BigDecimal(filter.getValues().get(0).toString());
                        BigDecimal filterValueUpper = new BigDecimal(filter.getValues().get(1).toString());
                        predicates.add(cb.between(path.as(BigDecimal.class), filterValueLower, filterValueUpper));
                    } else {
                        String stringLower = filter.getValues().get(0).toString();
                        String stringUpper = filter.getValues().get(1).toString();
                        predicates.add(cb.between(cb.lower(path.as(String.class)), stringLower.toLowerCase(), stringUpper.toLowerCase()));
                    }
                }
                case IN -> {
                    CriteriaBuilder.In<Object> inClause = cb.in(path);
                    for (Object val : filter.getValues()) {
                        inClause.value(val);
                    }
                    predicates.add(inClause);
                }
                case NULL -> predicates.add(cb.isNull(path));
                case NOT_NULL -> predicates.add(cb.isNotNull(path));
                case LIKE ->
                        predicates.add(cb.like(cb.lower(path.as(String.class)), "%%%s%%".formatted(filter.getWildcard().toLowerCase())));
                case NOT_LIKE ->
                        predicates.add(cb.notLike(cb.lower(path.as(String.class)), filter.getWildcard().toLowerCase()));
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(filterRequest.getPageRequest().getSort().getDirection() == FilterRequestDTO.Direction.ASC ?
                cb.asc(root.get(filterRequest.getPageRequest().getSort().getProperty())) :
                cb.desc(root.get(filterRequest.getPageRequest().getSort().getProperty())));


        TypedQuery<T> query = entityManager.createQuery(cq);
        query.setFirstResult(filterRequest.getPageRequest().getPage() * filterRequest.getPageRequest().getSize());
        query.setMaxResults(filterRequest.getPageRequest().getSize());

        return query.getResultList();
    }
}