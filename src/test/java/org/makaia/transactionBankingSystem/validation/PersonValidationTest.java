package org.makaia.transactionBankingSystem.validation;

import org.junit.jupiter.api.Test;
import org.makaia.transactionBankingSystem.dto.dtoPerson.DTOPersonUpdate;
import org.makaia.transactionBankingSystem.exception.ApiException;
import org.makaia.transactionBankingSystem.model.Person;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonValidationTest {

    PersonValidation personValidation = new PersonValidation();

    @Test
    void testCreatePersonValidationWithErrors() {
        Person person = new Person("1020", "Yeisson1", "Vahos1", "+57" +
                "3046406015", "yvahoscgmail.com");
        ApiException apiException = assertThrows(ApiException.class,
                () -> personValidation.createPersonValidation(person));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("- Error al ingresar el documento de identidad. " +
                        "Ingrese un identificador válido: número con entre " +
                                "seis y diez dígitos.",
                        "- Error al ingresar el nombre y/o apellido. Ingrese " +
                                "información válida: solo letras.", "- Error " +
                                "al ingresar el correo. Ingrese uno válido: " +
                                "con la estructura ejemplo@dominio.com.co",
                        "- Error al ingresar el teléfono. Ingrese uno válido:" +
                                " con la estructura +XX-XXXXXXXXXX"),
                apiException.getErrors());
    }

    @Test
    void testCreatePersonValidation() throws ApiException {
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        assertDoesNotThrow(() -> personValidation.createPersonValidation(person));
        assertTrue(personValidation.createPersonValidation(person));
    }

    @Test
    void testGetPersonValidationWithError() {
        String id = "3L";
        ApiException apiException = assertThrows(ApiException.class,
                () -> personValidation.getPersonValidation(id));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("- Error al ingresar el documento de identidad. " +
                        "Ingrese un identificador válido: número con entre " +
                        "seis y diez dígitos."),
                apiException.getErrors());
    }

    @Test
    void testGetPersonValidation() throws ApiException {
        String id = "1020475585";
        assertDoesNotThrow(() -> personValidation.getPersonValidation(id));
        assertTrue(personValidation.getPersonValidation(id));
    }

    @Test
    void testUpdatePersonValidationWithError() {
        DTOPersonUpdate person = new DTOPersonUpdate("1020", "+57" +
                "3046406015", "yvahoscgmail.com");
        ApiException apiException = assertThrows(ApiException.class,
                () -> personValidation.updatePersonValidation(person));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("- Error al ingresar el documento de identidad. " +
                                "Ingrese un identificador válido: número con entre " +
                                "seis y diez dígitos.", "- Error " +
                                "al ingresar el correo. Ingrese uno válido: " +
                                "con la estructura ejemplo@dominio.com.co",
                                "- Error al ingresar el teléfono. Ingrese uno válido:" +
                                " con la estructura +XX-XXXXXXXXXX"),
                apiException.getErrors());
    }

    @Test
    void testUpdatePersonValidation() throws ApiException {
        DTOPersonUpdate person = new DTOPersonUpdate("1020475585", "+57-3046406015", "yvahosc" +
                "@gmail.com");
        assertDoesNotThrow(() -> personValidation.updatePersonValidation(person));
        assertTrue(personValidation.updatePersonValidation(person));
    }
}