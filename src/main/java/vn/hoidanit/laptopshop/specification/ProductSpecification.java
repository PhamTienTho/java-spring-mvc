package vn.hoidanit.laptopshop.specification;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.Product_;

public class ProductSpecification {
    public static Specification<Product> nameLike(String name) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(root.get(Product_.NAME), "%" + name + "%");
        };
    }

    public static Specification<Product> minPrice(Double minPrice) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.greaterThan(root.get(Product_.PRICE), minPrice);
        };
    }

    public static Specification<Product> maxPrice(Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.lessThan(root.get(Product_.PRICE), maxPrice);
        };
    }

    public static Specification<Product> matchListFactory(List<String> factory) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get(Product_.FACTORY)).value(factory);
    }

    public static Specification<Product> matchListTarget(List<String> target) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get(Product_.TARGET)).value(target);
    }

    public static Specification<Product> matchMultiplePrice(double min, double max) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
            criteriaBuilder.greaterThan(root.get(Product_.PRICE), min),
            criteriaBuilder.lessThan(root.get(Product_.PRICE), max));
    }
}
