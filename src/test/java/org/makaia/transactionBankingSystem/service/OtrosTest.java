package org.makaia.transactionBankingSystem.service;

import org.junit.jupiter.api.Test;
import org.makaia.transactionBankingSystem.dto.dtoAccount.DTOAccountCreation;
import org.makaia.transactionBankingSystem.dto.dtoAccount.DTOAccountDeposit;
import org.makaia.transactionBankingSystem.dto.dtoPerson.DTOPersonCreate;
import org.makaia.transactionBankingSystem.exception.ApiException;
import org.makaia.transactionBankingSystem.model.Account;
import org.makaia.transactionBankingSystem.model.Person;
import org.makaia.transactionBankingSystem.repository.AccountRepository;
import org.makaia.transactionBankingSystem.validation.AccountValidation;
import org.makaia.transactionBankingSystem.validation.PersonValidation;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class OtrosTest {

    AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
    AccountValidation accountValidation = new AccountValidation();
    PersonValidation personValidation = new PersonValidation();
    PersonService personService = Mockito.mock(PersonService.class);
    PocketService pocketService;

    AccountService accountService = new AccountService(accountRepository,
            accountValidation, personValidation, personService, pocketService);


    @Test
    void getAccountByIdWithInvalidId() throws ApiException {
        Long id = 12345L;
        ApiException apiException = assertThrows(ApiException.class,
                () -> accountService.getAccountById(id));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("- Error al ingresar el " +
                "numero de la cuenta. Ingrese un valor válido: número de 15 " +
                "dígitos."), apiException.getErrors());
    }

    @Test
    void getAccountByIdWithNonExistentId() throws ApiException {
        Long id = 434532147915509L;
        Optional<Account> account = Optional.empty();
        Mockito.when(accountRepository.findById(id)).thenReturn(account);
        ApiException apiException = assertThrows(ApiException.class,
                () -> accountService.getAccountById(id));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("La cuenta con id " +
                "'434532147915509' no se encuentra " +
                "en la base de datos."), apiException.getErrors());
    }

    @Test
    void getAccountByIdWithExistentId() throws ApiException {
        Long id = 434532147915509L;
        BigDecimal balance = BigDecimal.valueOf(0);
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        Account account = new Account(id, balance, person);
        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(account));
        assertEquals(434532147915509L,
                accountService.getAccountById(id).getAccountNumber());
    }

    @Test
    void createAccountWithNullOwnerAndNullInitialBalance() {
        DTOAccountCreation dtoAccountCreation = new DTOAccountCreation();
        ApiException apiException = assertThrows(ApiException.class,
                () -> accountService.createAccount(dtoAccountCreation));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("- Error al ingresar el saldo. Ingrese " +
                "información no nula.", "- Error al ingresar el propietario. " +
                "Ingrese información no nula."), apiException.getErrors());
    }

    @Test
    void createAccountWithInvalidInformationOfOwner() {
        BigDecimal initialBalance = BigDecimal.valueOf(0);
        Person person = new Person();
        DTOPersonCreate owner = new DTOPersonCreate(person, "1235");
        DTOAccountCreation dtoAccountCreation =
                new DTOAccountCreation(owner, initialBalance);
        ApiException apiException = assertThrows(ApiException.class,
                () -> accountService.createAccount(dtoAccountCreation));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("- Error al ingresar el documento de identidad. " +
                "Ingrese información no nula.", "- Error al ingresar el " +
                "nombre y/o apellido. Ingrese información no nula.", "- Error" +
                " al ingresar el correo. Ingrese información no nula.", "- " +
                "Error al ingresar el teléfono. Ingrese información no nula."),
                apiException.getErrors());
    }

    @Test
    void createSuccessfulAccountWithExistingPerson() throws ApiException {
        BigDecimal initialBalance = BigDecimal.valueOf(0);
        Person person = new Person("1020475585", "Yeisson", "Vahos",
                "+57-3046406015", "yvahosc@gmail.com");
        DTOPersonCreate owner = new DTOPersonCreate(person, "1235");
        DTOAccountCreation dtoAccountCreation =
                new DTOAccountCreation(owner, initialBalance);
        Person existingPerson = new Person("1020475585", "Yeisson", "Vahos",
                "+57-3046406015", "yvahosc@gmail.com");
        Mockito.when(accountService.getPersonById(dtoAccountCreation.getOwner().getPerson().getId()))
                .thenReturn(existingPerson);
        Account account = new Account(333908555109438L,
                dtoAccountCreation.getInitialBalance(), dtoAccountCreation.getOwner().getPerson());
        Mockito.when(accountRepository.save(Mockito.any())).thenReturn(account);
        assertEquals(333908555109438L,
                accountService.createAccount(dtoAccountCreation).getAccountNumber());
    }

    @Test
    void createSuccessfulAccountWithNonExistingPerson() throws ApiException {
        BigDecimal initialBalance = BigDecimal.valueOf(0);
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        DTOPersonCreate owner = new DTOPersonCreate(person, "1235");
        DTOAccountCreation dtoAccountCreation =
                new DTOAccountCreation(owner, initialBalance);
        Mockito.when(personService.getPersonById(Mockito.any())).thenReturn(null);
        Mockito.when(personService.createPerson(Mockito.any())).thenReturn(person);
        Account account = new Account(333908555109438L,
                dtoAccountCreation.getInitialBalance(), dtoAccountCreation.getOwner().getPerson());
        Mockito.when(accountRepository.save(Mockito.any())).thenReturn(account);
        assertEquals(333908555109438L,
                accountService.createAccount(dtoAccountCreation).getAccountNumber());
    }

    @Test
    void depositInAccountWithInvalidNumberAccountAndInvalidAmount() {
        Long id = 123L;
        DTOAccountDeposit dtoAccountDeposit = new DTOAccountDeposit();
        ApiException apiException = assertThrows(ApiException.class,
                () -> accountService.depositInAccount(id, dtoAccountDeposit));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("- Error al ingresar el numero de la cuenta. " +
                "Ingrese un valor válido: número de 15 dígitos.", "- Error al" +
                " ingresar la cantidad. Ingrese información no nula."),
                apiException.getErrors());
    }

    @Test
    void depositInAccountWithNonExistingAccount() {
        Long id = 333908555109438L;
        BigDecimal amount = BigDecimal.valueOf(20000);
        DTOAccountDeposit dtoAccountDeposit = new DTOAccountDeposit(amount);
        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.empty());
        ApiException apiException = assertThrows(ApiException.class,
                () -> accountService.depositInAccount(id, dtoAccountDeposit));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("La cuenta con id '333908555109438' no se encuentra en la base de datos."),
                apiException.getErrors());
    }

    @Test
    void depositInAccountWithInvalidAmount() {
        Long id = 333908555109438L;
        BigDecimal amount = BigDecimal.valueOf(1000);
        DTOAccountDeposit dtoAccountDeposit = new DTOAccountDeposit(amount);
        ApiException apiException = assertThrows(ApiException.class,
                () -> accountService.depositInAccount(id, dtoAccountDeposit));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("- Error. La cantidad a transferir debe ser mayor o igual a $2000."),
                apiException.getErrors());
    }

    @Test
    void depositInAccount() throws ApiException {
        Long id = 333908555109438L;
        BigDecimal amount = BigDecimal.valueOf(2000);
        DTOAccountDeposit dtoAccountDeposit = new DTOAccountDeposit(amount);
        BigDecimal balance = BigDecimal.valueOf(0);
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        Account account = new Account(id,balance, person);
        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(account));
        Mockito.when(accountRepository.save(Mockito.any())).thenReturn(account);
        assertEquals(BigDecimal.valueOf(2000),
                accountService.depositInAccount(id, dtoAccountDeposit).getBalance());
    }

    @Test
    void transferToAccount() {
    }

    @Test
    void getPocketsByAccountNumber() {
    }

    @Test
    void getPersonById() {
    }

    @Test
    void createPerson() {
    }

    @Test
    void balanceInPocketsOfOneAccount() {
    }

    @Test
    void isLoggedInUserTheOwnerOfTheAccount() {
    }
}