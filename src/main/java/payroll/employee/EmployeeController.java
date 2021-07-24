package payroll.employee;

import java.util.List;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
class EmployeeController {

  private final EmployeeRepository repository;

  EmployeeController(EmployeeRepository repository) {

    this.repository = repository;
  }

  // Aggregate root
  // tag::get-aggregate-root[]
  @GetMapping("/employees")
  ResponseEntity<?> all() {

    List<Employee> employees = repository.findAll();

    return ResponseEntity.ok().body(employees);
  }
  // end::get-aggregate-root[]

  @PostMapping("/employees")
  ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) {
  
    Employee newEmployeeCreated = repository.save(newEmployee);
  
    return ResponseEntity.ok().body(newEmployeeCreated);
  }

  // Single item
  @GetMapping("/employees/{id}")
  ResponseEntity<?> one(@PathVariable Long id) {

    Employee employee = repository.findById(id) //
        .orElseThrow(() -> new EmployeeNotFoundException(id));

    return ResponseEntity.ok().body(employee);
  }

  // PUT
  @PutMapping("/employees/{id}")
  ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

    Employee updatedEmployee = repository.findById(id) //
        .map(employee -> {
          employee.setName(newEmployee.getName());
          employee.setRole(newEmployee.getRole());
          return repository.save(employee);
        }) //
        .orElseGet(() -> {
          newEmployee.setId(id);
          return repository.save(newEmployee);
        });

    return ResponseEntity.ok().body(updatedEmployee);
  }

  @DeleteMapping("/employees/{id}")
  ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
    repository.deleteById(id);

    return ResponseEntity.noContent().build();
  }
}