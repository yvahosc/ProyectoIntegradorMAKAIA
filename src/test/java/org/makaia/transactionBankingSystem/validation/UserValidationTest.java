package org.makaia.transactionBankingSystem.validation;

import org.junit.jupiter.api.Test;
import org.makaia.transactionBankingSystem.exception.ApiException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserValidationTest {

    UserValidation userValidation = new UserValidation();

    @Test
    void createUserValidationWithErrors() {
        String id = "3L";
        ApiException apiException = assertThrows(ApiException.class,
                () -> userValidation.createUserValidation(id));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("- Error al ingresar la contraseña. Ingrese uno " +
                        "válido: cuatro digitos."),
                apiException.getErrors());
    }

    @Test
    void createUserValidationWithNull() {
        ApiException apiException = assertThrows(ApiException.class,
                () -> userValidation.createUserValidation(null));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("- Error al ingresar la contraseña. Ingrese " +
                        "información no nula."),
                apiException.getErrors());
    }

    @Test
    void createUserValidation() throws ApiException {
        String id = "1234";
        assertDoesNotThrow(() -> userValidation.createUserValidation(id));
        assertTrue(userValidation.createUserValidation(id));
    }

    @Test
    void getUserValidationWithErrors() {
        String userName = "3L";
        ApiException apiException = assertThrows(ApiException.class,
                () -> userValidation.getUserValidation(userName));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("- Error al ingresar el nombre de usuario. " +
                        "Ingrese información válida: solo letras."),
                apiException.getErrors());
    }

    @Test
    void getUserValidationWithNull() {
        ApiException apiException = assertThrows(ApiException.class,
                () -> userValidation.getUserValidation(null));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("- Error al ingresar el nombre de usuario. " +
                        "Ingrese información no nula."),
                apiException.getErrors());
    }

    @Test
    void getUserValidation() throws ApiException {
        String userName = "jkh";
        assertDoesNotThrow(() -> userValidation.getUserValidation(userName));
        assertTrue(userValidation.getUserValidation(userName));
    }
}