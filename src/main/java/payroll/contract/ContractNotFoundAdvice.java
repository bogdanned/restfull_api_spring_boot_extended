package payroll.contract;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class ContractNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(ContractNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String contractNotFoundHandler(ContractNotFoundException ex) {
    return ex.getMessage();
  }
}