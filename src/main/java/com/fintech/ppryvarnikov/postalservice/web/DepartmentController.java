package com.fintech.ppryvarnikov.postalservice.web;

import com.fintech.ppryvarnikov.postalservice.model.Department;
import com.fintech.ppryvarnikov.postalservice.service.DepartmentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Log4j2
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class DepartmentController {
    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping("/create-department")
    public Department createDepartment(@RequestBody Department department) {
        departmentService.save(department);
        log.info("New department successfully created: {}", department);
        return department;
    }

    @PostMapping("/create-multiple-departments")
    public List<Department> createDepartments(@RequestBody List<Department> departments) {
        departmentService.saveAll(departments);
        log.info("Departments group successfully created: {}", departments);
        return departments;
    }

    @GetMapping("/find-department-by-id")
    public Department findDepartmentById(@RequestParam("id") Integer id) {
        return departmentService
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Department with id number " + id + " not found.")
                );
    }

    @GetMapping("/find-all-departments")
    public List<Department> findAllDepartments() {
        return departmentService.findAll();
    }
}
