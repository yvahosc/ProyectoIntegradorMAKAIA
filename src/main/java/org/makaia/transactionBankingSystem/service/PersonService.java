package org.makaia.transactionBankingSystem.service;

import org.makaia.transactionBankingSystem.dto.dtoPerson.DTOPersonCreate;
import org.makaia.transactionBankingSystem.dto.dtoPerson.DTOPersonUpdate;
import org.makaia.transactionBankingSystem.exception.ApiException;
import org.makaia.transactionBankingSystem.model.Person;
import org.makaia.transactionBankingSystem.repository.PersonRepository;
import org.makaia.transactionBankingSystem.validation.PersonValidation;
import org.makaia.transactionBankingSystem.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    PersonRepository personRepository;
    PersonValidation personValidation;
    UserService userService;
    UserValidation userValidation;


    @Autowired
    public PersonService(PersonRepository personRepository, PersonValidation personValidation, UserService userService,
                         UserValidation userValidation)
    {
        this.personRepository = personRepository;
        this.personValidation = personValidation;
        this.userService = userService;
        this.userValidation = userValidation;
    }

    public Person getPersonById(String id) throws ApiException {
        personValidation.getPersonValidation(id);
        Optional <Person> person = this.personRepository.findById(id);
        List<String> error = List.of("La persona con id '" + id + "' no se encuentra en la base de datos.");
        if (person.isEmpty()){
            throw new ApiException (404, error);
        }
        return person.get();
    }

    public Person createPerson(DTOPersonCreate dtoPersonCreate) throws ApiException {

        personValidation.createPersonValidation(dtoPersonCreate.getPerson());
        userValidation.createUserValidation(dtoPersonCreate.getPassword());
        Person person = this.personRepository.save(dtoPersonCreate.getPerson());
        this.userService.createUser(dtoPersonCreate);
        return person;
    }

    public Person updatePerson(DTOPersonUpdate personUpdate) throws ApiException {
        personValidation.updatePersonValidation(personUpdate);
        Person existingPerson = getPersonById(personUpdate.getId());
        existingPerson.setEmail(personUpdate.getEmail());
        existingPerson.setPhone(personUpdate.getPhone());
        return this.personRepository.save(existingPerson);
    }
}
