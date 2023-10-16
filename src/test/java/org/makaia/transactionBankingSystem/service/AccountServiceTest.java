package org.makaia.transactionBankingSystem.service;

import org.junit.jupiter.api.Test;
import org.makaia.transactionBankingSystem.dto.dtoAccount.DTOAccountCreation;
import org.makaia.transactionBankingSystem.dto.dtoAccount.DTOAccountDeposit;
import org.makaia.transactionBankingSystem.dto.dtoAccount.DTOAccountTransfer;
import org.makaia.transactionBankingSystem.dto.dtoPerson.DTOPersonCreate;
import org.makaia.transactionBankingSystem.dto.dtoPocket.DTOPocketConsultIn;
import org.makaia.transactionBankingSystem.dto.dtoPocket.DTOPocketConsultOut;
import org.makaia.transactionBankingSystem.exception.ApiException;
import org.makaia.transactionBankingSystem.model.Account;
import org.makaia.transactionBankingSystem.model.Person;
import org.makaia.transactionBankingSystem.repository.AccountRepository;
import org.makaia.transactionBankingSystem.validation.AccountValidation;
import org.makaia.transactionBankingSystem.validation.PersonValidation;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {
    AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
    AccountValidation accountValidation = Mockito.mock(AccountValidation.class);
    PersonValidation personValidation = Mockito.mock(PersonValidation.class);
    PersonService personService = Mockito.mock(PersonService.class);
    PocketService pocketService = Mockito.mock(PocketService.class);

    AccountService accountService = new AccountService(accountRepository,
            accountValidation, personValidation, personService, pocketService);

    @Test
    void getAccountByIdWithNonExistentId() throws ApiException {
        Long id = 434532147915509L;
        Mockito.when(accountValidation.getAccountValidation(id)).thenReturn(true);
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
        Mockito.when(accountValidation.getAccountValidation(id)).thenReturn(true);
        BigDecimal balance = BigDecimal.valueOf(0);
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        Account account = new Account(id, balance, person);
        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(account));
        assertEquals(434532147915509L,
                accountService.getAccountById(id).getAccountNumber());
    }

    @Test
    void createSuccessfulAccountWithExistingPerson() throws ApiException {
        BigDecimal initialBalance = BigDecimal.valueOf(0);
        Person person = new Person("1020475585", "Yeisson", "Vahos",
                "+57-3046406015", "yvahosc@gmail.com");
        DTOPersonCreate owner = new DTOPersonCreate(person, "1235");
        DTOAccountCreation dtoAccountCreation =
                new DTOAccountCreation(owner, initialBalance);
        Mockito.when(accountValidation.createAccountValidation(dtoAccountCreation)).thenReturn(true);
        Mockito.when(personValidation.createPersonValidation(owner.getPerson())).thenReturn(true);

        Person existingPerson = new Person("1020475585", "Yeisson", "Vahos",
                "+57-3046406015", "yvahosc@gmail.com");

        Mockito.when(accountService.getPersonById(dtoAccountCreation.getOwner().getPerson().getId()))
                .thenReturn(existingPerson);
        Account account = new Account(333908555109438L,
                dtoAccountCreation.getInitialBalance(), dtoAccountCreation.getOwner().getPerson());
        Mockito.when(accountRepository.save(Mockito.any())).thenReturn(account);
        assertDoesNotThrow(() -> accountService.createAccount(dtoAccountCreation));
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
        Mockito.when(accountValidation.createAccountValidation(dtoAccountCreation)).thenReturn(true);
        Mockito.when(personValidation.createPersonValidation(owner.getPerson())).thenReturn(true);
        Mockito.when(personService.getPersonById(Mockito.any())).thenReturn(null);
        Mockito.when(personService.createPerson(Mockito.any())).thenReturn(person);
        Account account = new Account(333908555109438L,
                dtoAccountCreation.getInitialBalance(), dtoAccountCreation.getOwner().getPerson());
        Mockito.when(accountRepository.save(Mockito.any())).thenReturn(account);
        assertDoesNotThrow(() -> accountService.createAccount(dtoAccountCreation));
        assertEquals(333908555109438L,
                accountService.createAccount(dtoAccountCreation).getAccountNumber());
    }

    @Test
    void depositInAccountWithNonExistingAccount() throws ApiException {
        Long id = 333908555109438L;
        BigDecimal amount = BigDecimal.valueOf(20000);
        DTOAccountDeposit dtoAccountDeposit = new DTOAccountDeposit(amount);
        Mockito.when(accountValidation.depositAccountValidation(id,
                dtoAccountDeposit)).thenReturn(true);
        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.empty());
        ApiException apiException = assertThrows(ApiException.class,
                () -> accountService.depositInAccount(id, dtoAccountDeposit));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("La cuenta con id '333908555109438' no se encuentra en la base de datos."),
                apiException.getErrors());
    }

    @Test
    void depositInAccount() throws ApiException {
        Long id = 333908555109438L;
        BigDecimal amount = BigDecimal.valueOf(2000);
        DTOAccountDeposit dtoAccountDeposit = new DTOAccountDeposit(amount);
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        Account account = new Account(id, BigDecimal.valueOf(0), person);
        Mockito.when(accountValidation.depositAccountValidation(id,
                dtoAccountDeposit)).thenReturn(true);
        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(account));
        Mockito.when(accountRepository.save(Mockito.any())).thenReturn(account);
        assertDoesNotThrow(() -> accountService.depositInAccount(id, dtoAccountDeposit));
    }

    @Test
    void transferToAccountWithEqualsSourceAndDestinationAccounts() throws ApiException {
        Long sourceAccountNumber = 333908555109438L;
        Long destinationAccountNumber = 333908555109438L;
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        Account account = new Account(sourceAccountNumber, BigDecimal.valueOf(0), person);
        DTOAccountTransfer dtoAccountTransfer =
                new DTOAccountTransfer(sourceAccountNumber,
                        destinationAccountNumber, BigDecimal.valueOf(10000));
        Mockito.when(accountValidation.transferAccountValidation(dtoAccountTransfer)).thenReturn(true);
        Mockito.when(accountRepository.findById(sourceAccountNumber)).thenReturn(Optional.of(account));
        Mockito.when(accountRepository.findById(destinationAccountNumber)).thenReturn(Optional.of(account));
        ApiException apiException = assertThrows(ApiException.class,
                () -> accountService.transferToAccount(dtoAccountTransfer));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("Las cuentas de origen y destino no pueden ser iguales."),
                apiException.getErrors());
    }


    @Test
    void transferToAccountWithNonEnoughBalanceInSourceAccount() throws ApiException {
        Long sourceAccountNumber = 333908555109438L;
        Long destinationAccountNumber = 100908555109438L;
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        Account existingSourceAccount = new Account(sourceAccountNumber,
                BigDecimal.valueOf(10000), person);
        Account existingDestinationAccount = new Account(sourceAccountNumber, BigDecimal.valueOf(0), person);
        DTOAccountTransfer dtoAccountTransfer =
                new DTOAccountTransfer(sourceAccountNumber,
                        destinationAccountNumber, BigDecimal.valueOf(12000));
        Mockito.when(accountValidation.transferAccountValidation(dtoAccountTransfer)).thenReturn(true);
        Mockito.when(accountRepository.findById(sourceAccountNumber)).thenReturn(Optional.of(existingSourceAccount));
        Mockito.when(accountRepository.findById(destinationAccountNumber)).thenReturn(Optional.of(existingDestinationAccount));
        Mockito.when(pocketService.getPocketsByAccountNumber(sourceAccountNumber)).thenReturn(new DTOPocketConsultOut());
        ApiException apiException = assertThrows(ApiException.class,
                () -> accountService.transferToAccount(dtoAccountTransfer));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("La cuenta con numero '333908555109438' no tiene suficientes fondos disponibles para realizar la transferencia."),
                apiException.getErrors());
    }

    @Test
    void transferToAccountWithEnoughBalanceInSourceAccount() throws ApiException {
        Long sourceAccountNumber = 333908555109438L;
        Long destinationAccountNumber = 100908555109438L;
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        Account existingSourceAccount = new Account(sourceAccountNumber,
                BigDecimal.valueOf(10000), person);
        Account existingDestinationAccount = new Account(sourceAccountNumber, BigDecimal.valueOf(0), person);
        DTOAccountTransfer dtoAccountTransfer =
                new DTOAccountTransfer(sourceAccountNumber,
                        destinationAccountNumber, BigDecimal.valueOf(10000));
        Mockito.when(accountValidation.transferAccountValidation(dtoAccountTransfer)).thenReturn(true);
        Mockito.when(accountRepository.findById(sourceAccountNumber)).thenReturn(Optional.of(existingSourceAccount));
        Mockito.when(accountRepository.findById(destinationAccountNumber)).thenReturn(Optional.of(existingDestinationAccount));
        Mockito.when(pocketService.getPocketsByAccountNumber(sourceAccountNumber)).thenReturn(new DTOPocketConsultOut());
        assertDoesNotThrow(() -> accountService.transferToAccount(dtoAccountTransfer));
    }


    @Test
    void getPocketsByAccountNumberExistingPockets() {
        Long accountNumber = 333908555109438L;
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        Account account = new Account(accountNumber, BigDecimal.valueOf(1000),
                person);
        Mockito.when(accountRepository.findById(accountNumber)).thenReturn(Optional.of(account));
        DTOPocketConsultIn dtoPocketConsultIn = new DTOPocketConsultIn(1L,
                "Vacaciones", BigDecimal.valueOf(1000));
        List<DTOPocketConsultIn> pocketsIn = List.of(dtoPocketConsultIn);
        DTOPocketConsultOut pockets =
                new DTOPocketConsultOut(pocketsIn);
        Mockito.when(pocketService.getPocketsByAccountNumber(accountNumber)).thenReturn(pockets);
        assertDoesNotThrow(() -> accountService.getPocketsByAccountNumber(333908555109438L));
    }

    @Test
    void getPocketsByAccountNumberNonExistingPockets() {
        Long accountNumber = 333908555109438L;
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        Account account = new Account(accountNumber, BigDecimal.valueOf(1000),
                person);
        Mockito.when(accountRepository.findById(accountNumber)).thenReturn(Optional.of(account));
        List<DTOPocketConsultIn> pocketsIn = new ArrayList<>();
        DTOPocketConsultOut pockets =
                new DTOPocketConsultOut(pocketsIn);
        Mockito.when(pocketService.getPocketsByAccountNumber(accountNumber)).thenReturn(pockets);
        ApiException apiException = assertThrows(ApiException.class,
                () -> accountService.getPocketsByAccountNumber(333908555109438L));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("La cuenta con numero '333908555109438' no tiene bolsillos asociados."),
                apiException.getErrors());
    }

    @Test
    void getPersonByIdNonExistingId() throws ApiException {
        String id = "1020475585";
        Mockito.when(personService.getPersonById(id)).thenThrow(new
                ApiException(404, List.of("La persona con id '" + id + "' no se" +
                " encuentra en la base de datos.")));
        assertNull(accountService.getPersonById(id));
    }

    @Test
    void balanceInPocketsOfOneAccountExistingPockets() {
        Long accountNumber = 333908555109438L;
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        Account account = new Account(accountNumber, BigDecimal.valueOf(1000),
                person);
        Mockito.when(accountRepository.findById(333908555109438L)).thenReturn(Optional.of(account));
        DTOPocketConsultIn dtoPocketConsultIn = new DTOPocketConsultIn(1L,
                "Vacaciones", BigDecimal.valueOf(1000));
        List<DTOPocketConsultIn> pocketsIn = List.of(dtoPocketConsultIn);
        DTOPocketConsultOut pocketsInAccount =
                new DTOPocketConsultOut(pocketsIn);
        Mockito.when(pocketService.getPocketsByAccountNumber(accountNumber)).thenReturn(pocketsInAccount);
        assertDoesNotThrow(() -> accountService.balanceInPocketsOfOneAccount(accountNumber));
        assertEquals(BigDecimal.valueOf(1000), accountService.balanceInPocketsOfOneAccount(accountNumber));
    }

    @Test
    void isLoggedInUserNonOwnerOfTheAccount() {
        Long accountNumber = 333908555109438L;
        List<Account> accounts = new ArrayList<>();
        Mockito.when(accountRepository.findByPersonId("102047585")).thenReturn(accounts);
        ApiException apiException = assertThrows(ApiException.class,
                () -> accountService.isLoggedInUserTheOwnerOfTheAccount(accountNumber, "102047585"));
        assertEquals(403, apiException.getStatusCode());
        assertEquals(List.of("El usuario '102047585' no cuenta con " +
                        "permisos para acceder a esta cuenta."),
                apiException.getErrors());
    }

    @Test
    void isLoggedInUserTheOwnerOfTheAccount() {
        Long accountNumber = 333908555109438L;
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        Account account = new Account(accountNumber, BigDecimal.valueOf(1000),
                person);
        List<Account> accounts = List.of(account);
        Mockito.when(accountRepository.findByPersonId("102047585")).thenReturn(accounts);
        assertDoesNotThrow(() -> accountService.isLoggedInUserTheOwnerOfTheAccount(accountNumber, "102047585"));

    }
}