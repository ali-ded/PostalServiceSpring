package com.fintech.ppryvarnikov.postalservice.service;

import com.fintech.ppryvarnikov.postalservice.model.Department;

import java.util.List;
import java.util.Optional;

public interface IDepartment {
    Optional<Department> findById(Integer id);
    List<Department> findAll();
    Department save(Department department);
    List<Department> saveAll(List<Department> departments);
    boolean update(Department department);
    boolean deleteById(Integer id);
}
