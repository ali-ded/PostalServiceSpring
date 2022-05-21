package com.fintech.ppryvarnikov.postalservice.service;

import com.fintech.ppryvarnikov.postalservice.model.Department;
import com.fintech.ppryvarnikov.postalservice.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DepartmentService implements IDepartment {
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Optional<Department> findById(Integer id) {
        return departmentRepository.findById(id);
    }

    @Override
    public List<Department> findAll() {
        return (List<Department>) departmentRepository.findAll();
    }

    @Override
    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public List<Department> saveAll(List<Department> departments) {
        return (List<Department>) departmentRepository.saveAll(departments);
    }

    @Override
    public boolean update(Department department) {
        boolean operationResult = false;
        int updatedRows = 0;

        updatedRows = departmentRepository.update(department.getId(), department.getDescription());

        if (updatedRows == 1) {
            operationResult = true;
        }

        return operationResult;
    }

    @Override
    public boolean deleteById(Integer id) {
        boolean operationResult = false;

        if (departmentRepository.findById(id).isPresent()) {
            departmentRepository.deleteById(id);
            operationResult = true;
        }

        return operationResult;
    }
}
