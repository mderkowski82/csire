package pl.npesystem.services.tables.provider;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Service;
import pl.npesystem.models.dto.FilterRequestDTO;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataFilterService {

    @PersistenceContext
    private EntityManager entityManager;

    public <T> List<T> getFilteredData(FilterRequestDTO filterRequest, Class<T> type) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(type);

        Root<T> root = cq.from(type);
        List<Predicate> predicates = new ArrayList<>();

        for (FilterRequestDTO.FilterCriteriaDTO filter : filterRequest.getFilters()) {
            Path<Object> path = root.get(filter.getFieldName());
            if (filter.getOperation().equals(FilterRequestDTO.Operation.EQUALS)) {
                predicates.add(cb.equal(cb.lower(path.as(String.class)), filter.getValues().get(0).toString().toLowerCase()));
            } else if (filter.getOperation().equals(FilterRequestDTO.Operation.NOT_EQUALS)) {
                predicates.add(cb.notEqual(cb.lower(path.as(String.class)), filter.getValues().get(0).toString().toLowerCase()));
            } else if (filter.getOperation().equals(FilterRequestDTO.Operation.GREATER_THAN)) {
                predicates.add(cb.greaterThan(path.<Comparable>get(filter.getFieldName()), Comparable.class.cast(filter.getValues().get(0))));
            } else if (filter.getOperation().equals(FilterRequestDTO.Operation.LESS_THAN)) {
                predicates.add(cb.lessThan(path.<Comparable>get(filter.getFieldName()), Comparable.class.cast(filter.getValues().get(0))));
            } else if (filter.getOperation().equals(FilterRequestDTO.Operation.GREATER_THAN_EQUAL_TO)) {
                predicates.add(cb.greaterThanOrEqualTo(path.<Comparable>get(filter.getFieldName()), Comparable.class.cast(filter.getValues().get(0))));
            } else if (filter.getOperation().equals(FilterRequestDTO.Operation.LESS_THAN_EQUAL_TO)) {
                predicates.add(cb.lessThanOrEqualTo(path.<Comparable>get(filter.getFieldName()), Comparable.class.cast(filter.getValues().get(0))));
            } else if (filter.getOperation().equals(FilterRequestDTO.Operation.BETWEEN)) {
                predicates.add(cb.between(path.<Comparable>get(filter.getFieldName()), Comparable.class.cast(filter.getValues().get(0)), Comparable.class.cast(filter.getValues().get(1))));
            } else if (filter.getOperation().equals(FilterRequestDTO.Operation.IN)) {
                CriteriaBuilder.In<Object> inClause = cb.in(path);
                for (Object val : filter.getValues()) {
                    inClause.value(val);
                }
                predicates.add(inClause);
            } else if (filter.getOperation().equals(FilterRequestDTO.Operation.NULL)) {
                predicates.add(cb.isNull(path));
            } else if (filter.getOperation().equals(FilterRequestDTO.Operation.NOT_NULL)) {
                predicates.add(cb.isNotNull(path));
            } else if (filter.getOperation().equals(FilterRequestDTO.Operation.LIKE)) {
                predicates.add(cb.like(cb.lower(path.as(String.class)), "%%%s%%".formatted(filter.getWildcard().toLowerCase())));
            } else if (filter.getOperation().equals(FilterRequestDTO.Operation.NOT_LIKE)) {
                predicates.add(cb.notLike(cb.lower(path.as(String.class)), filter.getWildcard().toLowerCase()));            }
        }

        cq.where(predicates.toArray(new Predicate[0]));

        TypedQuery<T> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}