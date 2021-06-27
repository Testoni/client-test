package com.platformbuilders.clients.specification;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class SpecificationFilter<T> {

    public Specification<T> containsString(String term, String field) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get(field)), likeTerm(term.trim().toLowerCase())));
    }

    private static String likeTerm(String term) {
        return new StringBuilder().append('%').append(term).append('%').toString();
    }

    public Specification<T> greaterThanOrEqualTo(LocalDate filterDate, String field) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(field).as(LocalDate.class), filterDate));
    }

    public Specification<T> lessThanOrEqualTo(LocalDate filterDate, String field) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(field).as(LocalDate.class), filterDate));
    }

}
