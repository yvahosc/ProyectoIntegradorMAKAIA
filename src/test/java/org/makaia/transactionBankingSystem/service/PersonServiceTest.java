package org.makaia.transactionBankingSystem.service;

import org.junit.jupiter.api.Test;
import org.makaia.transactionBankingSystem.dto.dtoPerson.DTOPersonCreate;
import org.makaia.transactionBankingSystem.dto.dtoPerson.DTOPersonUpdate;
import org.makaia.transactionBankingSystem.exception.ApiException;
import org.makaia.transactionBankingSystem.model.Person;
import org.makaia.transactionBankingSystem.model.User;
import org.makaia.transactionBankingSystem.repository.PersonRepository;
import org.makaia.transactionBankingSystem.validation.PersonValidation;
import org.makaia.transactionBankingSystem.validation.UserValidation;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PersonServiceTest {
    PersonRepository personRepository = Mockito.mock(PersonRepository .class);
    PersonValidation personValidation = Mockito.mock(PersonValidation.class);
    UserService userService = Mockito.mock(UserService.class);
    UserValidation userValidation = Mockito.mock(UserValidation.class);

    PersonService personService = new PersonService(personRepository,
            personValidation, userService, userValidation);

    @Test
    void getPersonByIdWithNonExistentId() throws ApiException {
        String id = "1020475585";
        Mockito.when(personValidation.getPersonValidation(id)).thenReturn(true);
        Optional<Person> person = Optional.empty();
        Mockito.when(personRepository.findById(id)).thenReturn(person);
        ApiException apiException = assertThrows(ApiException.class,
                () -> personService.getPersonById(id));
        assertEquals(404, apiException.getStatusCode());
        assertEquals(List.of("La persona con id '1020475585' no se encuentra " +
                "en la base de datos."), apiException.getErrors());
    }

    @Test
    void getPersonByIdWithExistentId() throws ApiException {
        String id = "1020475585";
        Mockito.when(personValidation.getPersonValidation(id)).thenReturn(true);
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        Mockito.when(personRepository.findById(id)).thenReturn(Optional.of(person));
        assertDoesNotThrow(() -> personService.getPersonById(id));
        assertEquals(id,
                personService.getPersonById(id).getId());
    }

    @Test
    void createPerson() throws ApiException {
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        String password = "1234";
        DTOPersonCreate dtoPersonCreate = new DTOPersonCreate(person, password);
        UserDetails userDetails = new User(password, person);
        Mockito.when(personValidation.createPersonValidation(dtoPersonCreate.getPerson())).thenReturn(true);
        Mockito.when(userValidation.createUserValidation(dtoPersonCreate.getPassword())).thenReturn(true);
        Mockito.when(personRepository.save(dtoPersonCreate.getPerson())).thenReturn(person);
        Mockito.when(userService.createUser(dtoPersonCreate)).thenReturn(userDetails);
        assertDoesNotThrow(() -> personService.createPerson(dtoPersonCreate));
        assertEquals("1020475585",
                personService.createPerson(dtoPersonCreate).getId());
    }

    @Test
    void updatePerson() {
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        DTOPersonUpdate dtoPersonUpdate = new DTOPersonUpdate("1020475585",
                "-3046406015", "yvahosc@gmail.com");
        Mockito.when(personRepository.findById("1020475585")).thenReturn(Optional.of(person));
        Mockito.when(personRepository.save(person)).thenReturn(person);
        assertDoesNotThrow(() -> personService.updatePerson(dtoPersonUpdate));


    }
}