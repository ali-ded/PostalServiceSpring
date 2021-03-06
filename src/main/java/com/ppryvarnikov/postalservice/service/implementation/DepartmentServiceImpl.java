package com.ppryvarnikov.postalservice.service.implementation;

import com.ppryvarnikov.postalservice.model.Department;
import com.ppryvarnikov.postalservice.repository.DepartmentRepository;
import com.ppryvarnikov.postalservice.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
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
    public Department save(Department department) throws InstanceAlreadyExistsException {
        if (findByDescription(department.getDescription()).isPresent()) {
            throw new InstanceAlreadyExistsException("This department already exists.");
        }
        return departmentRepository.save(department);
    }

    @Override
    public List<Department> saveAll(List<Department> departments) throws InstanceAlreadyExistsException {
        departments.removeIf(department -> departmentRepository
                .findByDescription(department.getDescription())
                .isPresent()
        );

        if (departments.isEmpty()) {
            throw new InstanceAlreadyExistsException("These departments already exist.");
        }
        return (List<Department>) departmentRepository.saveAll(departments);
    }

    @Transactional
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
