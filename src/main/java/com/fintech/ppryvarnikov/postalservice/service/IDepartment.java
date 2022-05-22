package com.fintech.ppryvarnikov.postalservice.service;

import com.fintech.ppryvarnikov.postalservice.model.Department;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Optional;

public interface IDepartment {
    Optional<Department> findById(Integer id);
    List<Department> findAll();
    Optional<Department> findByDescription(String description);
    Department save(String description) throws InstanceAlreadyExistsException;
    List<Department> saveAll(List<String> departmentNames) throws InstanceAlreadyExistsException;
    boolean update(Department department);
    boolean deleteById(Integer id);
}
