package org.makaia.transactionBankingSystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.makaia.transactionBankingSystem.dto.dtoPerson.DTOPersonCreate;
import org.makaia.transactionBankingSystem.dto.dtoPerson.DTOPersonUpdate;
import org.makaia.transactionBankingSystem.exception.ApiException;
import org.makaia.transactionBankingSystem.model.Person;
import org.makaia.transactionBankingSystem.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api/persons")
public class PersonController {
    PersonService personService;
    @Autowired
    public PersonController(PersonService personService){
        this.personService = personService;
    }

    @Operation(hidden = true)
    @GetMapping ("/{id}")
    public ResponseEntity<Person> getPerson (@PathVariable String id) throws ApiException {
        return ResponseEntity.ok(this.personService.getPersonById(id));
    }

    @Operation(hidden = true)
    @PostMapping
    public ResponseEntity<Person> createPerson (@RequestBody DTOPersonCreate dtoPersonCreate) throws ApiException {
        return ResponseEntity.ok(this.personService.createPerson(dtoPersonCreate));
    }

    @Operation(hidden = true)
    @PutMapping
    public ResponseEntity<Person> updatePerson (@RequestBody DTOPersonUpdate person) throws ApiException {
        return ResponseEntity.ok(this.personService.updatePerson(person));
    }
}
