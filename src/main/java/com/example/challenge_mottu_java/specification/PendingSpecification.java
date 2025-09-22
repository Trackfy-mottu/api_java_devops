package com.example.challenge_mottu_java.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.challenge_mottu_java.model.Pending;
import com.example.challenge_mottu_java.model.PendingFilter;

import jakarta.persistence.criteria.Predicate;

public class PendingSpecification {
    public static Specification<Pending> withFilters(PendingFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.description() != null) {
                predicates.add(
                        cb.like(root.get("description"), filter.description()));
            }

            if (filter.number() != null) {
                predicates.add(cb.equal(root.get("number"), filter.number()));
            }

            if (filter.status() != null) {
                predicates.add(
                        cb.equal(root.get("status"), filter.status()));
            }

            if (filter.placaDaMoto() != null) {
                predicates.add(
                        cb.equal(root.get("bike_id"), filter.placaDaMoto()));
            }

            var arrayPredicates = predicates.toArray(new Predicate[0]);
            return cb.and(arrayPredicates);
        };
    }
}
