package payroll.contract;

class ContractNotFoundException extends RuntimeException {

  ContractNotFoundException(Long id) {
    super("Could not find contract " + id);
  }
}