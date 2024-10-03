package com.example.bankingapp.repository;

import com.example.bankingapp.entity.OrganisationName;
import com.example.bankingapp.entity.Role;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.service.JwtService;
import com.example.bankingapp.validation.TokenValidation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findUsersByParams(OrganisationName organisationName, Date startDate, Date endDate, String firstName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = query.from(User.class);
        List<Predicate> predicates = new ArrayList<>();
        if (organisationName != null) {
            predicates.add(criteriaBuilder.equal(userRoot.get("organisationName"), organisationName));
        }
        if (startDate != null && endDate != null) {
            predicates.add(criteriaBuilder.between(userRoot.get("creationTimeStamp"), startDate, endDate));
        } else if (startDate != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(userRoot.get("creationTimeStamp"), startDate));
        } else if (endDate != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(userRoot.get("creationTimeStamp"), endDate));
        }
        if (firstName != null && !firstName.isEmpty()) {
            predicates.add(criteriaBuilder.equal(userRoot.get("firstName"), firstName));
        }
        query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }
}
