package org.makaia.transactionBankingSystem.service;

import org.junit.jupiter.api.Test;
import org.makaia.transactionBankingSystem.dto.dtoPocket.DTOPocketCreation;
import org.makaia.transactionBankingSystem.dto.dtoPocket.DTOPocketTransfer;
import org.makaia.transactionBankingSystem.exception.ApiException;
import org.makaia.transactionBankingSystem.model.Account;
import org.makaia.transactionBankingSystem.model.Person;
import org.makaia.transactionBankingSystem.model.Pocket;
import org.makaia.transactionBankingSystem.repository.PocketRepository;
import org.makaia.transactionBankingSystem.validation.PocketValidation;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PocketServiceTest {

    PocketRepository pocketRepository = Mockito.mock(PocketRepository.class);
    PocketValidation pocketValidation = Mockito.mock(PocketValidation.class);
    AccountService accountService = Mockito.mock(AccountService.class);

    PocketService pocketService = new PocketService(pocketRepository,
            pocketValidation, accountService);

    @Test
    void getPocketsByAccountNumber() {
        Long accountNumber = 333908555109438L;
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        Account account = new Account(accountNumber, BigDecimal.valueOf(1000),
                person);
        Pocket pocket = new Pocket("Vacaciones", BigDecimal.valueOf(1000), account);
        List<Pocket> pockets = List.of(pocket);
        Mockito.when(pocketRepository.findByAccountAccountNumber(accountNumber)).thenReturn(pockets);

        assertEquals(1,
                pocketService.getPocketsByAccountNumber(accountNumber).getPockets().size());
    }

    @Test
    void createPocketWithNonExistingAccount() throws ApiException {
        Long accountNumber = 333908555109438L;
        DTOPocketCreation dtoPocketCreation =
                new DTOPocketCreation(accountNumber, "Vacaciones",
                        BigDecimal.valueOf(1000));

        Mockito.when(pocketValidation.createPocketValidation(dtoPocketCreation)).thenReturn(true);
        Mockito.when(accountService.getAccountById(accountNumber)).thenReturn(null);
        ApiException apiException = assertThrows(ApiException.class,
                () -> pocketService.createPocket(dtoPocketCreation));
        assertEquals(404, apiException.getStatusCode());
        assertEquals(List.of("La cuenta con numero '" + dtoPocketCreation.getAccountNumber() +
                        "' no existe en la base de datos."),
                apiException.getErrors());
    }

    @Test
    void createPocketWithExistingAccountAndEnoughBalance() throws ApiException {
        Long accountNumber = 333908555109438L;
        DTOPocketCreation dtoPocketCreation =
                new DTOPocketCreation(accountNumber, "Vacaciones",
                        BigDecimal.valueOf(1000));
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        Account account = new Account(accountNumber, BigDecimal.valueOf(1000),
                person);
        Mockito.when(accountService.getAccountById(accountNumber)).thenReturn(account);
        List<Pocket> pockets = new ArrayList<>();
        Mockito.when(pocketRepository.findByAccountAccountNumber(accountNumber)).thenReturn(pockets);
        Pocket existingPocket = new Pocket(dtoPocketCreation.getName(),
                dtoPocketCreation.getInitialBalance(), account);
        Mockito.when(pocketRepository.save(existingPocket)).thenReturn(existingPocket);
        assertDoesNotThrow(() -> pocketService.createPocket(dtoPocketCreation));
    }

    @Test
    void createPocketWithExistingAccountAndNonEnoughBalance() throws ApiException {
        Long accountNumber = 333908555109438L;
        DTOPocketCreation dtoPocketCreation =
                new DTOPocketCreation(accountNumber, "Vacaciones",
                        BigDecimal.valueOf(1000));
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        Account account = new Account(accountNumber, BigDecimal.valueOf(1000),
                person);
        Mockito.when(accountService.getAccountById(accountNumber)).thenReturn(account);
        Pocket pocket = new Pocket("Arriendo", BigDecimal.valueOf(1000),
                account);
        List<Pocket> pockets = List.of(pocket);
        Mockito.when(pocketRepository.findByAccountAccountNumber(accountNumber)).thenReturn(pockets);
        Pocket existingPocket = new Pocket(dtoPocketCreation.getName(),
                dtoPocketCreation.getInitialBalance(), account);
        Mockito.when(pocketRepository.save(existingPocket)).thenReturn(existingPocket);
        ApiException apiException = assertThrows(ApiException.class,
                () -> pocketService.createPocket(dtoPocketCreation));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("La cuenta con numero '" + dtoPocketCreation.getAccountNumber() +
                        "' no tiene suficientes fondos disponibles para enviar al bolsillo."),
                apiException.getErrors());
    }

    @Test
    void transferToPocketWithNonExistingAccount() throws ApiException {
        Long accountNumber = 333908555109438L;
        DTOPocketTransfer dtoPocketTransfer =
                new DTOPocketTransfer(accountNumber, 1L,
                        BigDecimal.valueOf(1000));

        Mockito.when(pocketValidation.transferPocketValidation(dtoPocketTransfer)).thenReturn(true);
        Mockito.when(accountService.getAccountById(accountNumber)).thenReturn(null);
        ApiException apiException = assertThrows(ApiException.class,
                () -> pocketService.transferToPocket(dtoPocketTransfer));
        assertEquals(404, apiException.getStatusCode());
        assertEquals(List.of("La cuenta con numero '" + dtoPocketTransfer.getAccountNumber() +
                        "' no existe en la base de datos."),
                apiException.getErrors());
    }

    @Test
    void transferToPocketWithExistingAccountAndNonExistingPocket() throws ApiException {
        Long accountNumber = 333908555109438L;
        DTOPocketTransfer dtoPocketTransfer =
                new DTOPocketTransfer(accountNumber, 1L,
                        BigDecimal.valueOf(1000));
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        Account account = new Account(accountNumber, BigDecimal.valueOf(1000),
                person);
        Mockito.when(accountService.getAccountById(accountNumber)).thenReturn(account);
        Mockito.when(pocketRepository.findByNumberPocketAndAccountAccountNumber(dtoPocketTransfer.getPocketNumber(), accountNumber)).thenReturn(null);
        ApiException apiException = assertThrows(ApiException.class,
                () -> pocketService.transferToPocket(dtoPocketTransfer));
        assertEquals(404, apiException.getStatusCode());
        assertEquals(List.of("El bolsillo con numero '" + dtoPocketTransfer.getPocketNumber() +
                        "' correspondiente a la cuenta '" + dtoPocketTransfer.getAccountNumber() + "' no existe en la base de datos."),
                apiException.getErrors());
    }

    @Test
    void transferToPocketWithExistingAccountExistingPocketAndNonEnoughBalance() throws ApiException {
        Long accountNumber = 333908555109438L;
        DTOPocketTransfer dtoPocketTransfer =
                new DTOPocketTransfer(accountNumber, 1L,
                        BigDecimal.valueOf(1000));
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        Account account = new Account(accountNumber, BigDecimal.valueOf(1000),
                person);
        Mockito.when(accountService.getAccountById(accountNumber)).thenReturn(account);
        Pocket pocket = new Pocket("Arriendo", BigDecimal.valueOf(1000),
                account);
        Pocket existingPocket = new Pocket("Vacaciones",
                BigDecimal.valueOf(1000),
                account);
        List<Pocket> pockets = List.of(existingPocket);
        Mockito.when(pocketRepository.findByNumberPocketAndAccountAccountNumber(dtoPocketTransfer.getPocketNumber(), accountNumber)).thenReturn(pocket);
        Mockito.when(pocketRepository.findByAccountAccountNumber(accountNumber)).thenReturn(pockets);
        ApiException apiException = assertThrows(ApiException.class,
                () -> pocketService.transferToPocket(dtoPocketTransfer));
        assertEquals(400, apiException.getStatusCode());
        assertEquals(List.of("La cuenta con numero '" + dtoPocketTransfer.getAccountNumber() +
                        "' no tiene suficientes fondos disponibles para enviar al bolsillo."),
                apiException.getErrors());
    }

    @Test
    void transferToPocketWithExistingAccountExistingPocketAndEnoughBalance() throws ApiException {
        Long accountNumber = 333908555109438L;
        DTOPocketTransfer dtoPocketTransfer =
                new DTOPocketTransfer(accountNumber, 1L,
                        BigDecimal.valueOf(1000));
        Person person = new Person("1020475585", "Yeisson", "Vahos", "+57" +
                "-3046406015", "yvahosc@gmail.com");
        Account account = new Account(accountNumber, BigDecimal.valueOf(1000),
                person);
        Mockito.when(accountService.getAccountById(accountNumber)).thenReturn(account);
        Pocket pocket = new Pocket("Arriendo", BigDecimal.valueOf(1000),
                account);
        List<Pocket> pockets = new ArrayList<>();
        Mockito.when(pocketRepository.findByNumberPocketAndAccountAccountNumber(dtoPocketTransfer.getPocketNumber(), accountNumber)).thenReturn(pocket);
        Mockito.when(pocketRepository.findByAccountAccountNumber(accountNumber)).thenReturn(pockets);
        assertDoesNotThrow(() -> pocketService.transferToPocket(dtoPocketTransfer));
    }

    @Test
    void getAccountByIdWithNonExistingId() throws ApiException {
        Long id = 1L;
        Mockito.when(accountService.getAccountById(id)).thenThrow(new
                ApiException(404, List.of("La cuenta con id '1' no se " +
                "encuentra en la base de datos.")));
        assertNull(pocketService.getAccountById(id));
    }
}