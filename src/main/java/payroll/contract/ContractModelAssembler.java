package payroll.contract;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class ContractModelAssembler implements RepresentationModelAssembler<Contract, EntityModel<Contract>> {

  @Override
  public EntityModel<Contract> toModel(Contract contract) {

    return EntityModel.of(contract, //
        linkTo(methodOn(ContractController.class).one(contract.getId())).withSelfRel(),
        linkTo(methodOn(ContractController.class).all()).withRel("contracts"));
  }
}