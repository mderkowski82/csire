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
import java.util.Collection;
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

            Path<T> path = root;
            String[] fields = filter.getFieldName().split("\\.");
            for (String field : fields) {
                path = path.get(field);
            }


            Class<?> javaType = path.getJavaType();
            if (javaType.equals(String.class)) {
                String string = filter.getValues().get(0).toString();
                switch (filter.getOperation()) {
                    case EQUALS -> predicates.add(cb.equal(cb.lower(path.as(String.class)), string.toLowerCase()));
                    case NOT_EQUALS ->
                            predicates.add(cb.notEqual(cb.lower(path.as(String.class)), string.toLowerCase()));
                    case LIKE ->
                            predicates.add(cb.like(cb.lower(path.as(String.class)), "%%%s%%".formatted(filter.getWildcard().toLowerCase())));
                    case NOT_LIKE ->
                            predicates.add(cb.notLike(cb.lower(path.as(String.class)), filter.getWildcard().toLowerCase()));
                    case GREATER_THAN ->
                            throw new IllegalArgumentException("Operation GREATER_THAN is not supported for String type");
                    case LESS_THAN ->
                            throw new IllegalArgumentException("Operation LESS_THAN is not supported for String type");
                    case GREATER_THAN_EQUAL_TO ->
                            throw new IllegalArgumentException("Operation GREATER_THAN_EQUAL_TO is not supported for String type");
                    case LESS_THAN_EQUAL_TO ->
                            throw new IllegalArgumentException("Operation LESS_THAN_EQUAL_TO is not supported for String type");
                    case BETWEEN ->
                            throw new IllegalArgumentException("Operation BETWEEN is not supported for String type");
                }
            } else if (javaType.equals(BigDecimal.class)) {
                BigDecimal filterValue = new BigDecimal(filter.getValues().get(0).toString());
                switch (filter.getOperation()) {
                    case EQUALS -> predicates.add(cb.equal(path, filterValue));
                    case NOT_EQUALS -> predicates.add(cb.notEqual(path, filterValue));
                    case GREATER_THAN -> predicates.add(cb.greaterThan(path.as(BigDecimal.class), filterValue));
                    case LESS_THAN -> predicates.add(cb.lessThan(path.as(BigDecimal.class), filterValue));
                    case GREATER_THAN_EQUAL_TO ->
                            predicates.add(cb.greaterThanOrEqualTo(path.as(BigDecimal.class), filterValue));
                    case LESS_THAN_EQUAL_TO ->
                            predicates.add(cb.lessThanOrEqualTo(path.as(BigDecimal.class), filterValue));
                    case BETWEEN -> {
                        BigDecimal filterValueLower = new BigDecimal(filter.getValues().get(0).toString());
                        BigDecimal filterValueUpper = new BigDecimal(filter.getValues().get(1).toString());
                        predicates.add(cb.between(path.as(BigDecimal.class), filterValueLower, filterValueUpper));
                    }
                    default -> throw new IllegalArgumentException("Unsupported operation: " + filter.getOperation());
                }
            } else if (javaType.equals(Integer.class)) {
                Integer filterValue = (Integer) filter.getValues().get(0);
                switch (filter.getOperation()) {
                    case EQUALS -> predicates.add(cb.equal(path, filterValue));
                    case NOT_EQUALS -> predicates.add(cb.notEqual(path, filterValue));
                    case GREATER_THAN -> predicates.add(cb.greaterThan(path.as(Integer.class), filterValue));
                    case LESS_THAN -> predicates.add(cb.lessThan(path.as(Integer.class), filterValue));
                    case GREATER_THAN_EQUAL_TO ->
                            predicates.add(cb.greaterThanOrEqualTo(path.as(Integer.class), filterValue));
                    case LESS_THAN_EQUAL_TO ->
                            predicates.add(cb.lessThanOrEqualTo(path.as(Integer.class), filterValue));
                    case BETWEEN -> {
                        Integer filterValueLower = (Integer) filter.getValues().get(0);
                        Integer filterValueUpper = (Integer) filter.getValues().get(1);
                        predicates.add(cb.between(path.as(Integer.class), filterValueLower, filterValueUpper));
                    }
                }
            } else if (javaType.equals(Long.class)) {
                Long filterValue = Long.valueOf(filter.getValues().get(0).toString());
                switch (filter.getOperation()) {
                    case EQUALS -> predicates.add(cb.equal(path, filterValue));
                    case NOT_EQUALS -> predicates.add(cb.notEqual(path, filterValue));
                    case GREATER_THAN -> predicates.add(cb.greaterThan(path.as(Long.class), filterValue));
                    case LESS_THAN -> predicates.add(cb.lessThan(path.as(Long.class), filterValue));
                    case GREATER_THAN_EQUAL_TO ->
                            predicates.add(cb.greaterThanOrEqualTo(path.as(Long.class), filterValue));
                    case LESS_THAN_EQUAL_TO -> predicates.add(cb.lessThanOrEqualTo(path.as(Long.class), filterValue));
                    case BETWEEN -> {
                        Long filterValueLower = (Long) filter.getValues().get(0);
                        Long filterValueUpper = (Long) filter.getValues().get(1);
                        predicates.add(cb.between(path.as(Long.class), filterValueLower, filterValueUpper));
                    }
                }
            } else if(AbstractEntity.class.isAssignableFrom(javaType)) {
                Class<? extends AbstractEntity> entityClass = (Class<? extends AbstractEntity>) javaType;



                Long dbId = Long.valueOf(filter.getValues().get(0).toString());

                AbstractEntity abstractEntity = entityManager.find(entityClass, dbId);


                switch (filter.getOperation()) {
                    case EQUALS -> predicates.add(cb.equal(path, abstractEntity));
                    case NOT_EQUALS -> predicates.add(cb.notEqual(path, abstractEntity));
                    case GREATER_THAN ->
                            throw new IllegalArgumentException("Operation GREATER_THAN is not supported for Entity type");
                    case LESS_THAN ->
                            throw new IllegalArgumentException("Operation LESS_THAN is not supported for Entity type");
                    case GREATER_THAN_EQUAL_TO ->
                            throw new IllegalArgumentException("Operation GREATER_THAN_EQUAL_TO is not supported for Entity type");
                    case LESS_THAN_EQUAL_TO ->
                            throw new IllegalArgumentException("Operation LESS_THAN_EQUAL_TO is not supported for Entity type");
                    case BETWEEN ->
                            throw new IllegalArgumentException("Operation BETWEEN is not supported for Entity type");
                    case IN -> {
                        CriteriaBuilder.In<Object> inClause = cb.in(path);
                        for (Object val : filter.getValues()) {
                            inClause.value(val);
                        }
                        predicates.add(inClause);
                    }
                    case NULL -> predicates.add(cb.isNull(path));
                    case NOT_NULL -> predicates.add(cb.isNotNull(path));
                }
            } else if (Collection.class.isAssignableFrom(javaType)) {
                Collection<?> filterValue = (Collection<?>) filter.getValues().get(0);
                CriteriaBuilder.In<Object> inClause = cb.in(path);
                for (Object val : filterValue) {
                    inClause.value(val);
                }
                predicates.add(inClause);
            } else if (javaType.isEnum()) {
                Enum<?> filterValue = (Enum<?>) filter.getValues().get(0);
                switch (filter.getOperation()) {
                    case EQUALS -> predicates.add(cb.equal(path, filterValue));
                    case NOT_EQUALS -> predicates.add(cb.notEqual(path, filterValue));
                    case GREATER_THAN ->
                            throw new IllegalArgumentException("Operation GREATER_THAN is not supported for Enum type");
                    case LESS_THAN ->
                            throw new IllegalArgumentException("Operation LESS_THAN is not supported for Enum type");
                    case GREATER_THAN_EQUAL_TO ->
                            throw new IllegalArgumentException("Operation GREATER_THAN_EQUAL_TO is not supported for Enum type");
                    case LESS_THAN_EQUAL_TO ->
                            throw new IllegalArgumentException("Operation LESS_THAN_EQUAL_TO is not supported for Enum type");
                    case BETWEEN ->
                            throw new IllegalArgumentException("Operation BETWEEN is not supported for Enum type");
                }
            } else if (javaType.equals(Boolean.class)) {
                Boolean filterValue = (Boolean) filter.getValues().get(0);
                switch (filter.getOperation()) {
                    case EQUALS -> predicates.add(cb.equal(path, filterValue));
                    case NOT_EQUALS -> predicates.add(cb.notEqual(path, filterValue));
                    case GREATER_THAN ->
                            throw new IllegalArgumentException("Operation GREATER_THAN is not supported for Boolean type");
                    case LESS_THAN ->
                            throw new IllegalArgumentException("Operation LESS_THAN is not supported for Boolean type");
                    case GREATER_THAN_EQUAL_TO ->
                            throw new IllegalArgumentException("Operation GREATER_THAN_EQUAL_TO is not supported for Boolean type");
                    case LESS_THAN_EQUAL_TO ->
                            throw new IllegalArgumentException("Operation LESS_THAN_EQUAL_TO is not supported for Boolean type");
                    case BETWEEN ->
                            throw new IllegalArgumentException("Operation BETWEEN is not supported for Boolean type");
                }
            } else {
                throw new IllegalArgumentException("Unsupported type: " + path.getJavaType());
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