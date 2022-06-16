package com.ppryvarnikov.postalservice.web;

import com.ppryvarnikov.postalservice.model.Department;
import com.ppryvarnikov.postalservice.service.DepartmentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
    public Department createDepartment(@RequestBody Department departmentDto) throws InstanceAlreadyExistsException {
        Department department = departmentService.save(departmentDto.toBuilder()
                .id(null)
                .build());
        log.info("New department successfully created: {}", department);
        return department;
    }

    @PostMapping("/create-departments")
    public List<Department> createDepartments(@RequestBody List<Department> departmentsDto) throws InstanceAlreadyExistsException {
        departmentsDto = departmentsDto.stream()
                .map(department -> department.toBuilder().id(null).build())
                .collect(Collectors.toList());
        List<Department> departments = departmentService.saveAll(departmentsDto);
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
