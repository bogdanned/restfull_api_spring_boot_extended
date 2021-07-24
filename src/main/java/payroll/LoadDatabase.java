package payroll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import payroll.contract.Contract;
import payroll.contract.ContractRepository;
import payroll.employee.Employee;
import payroll.employee.EmployeeRepository;

@Configuration
class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(EmployeeRepository employeeRepository, ContractRepository contractRepo) {

    return args -> {
      log.info("Preloading " + employeeRepository.save(new Employee("Bilbo", "Baggins", "burglar")));
      log.info("Preloading " + employeeRepository.save(new Employee("Frodo", "Baggins", "thief")));
      log.info("Preloading " + contractRepo.save(new Contract("work_contract")));
      log.info("Preloading " + contractRepo.save(new Contract("freelance_contract")));
    };
  }
}