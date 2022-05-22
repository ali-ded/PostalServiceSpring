package com.fintech.ppryvarnikov.postalservice.service;

import com.fintech.ppryvarnikov.postalservice.model.Department;
import com.fintech.ppryvarnikov.postalservice.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Optional<Department> findByDescription(String description) {
        return departmentRepository.findByDescription(description);
    }

    @Override
    public Department save(String departmentName) throws InstanceAlreadyExistsException {
        if (findByDescription(departmentName).isPresent()) {
            throw new InstanceAlreadyExistsException("This department already exists.");
        }
        return departmentRepository.save(Department.builder()
                .description(departmentName)
                .build());
    }

    @Override
    public List<Department> saveAll(List<String> departmentNames) throws InstanceAlreadyExistsException {
        departmentNames.removeIf(s -> departmentRepository.findByDescription(s).isPresent());
        List<Department> departments = departmentNames.stream()
                .map(departmentName -> Department.builder()
                        .description(departmentName)
                        .build())
                .collect(Collectors.toList());
        if (departments.isEmpty()) {
            throw new InstanceAlreadyExistsException("These departments already exist.");
        }
        return (List<Department>) departmentRepository.saveAll(departments);
    }

    @Override
    public boolean update(Department department) {
        boolean operationResult = false;
        int updatedRows;

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
