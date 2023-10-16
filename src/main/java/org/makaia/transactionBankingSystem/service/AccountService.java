package org.makaia.transactionBankingSystem.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    AccountRepository accountRepository;
    AccountValidation accountValidation;
    PersonValidation personValidation;
    PersonService personService;
    PocketService pocketService;

    @Autowired
    public AccountService(AccountRepository accountRepository, AccountValidation accountValidation, PersonValidation personValidation,
                          PersonService personService, PocketService pocketService)
    {
        this.accountRepository = accountRepository;
        this.accountValidation = accountValidation;
        this.personValidation = personValidation;
        this.personService = personService;
        this.pocketService = pocketService;
    }

    public Account getAccountById(Long id) throws ApiException {
        accountValidation.getAccountValidation(id);
        Optional<Account> account = this.accountRepository.findById(id);
        if (account.isEmpty()){
            List<String> error = List.of("La cuenta con id '" + id + "' no se encuentra en la base de datos.");
            throw new ApiException (400, error);
        }
        return account.get();
    }


    public Account createAccount(DTOAccountCreation dtoAccountCreation) throws ApiException {

        accountValidation.createAccountValidation(dtoAccountCreation);

        personValidation.createPersonValidation(dtoAccountCreation.getOwner().getPerson());

        Person existingPerson = getPersonById(dtoAccountCreation.getOwner().getPerson().getId());

        if(existingPerson != null){
            Account account = new Account(Math.round(100000000000000L + Math.random() * 999999999999999L),
                    dtoAccountCreation.getInitialBalance(), dtoAccountCreation.getOwner().getPerson());
            return this.accountRepository.save(account);
        }else{
            Person createdPerson = createPerson(dtoAccountCreation.getOwner());
            Account account = new Account(Math.round(100000000000000L + Math.random() * 999999999999999L),
                    dtoAccountCreation.getInitialBalance(), createdPerson);
            return this.accountRepository.save(account);
        }
    }


    public Account depositInAccount(Long accountNumber, DTOAccountDeposit dtoAccountDeposit) throws
            ApiException
    {
        accountValidation.depositAccountValidation(accountNumber, dtoAccountDeposit);

        Account existingAccount = getAccountById(accountNumber);
        existingAccount.setBalance(existingAccount.getBalance().add(dtoAccountDeposit.getAmount()));
        return this.accountRepository.save(existingAccount);
    }


    public DTOAccountTransfer transferToAccount(DTOAccountTransfer dtoAccountTransfer) throws
            ApiException
    {
        accountValidation.transferAccountValidation(dtoAccountTransfer);
        Account existingSourceAccount = getAccountById(dtoAccountTransfer.getSourceAccountNumber());
        Account existingDestinationAccount = getAccountById(dtoAccountTransfer.getDestinationAccountNumber());

        if(existingDestinationAccount.equals(existingSourceAccount)){
            List<String> error = List.of("Las cuentas de origen y destino no pueden ser iguales.");
            throw new ApiException (400, error);
        }

        Boolean isThereEnoughBalance =
                isThereEnoughBalance(existingSourceAccount,
                dtoAccountTransfer.getAmount());
        if(isThereEnoughBalance){
            existingSourceAccount.setBalance(existingSourceAccount.getBalance().add(dtoAccountTransfer.getAmount().
                    negate()));
            existingDestinationAccount.setBalance(existingDestinationAccount.getBalance().add(dtoAccountTransfer.
                    getAmount()));
            this.accountRepository.save(existingSourceAccount);
            this.accountRepository.save(existingDestinationAccount);
            return dtoAccountTransfer;
        } else{
            List<String> error = List.of("La cuenta con numero '" + dtoAccountTransfer.getSourceAccountNumber()
                    + "' no tiene suficientes fondos disponibles para realizar la transferencia.");
            throw new ApiException (400, error);
        }

    }

    public Person getPersonById(String id) {
        try{
            return personService.getPersonById(id);
        } catch(ApiException e){
            return null;
        }
    }

    public Person createPerson(DTOPersonCreate dtoPersonCreate) throws ApiException {
        return personService.createPerson(dtoPersonCreate);
    }

    public void isLoggedInUserTheOwnerOfTheAccount (Long accountNumber,
                                                    String ownerId) throws ApiException {

        List<Account> accounts = accountRepository.findByPersonId(ownerId);
        boolean isTheOwner = false;
        for(Account account: accounts){
            if(accountNumber.equals(account.getAccountNumber())){
                isTheOwner = true;
                break;
            }
        }

        if(!isTheOwner){
            throw new ApiException (403,
                    List.of("El usuario '" + ownerId + "' no cuenta con " +
                            "permisos para acceder a esta cuenta."));
        }
    }

    public List<DTOPocketConsultIn> getPocketsByAccountNumber(Long accountNumber) throws ApiException {
        Account existingAccount = getAccountById(accountNumber);
        DTOPocketConsultOut pockets = pocketService.getPocketsByAccountNumber(existingAccount.getAccountNumber());
        if(pockets.getPockets().isEmpty()){
            List<String> error = List.of("La cuenta con numero '" + accountNumber
                    + "' no tiene bolsillos asociados.");
            throw new ApiException (400, error);
        }
        return pockets.getPockets();
    }

    public BigDecimal balanceInPocketsOfOneAccount(Long accountNumber){
        BigDecimal sumAmountInPockets = BigDecimal.valueOf(0);
        try{
            DTOPocketConsultOut pocketsInAccount = new DTOPocketConsultOut(getPocketsByAccountNumber(accountNumber));
            if(!pocketsInAccount.getPockets().isEmpty()){
                for (DTOPocketConsultIn pocket: pocketsInAccount.getPockets()){
                    BigDecimal amountInPocket = pocket.getAmount();
                    sumAmountInPockets = sumAmountInPockets.add(amountInPocket);
                }
            }
        } catch (NullPointerException | ApiException e) {
            sumAmountInPockets = BigDecimal.valueOf(0);
        }
        return sumAmountInPockets;
    }

    public Boolean isThereEnoughBalance(Account account,
                                        BigDecimal amountToTransfer) {
        return account.getBalance().add(balanceInPocketsOfOneAccount(account.getAccountNumber()).negate())
                .compareTo(amountToTransfer) >= 0;
    }
}
