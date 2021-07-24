package payroll.contract;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
class ContractController {

  private final ContractRepository repository;
  private final ContractModelAssembler assembler;

  ContractController(ContractRepository repository, ContractModelAssembler assembler) {

    this.repository = repository;
    this.assembler = assembler;
  }

  // Aggregate root
  // tag::get-aggregate-root[]
  @GetMapping("/contracts")
  CollectionModel<EntityModel<Contract>> all() {

    List<EntityModel<Contract>> contracts = repository.findAll().stream()
        .map(contract -> EntityModel.of(contract,
            linkTo(methodOn(ContractController.class).one(contract.getId())).withSelfRel(),
            linkTo(methodOn(ContractController.class).all()).withRel("contracts")))
        .collect(Collectors.toList());

    return CollectionModel.of(contracts, linkTo(methodOn(ContractController.class).all()).withSelfRel());
  }
  // end::get-aggregate-root[]

  @PostMapping("/contracts")
  ResponseEntity<?> newContract(@RequestBody Contract newContract) {
  
    EntityModel<Contract> entityModel = assembler.toModel(repository.save(newContract));
  
    return ResponseEntity //
        .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
        .body(entityModel);
  }

  // Single item
  @GetMapping("/contracts/{id}")
  EntityModel<Contract> one(@PathVariable Long id) {

    Contract contract = repository.findById(id) //
        .orElseThrow(() -> new ContractNotFoundException(id));

    return EntityModel.of(contract, //
        linkTo(methodOn(ContractController.class).one(id)).withSelfRel(),
        linkTo(methodOn(ContractController.class).all()).withRel("contracts"));
  }

  // PUT
  @PutMapping("/contracts/{id}")
  ResponseEntity<?> replaceContract(@RequestBody Contract newContract, @PathVariable Long id) {

    Contract updatedContract = repository.findById(id) //
        .map(contract -> {
          contract.setName(newContract.getName());
          return repository.save(contract);
        }) //
        .orElseGet(() -> {
          newContract.setId(id);
          return repository.save(newContract);
        });

    EntityModel<Contract> entityModel = assembler.toModel(updatedContract);

    return ResponseEntity //
        .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
        .body(entityModel);
  }

  @DeleteMapping("/contract/{id}")
  ResponseEntity<?> deleteContract(@PathVariable Long id) {
    repository.deleteById(id);

    return ResponseEntity.noContent().build();
  }
}