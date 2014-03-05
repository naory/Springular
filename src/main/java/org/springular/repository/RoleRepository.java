package org.springular.repository;

import org.springular.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**

 * Role JPA Repository
 */
public interface RoleRepository extends JpaRepository<Role, Long>, QueryDslPredicateExecutor<Role> {
}
