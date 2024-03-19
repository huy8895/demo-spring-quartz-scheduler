package com.example.demospringquartzschedulter.factory;

import com.example.demospringquartzschedulter.utils.SpecSearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.Date;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

@Setter
@Getter
@RequiredArgsConstructor
public class BaseSpecification<T> implements
    Specification<T> {

  private final SpecSearchCriteria criteria;

  @Override
  public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query,
      final CriteriaBuilder builder) {
    query.distinct(true);
    switch (criteria.getOperation()) {
      case EQUALITY:
        return builder.equal(root.get(criteria.getKey()), criteria.getValue());
      case IN:
        return builder.in(root.get(criteria.getKey())).value(criteria.getValue());
      case LESS_THAN_OR_EQUAL:
        if (criteria.getValue() instanceof Date) {
          return builder.lessThanOrEqualTo(root.get(criteria.getKey()),
                  (Date) criteria.getValue());
        } else {
          return builder.lessThanOrEqualTo(root.get(criteria.getKey()),
                  criteria.getValue().toString());
        }
      case GREATER_THAN_OR_EQUAL:
        if (criteria.getValue() instanceof Date) {
          return builder.greaterThanOrEqualTo(root.get(criteria.getKey()),
                  (Date) criteria.getValue());
        } else {
          return builder.greaterThanOrEqualTo(root.get(criteria.getKey()),
                  criteria.getValue().toString());
        }
      case IS_NOT_NULL:
        return builder.isNotNull(root.get(criteria.getKey()));
      case IS_NULL:
        return builder.isNull(root.get(criteria.getKey()));
      default:
        return null;
    }
  }
}
