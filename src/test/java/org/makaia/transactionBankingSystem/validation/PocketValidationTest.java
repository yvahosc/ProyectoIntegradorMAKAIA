package org.makaia.transactionBankingSystem.validation;

import org.junit.jupiter.api.Test;
import org.makaia.transactionBankingSystem.dto.dtoPocket.DTOPocketCreation;
import org.makaia.transactionBankingSystem.dto.dtoPocket.DTOPocketTransfer;
import org.makaia.transactionBankingSystem.exception.ApiException;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PocketValidationTest {

    PocketValidation pocketValidation = new PocketValidation();

    @Test
    void createPocketValidationWithNameNull() {
        DTOPocketCreation dtoPocketCreation = new DTOPocketCreation(333908555109438L, null,
                BigDecimal.valueOf(1));
        ApiException apiException = assertThrows(ApiException.class,
                () -> pocketValidation.createPocketValidation(dtoPocketCreation));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("- Error al ingresar el nombre del bolsillo. " +
                        "Ingrese información no nula."),
                apiException.getErrors());
    }

    @Test
    void createPocketValidationWithInvalidParameters() {
        DTOPocketCreation dtoPocketCreation = new DTOPocketCreation(3L, "2",
                BigDecimal.valueOf(0));
        ApiException apiException = assertThrows(ApiException.class,
                () -> pocketValidation.createPocketValidation(dtoPocketCreation));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("- Error al ingresar el numero de la cuenta. " +
                                "Ingrese un valor válido: número de 15 dígitos.",
                                "- Error al ingresar el nombre del bolsillo. Ingrese " +
                                "información válida: solo letras.", "- Error. La cantidad" +
                                " a transferir debe ser mayor a $0."),
                apiException.getErrors());
    }

    @Test
    void createPocketValidation() throws ApiException {
        DTOPocketCreation dtoPocketCreation = new DTOPocketCreation(333908555109438L, "fg",
                BigDecimal.valueOf(1000));
        assertDoesNotThrow(() -> pocketValidation.createPocketValidation(dtoPocketCreation));
        assertTrue(pocketValidation.createPocketValidation(dtoPocketCreation));
    }

    @Test
    void transferPocketValidationWithErrors() {
        DTOPocketTransfer dtoPocketTransfer = new DTOPocketTransfer(1L, 1L,
                BigDecimal.valueOf(0));
        ApiException apiException = assertThrows(ApiException.class,
                () -> pocketValidation.transferPocketValidation(dtoPocketTransfer));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("- Error al ingresar el numero de la cuenta. " +
                        "Ingrese un valor válido: número de 15 dígitos.", "- " +
                        "Error. La cantidad a transferir debe ser mayor a $0."),
                apiException.getErrors());
    }

    @Test
    void transferPocketValidation() throws ApiException {
        DTOPocketTransfer dtoPocketTransfer = new DTOPocketTransfer(333908555109438L, 1L,
                BigDecimal.valueOf(2000));
        assertDoesNotThrow(() -> pocketValidation.transferPocketValidation(dtoPocketTransfer));
        assertTrue(pocketValidation.transferPocketValidation(dtoPocketTransfer));
    }
}