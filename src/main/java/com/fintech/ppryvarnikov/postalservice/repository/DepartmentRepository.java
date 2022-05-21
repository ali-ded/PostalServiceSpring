package com.fintech.ppryvarnikov.postalservice.repository;

import com.fintech.ppryvarnikov.postalservice.model.Department;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, Integer> {
    @Modifying
    @Query(value = "update departments set " +
            "description = :description " +
            "where id = :departmentId", nativeQuery = true)
    int update(@Param("departmentId") Integer departmentId,
               @Param("description") String description);
}