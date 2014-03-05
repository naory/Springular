package org.springular.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springular.model.User;

/**
 * User JPA Repository
 */
public interface UserRepository extends JpaRepository<User, Long>, QueryDslPredicateExecutor<User> {
   User findByUserIdIgnoreCase(String userId);
}


