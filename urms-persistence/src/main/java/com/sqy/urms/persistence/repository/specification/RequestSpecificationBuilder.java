package com.sqy.urms.persistence.repository.specification;

import com.sqy.urms.dto.requestentity.RequestStatus;
import com.sqy.urms.dto.requestentity.SearchRequestDto;
import com.sqy.urms.dto.util.AppUserDto;
import com.sqy.urms.dto.util.OrderType;
import com.sqy.urms.persistence.model.Request;
import com.sqy.urms.persistence.model.User;
import com.sqy.urms.persistence.model.UserRole;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RequestSpecificationBuilder {

    public static Specification<Request> search(SearchRequestDto filter, AppUserDto appUserDto) {
        return (root, query, cb) -> {
            List<Predicate> predicates = getPredicates(filter, appUserDto, root, cb);
            List<Order> order = getOrder(cb, root, filter);
            return query.where(cb.and(predicates.toArray(new Predicate[0])))
                    .orderBy(order)
                    .getRestriction();
        };
    }

    private static List<Predicate> getPredicates(SearchRequestDto filter, AppUserDto appUserDto,
                                                 Root<Request> root, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        Join<Request, User> requestUserJoin = root.join("user");
        if (filter.fromCurrentUser()) {
            predicates.add(cb.equal(requestUserJoin.get("login"), appUserDto.currentUsername()));
            return predicates;
        }

        if (StringUtils.hasText(filter.name())) {
            predicates.add(cb.like(cb.upper(requestUserJoin.get("name")), prepareLikeValue(filter.name())));
        }

        if (appUserDto.roles().contains(UserRole.OPERATOR.getRoleName())) {
            predicates.add(cb.equal(root.get("requestStatus"), RequestStatus.SENT.name()));
        }

        if (appUserDto.roles().contains(UserRole.ADMINISTRATOR.getRoleName())) {
            predicates.add(cb.or(
                    cb.equal(root.get("requestStatus"), RequestStatus.SENT.name()),
                    cb.equal(root.get("requestStatus"), RequestStatus.ACCEPTED.name()),
                    cb.equal(root.get("requestStatus"), RequestStatus.REJECTED.name())
            ));
        }

        return predicates;
    }

    private static List<Order> getOrder(CriteriaBuilder cb, Root<? extends Request> root, SearchRequestDto filter) {
        OrderType orderType = filter.dateOrderType();
        if (orderType == OrderType.ASC) {
            return Collections.singletonList(cb.asc(root.get("createdAt")));
        }
        return Collections.singletonList(cb.desc(root.get("createdAt")));
    }

    private static String prepareLikeValue(String value) {
        return "%" + value.toUpperCase() + "%";
    }
}
