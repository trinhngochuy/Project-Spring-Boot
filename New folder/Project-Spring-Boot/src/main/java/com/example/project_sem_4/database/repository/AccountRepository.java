package com.example.project_sem_4.database.repository;

import com.example.project_sem_4.database.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByEmail(String email);

    List<Account> findAccountsByEmail(String email);

    @Query(value = "Select" +
            " accounts.*"+
            " From accounts join accounts_roles on accounts.id = accounts_roles.account_id"+
            " WHERE accounts_roles.role_id = ?1",nativeQuery = true)
    List<Account> findAccountsByRole_id(Integer role_id);
}

