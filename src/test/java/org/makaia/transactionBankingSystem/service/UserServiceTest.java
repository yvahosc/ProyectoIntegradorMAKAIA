package org.makaia.transactionBankingSystem.service;

import org.junit.jupiter.api.Test;
import org.makaia.transactionBankingSystem.dto.dtoPerson.DTOPersonCreate;
import org.makaia.transactionBankingSystem.exception.ApiException;
import org.makaia.transactionBankingSystem.model.Person;
import org.makaia.transactionBankingSystem.model.User;
import org.makaia.transactionBankingSystem.repository.UserRepository;
import org.makaia.transactionBankingSystem.validation.UserValidation;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    UserRepository userRepository = Mockito.mock(UserRepository.class);
    UserValidation userValidation = Mockito.mock(UserValidation.class);

    UserService userService = new UserService(userRepository, userValidation);

    @Test
    void getUserByUserNameNonExistingUser() throws ApiException {
        String userName = "1020475585";
        Mockito.when(userValidation.getUserValidation(userName)).thenReturn(true);
        Mockito.when(userRepository.findByPersonId(userName)).thenReturn(null);
        ApiException apiException = assertThrows(ApiException.class,
                () -> userService.getUserByUserName(userName));
        assertEquals(404, apiException.getStatusCode());
        assertEquals(List.of("El usuario con nombre de usuario '1020475585' " +
                "no se encuentra en la base de datos."), apiException.getErrors());

    }

    @Test
    void getUserByUserNameExistingUser() throws ApiException {
        String userName = "1020475585";
        Mockito.when(userValidation.getUserValidation(userName)).thenReturn(true);
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        User user = new User("1234", person);
        Mockito.when(userRepository.findByPersonId(userName)).thenReturn(user);
        assertDoesNotThrow(() -> userService.getUserByUserName(userName));
        assertEquals("1020475585", userService.getUserByUserName(userName).getUsername());

    }

    @Test
    void createUser() throws ApiException {
        String password = "1234";
        Mockito.when(userValidation.createUserValidation(password)).thenReturn(true);
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        DTOPersonCreate dtoPersonCreate = new DTOPersonCreate(person, password);
        User user = new User(password, person);
        Mockito.when(userRepository.save(new User(user.getPassword(),
                user.getPerson()))).thenReturn(user);
        assertDoesNotThrow(() -> userService.createUser(dtoPersonCreate));
    }
}