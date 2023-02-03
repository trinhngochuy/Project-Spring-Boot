package com.example.project_sem_4.database.repository;

import com.example.project_sem_4.database.entities.MembershipClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipClassRepository extends JpaRepository<MembershipClass, Integer> {
}
