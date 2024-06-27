package pl.npesystem.services.jpa;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import pl.npesystem.services.jpa.specification.*;
import static jakarta.persistence.criteria.Predicate.BooleanOperator.OR;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class PredicateBuilder<T> {

    private final Predicate.BooleanOperator operator;

    private List<Specification<T>> specifications;

    public PredicateBuilder(Predicate.BooleanOperator operator) {
        this.operator = operator;
        this.specifications = new ArrayList<>();
    }

    public PredicateBuilder<T> equal(String property, Object... values) {
        return equal(true, property, values);
    }

    public PredicateBuilder<T> equal(boolean condition, String property, Object... values) {
        return this.predicate(condition, new EqualSpecification<T>(property, values));
    }

    public PredicateBuilder<T> nonEqual(String property, Object... values) {
        return nonEqual(true, property, values);
    }

    public PredicateBuilder<T> nonEqual(boolean condition, String property, Object... values) {
        return this.predicate(condition, new NotEqualSpecification<T>(property, values));
    }

    public PredicateBuilder<T> greater(String property, Comparable<?> compare) {
        return greater(true, property, compare);
    }

    public PredicateBuilder<T> greater(boolean condition, String property, Comparable<?> compare) {
        return this.predicate(condition, new GtSpecification<T>(property, compare));
    }

    public PredicateBuilder<T> greaterOrEqual(String property, Comparable<?> compare) {
        return greaterOrEqual(true, property, compare);
    }

    public PredicateBuilder<T> greaterOrEqual(boolean condition, String property, Comparable<? extends Object> compare) {
        return this.predicate(condition, new GeSpecification<T>(property, compare));
    }

    public PredicateBuilder<T> less(String property, Comparable<?> number) {
        return less(true, property, number);
    }

    public PredicateBuilder<T> less(boolean condition, String property, Comparable<?> compare) {
        return this.predicate(condition, new LtSpecification<T>(property, compare));
    }

    public PredicateBuilder<T> lessOrEqual(String property, Comparable<?> compare) {
        return lessOrEqual(true, property, compare);
    }

    public PredicateBuilder<T> lessOrEqual(boolean condition, String property, Comparable<?> compare) {
        return this.predicate(condition, new LeSpecification<T>(property, compare));
    }

    public PredicateBuilder<T> between(String property, Object lower, Object upper) {
        return between(true, property, lower, upper);
    }

    public PredicateBuilder<T> between(boolean condition, String property, Object lower, Object upper) {
        return this.predicate(condition, new BetweenSpecification<T>(property, lower, upper));
    }

    public PredicateBuilder<T> like(String property, String... patterns) {
        return like(true, property, patterns);
    }

    public PredicateBuilder<T> like(boolean condition, String property, String... patterns) {
        return this.predicate(condition, new LikeSpecification<T>(property, patterns));
    }

    public PredicateBuilder<T> notLike(String property, String... patterns) {
        return notLike(true, property, patterns);
    }

    public PredicateBuilder<T> notLike(boolean condition, String property, String... patterns) {
        return this.predicate(condition, new NotLikeSpecification<T>(property, patterns));
    }

    public PredicateBuilder<T> in(String property, Collection<?> values) {
        return this.in(true, property, values);
    }

    public PredicateBuilder<T> in(boolean condition, String property, Collection<?> values) {
        return this.predicate(condition, new InSpecification<T>(property, values));
    }

    public PredicateBuilder<T> notIn(String property, Collection<?> values) {
        return this.notIn(true, property, values);
    }

    public PredicateBuilder<T> notIn(boolean condition, String property, Collection<?> values) {
        return this.predicate(condition, new NotInSpecification<T>(property, values));
    }

    public PredicateBuilder<T> isMember(String property, Collection<?> values) {
        return this.isMember(true, property, values);
    }

    public PredicateBuilder<T> isMember(boolean condition, String property, Collection<?> values) {
        return this.predicate(condition, new IsMember<T>(property, values));
    }

    public PredicateBuilder<T> isNull(String property) {
        return this.predicate(new NullSpecification<T>(property));
    }

    public PredicateBuilder<T> isNotNull(String property) {
        return this.predicate(new NotNullSpecification<T>(property));
    }

    public PredicateBuilder<T> predicate(Specification specification) {
        return predicate(true, specification);
    }


    public PredicateBuilder<T> predicate(boolean condition, Specification specification) {
        if (condition) {
            this.specifications.add(specification);
        }
        return this;
    }

    public Specification<T> build() {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate[] predicates = new Predicate[specifications.size()];
            for (int i = 0; i < specifications.size(); i++) {
                predicates[i] = specifications.get(i).toPredicate(root, query, cb);
            }
            if (Objects.equals(predicates.length, 0)) {
                return null;
            }
            return OR.equals(operator) ? cb.or(predicates) : cb.and(predicates);
        };
    }
}
