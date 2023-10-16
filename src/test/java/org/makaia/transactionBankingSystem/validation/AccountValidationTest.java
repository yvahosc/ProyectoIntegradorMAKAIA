package org.makaia.transactionBankingSystem.validation;

import org.junit.jupiter.api.Test;
import org.makaia.transactionBankingSystem.dto.dtoAccount.DTOAccountCreation;
import org.makaia.transactionBankingSystem.dto.dtoAccount.DTOAccountDeposit;
import org.makaia.transactionBankingSystem.dto.dtoAccount.DTOAccountTransfer;
import org.makaia.transactionBankingSystem.dto.dtoPerson.DTOPersonCreate;
import org.makaia.transactionBankingSystem.exception.ApiException;
import org.makaia.transactionBankingSystem.model.Person;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountValidationTest {

    AccountValidation accountValidation = new AccountValidation();

    @Test
    void createAccountValidationWithNullOwnerAndNullInitialBalance() {
        DTOAccountCreation dtoAccountCreation = new DTOAccountCreation();
        ApiException apiException = assertThrows(ApiException.class,
                () -> accountValidation.createAccountValidation(dtoAccountCreation));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("- Error al ingresar el saldo. Ingrese " +
                "información no nula.", "- Error al ingresar el propietario. " +
                "Ingrese información no nula."), apiException.getErrors());
    }

    @Test
    void createAccountValidationWithInvalidInitialBalance() {
        BigDecimal initialBalance = BigDecimal.valueOf(-1);
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        DTOPersonCreate owner = new DTOPersonCreate(person, "1235");
        DTOAccountCreation dtoAccountCreation = new DTOAccountCreation(owner,
                initialBalance);
        ApiException apiException = assertThrows(ApiException.class,
                () -> accountValidation.createAccountValidation(dtoAccountCreation));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("- Error. El saldo inicial de la cuenta debe ser mayor o igual a $0."), apiException.getErrors());
    }

    @Test
    void createAccountValidation() throws ApiException {
        BigDecimal initialBalance = BigDecimal.valueOf(0);
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        DTOPersonCreate owner = new DTOPersonCreate(person, "1235");
        DTOAccountCreation dtoAccountCreation = new DTOAccountCreation(owner,
                initialBalance);
        assertDoesNotThrow(() -> accountValidation.createAccountValidation(dtoAccountCreation));
        assertTrue(accountValidation.createAccountValidation(dtoAccountCreation));
    }

    @Test
    void depositInAccountValidationWithInvalidNumberAccountAndNullAmount() {
        Long id = 123L;
        DTOAccountDeposit dtoAccountDeposit = new DTOAccountDeposit();
        ApiException apiException = assertThrows(ApiException.class,
                () -> accountValidation.depositAccountValidation(id, dtoAccountDeposit));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("- Error al ingresar el numero de la cuenta. " +
                        "Ingrese un valor válido: número de 15 dígitos.", "- Error al" +
                        " ingresar la cantidad. Ingrese información no nula."),
                apiException.getErrors());
    }

    @Test
    void depositInAccountValidationWithInvalidAmount() {
        Long id = 333908555109438L;
        DTOAccountDeposit dtoAccountDeposit =
                new DTOAccountDeposit(BigDecimal.valueOf(-1));
        ApiException apiException = assertThrows(ApiException.class,
                () -> accountValidation.depositAccountValidation(id, dtoAccountDeposit));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("- Error. La cantidad a transferir debe ser mayor o igual a $2000."),
                apiException.getErrors());
    }

    @Test
    void depositAccountValidation() throws ApiException {
        Long id = 333908555109438L;
        DTOAccountDeposit dtoAccountDeposit =
                new DTOAccountDeposit(BigDecimal.valueOf(2000));
        assertDoesNotThrow(() -> accountValidation.depositAccountValidation(id, dtoAccountDeposit));
        assertTrue(accountValidation.depositAccountValidation(id, dtoAccountDeposit));
    }

    @Test
    void getAccountValidationWithInvalidNumberAccount() {
        Long id = 3L;
        ApiException apiException = assertThrows(ApiException.class,
                () -> accountValidation.getAccountValidation(id));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("- Error al ingresar el numero de la cuenta. " +
                        "Ingrese un valor válido: número de 15 dígitos."),
                apiException.getErrors());
    }

    @Test
    void getAccountValidation() throws ApiException {
        Long id = 333908555109438L;
        assertDoesNotThrow(() -> accountValidation.getAccountValidation(id));
        assertTrue(accountValidation.getAccountValidation(id));
    }

    @Test
    void transferAccountValidationWithErrors(){
        DTOAccountTransfer dtoAccountTransfer = new DTOAccountTransfer(333908555109438L,
                333908555109438L, BigDecimal.valueOf(1000));
        ApiException apiException = assertThrows(ApiException.class,
                () -> accountValidation.transferAccountValidation(dtoAccountTransfer));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("- Error. La cantidad a transferir debe ser mayor o igual a $2000."),
                apiException.getErrors());
    }

    @Test
    void transferAccountValidation() throws ApiException {
        DTOAccountTransfer dtoAccountTransfer = new DTOAccountTransfer(333908555109438L,
                333908555109438L, BigDecimal.valueOf(2000));
        assertDoesNotThrow(() -> accountValidation.transferAccountValidation(dtoAccountTransfer));
        assertTrue(accountValidation.transferAccountValidation(dtoAccountTransfer));
    }
}
