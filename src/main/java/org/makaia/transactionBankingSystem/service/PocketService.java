package org.makaia.transactionBankingSystem.service;

import org.makaia.transactionBankingSystem.dto.dtoPocket.DTOPocketConsultIn;
import org.makaia.transactionBankingSystem.dto.dtoPocket.DTOPocketConsultOut;
import org.makaia.transactionBankingSystem.dto.dtoPocket.DTOPocketCreation;
import org.makaia.transactionBankingSystem.dto.dtoPocket.DTOPocketTransfer;
import org.makaia.transactionBankingSystem.exception.ApiException;
import org.makaia.transactionBankingSystem.model.Account;
import org.makaia.transactionBankingSystem.model.Pocket;
import org.makaia.transactionBankingSystem.repository.PocketRepository;
import org.makaia.transactionBankingSystem.validation.PocketValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PocketService {

    PocketRepository pocketRepository;
    PocketValidation pocketValidation;
    AccountService accountService;


    @Autowired
    public PocketService(PocketRepository pocketRepository, PocketValidation pocketValidation
                         , @Lazy AccountService accountService)
    {
        this.pocketRepository = pocketRepository;
        this.pocketValidation = pocketValidation;
        this.accountService = accountService;
    }

    public DTOPocketConsultOut getPocketsByAccountNumber(Long accountNumber) {
        List<Pocket> pockets = this.pocketRepository.findByAccountAccountNumber(accountNumber);

        List<DTOPocketConsultIn> pocketsDTO = new ArrayList<>();
        for (Pocket pocket: pockets){
            pocketsDTO.add(new DTOPocketConsultIn(pocket.getNumberPocket(), pocket.getName(), pocket.getAmount()));
        }
        return new DTOPocketConsultOut(pocketsDTO);
    }

    public DTOPocketCreation createPocket(DTOPocketCreation dtoPocketCreation) throws ApiException {

        pocketValidation.createPocketValidation(dtoPocketCreation);

        Account existingAccount = getAccountById(dtoPocketCreation.getAccountNumber());
        if(existingAccount == null){
            List<String> error = List.of("La cuenta con numero '" + dtoPocketCreation.getAccountNumber() + "' no existe en la base de datos.");
            throw new ApiException (404, error);
        }else{
            if(isThereEnoughBalance(existingAccount, dtoPocketCreation.getInitialBalance())){
                Pocket pocket = new Pocket(dtoPocketCreation.getName(), dtoPocketCreation.getInitialBalance(), existingAccount);
                this.pocketRepository.save(pocket);
                return dtoPocketCreation;}
            else{
                List<String> error = List.of("La cuenta con numero '" + dtoPocketCreation.getAccountNumber() +
                        "' no tiene suficientes fondos disponibles para enviar al bolsillo.");
                throw new ApiException (400, error);
            }
        }
    }

    public DTOPocketTransfer transferToPocket(DTOPocketTransfer dtoPocketTransfer) throws ApiException {
        pocketValidation.transferPocketValidation(dtoPocketTransfer);
        Account existingAccount = getAccountById(dtoPocketTransfer.getAccountNumber());
        Pocket existingPocket = getPocketByIdByAccount(dtoPocketTransfer.getPocketNumber(), dtoPocketTransfer.getAccountNumber());

        if(existingAccount == null){
            List<String> error = List.of("La cuenta con numero '" + dtoPocketTransfer.getAccountNumber() +
                    "' no existe en la base de datos.");
            throw new ApiException (404, error);
        } else if(existingPocket == null){
            List<String> error = List.of("El bolsillo con numero '" + dtoPocketTransfer.getPocketNumber() +
                    "' correspondiente a la cuenta '" + dtoPocketTransfer.getAccountNumber() + "' no existe en la base de datos.");
            throw new ApiException (404, error);
        } else{
            if(isThereEnoughBalance(existingAccount, dtoPocketTransfer.getAmount())){
                existingPocket.setAmount(existingPocket.getAmount().add(dtoPocketTransfer.getAmount()));
                this.pocketRepository.save(existingPocket);
                dtoPocketTransfer.setAmount(existingPocket.getAmount());
                return dtoPocketTransfer;}
            else{
                List<String> error = List.of("La cuenta con numero '" + dtoPocketTransfer.getAccountNumber() + "' no tiene suficientes fondos disponibles para enviar al bolsillo.");
                throw new ApiException (400, error);
            }
        }
    }

    private Pocket getPocketByIdByAccount(Long pocketNumber, Long accountNumber) {
        return this.pocketRepository.findByNumberPocketAndAccountAccountNumber(pocketNumber, accountNumber);
    }

    private boolean isThereEnoughBalance(Account account, BigDecimal amountToMoveToPocket) {
        return account.getBalance().add(balanceInPocketsOfOneAccount(account.getAccountNumber()).negate()).compareTo(amountToMoveToPocket) >= 0;
    }

    public Account getAccountById (Long id){
        try{
            return accountService.getAccountById(id);
        } catch(ApiException e){
            return null;
        }
    }

    public BigDecimal balanceInPocketsOfOneAccount(Long accountNumber){

        DTOPocketConsultOut pocketsInAccount = getPocketsByAccountNumber(accountNumber);
        BigDecimal sumAmountInPockets = BigDecimal.valueOf(0);
        if(!pocketsInAccount.getPockets().isEmpty()){
            for (DTOPocketConsultIn pocket: pocketsInAccount.getPockets()){
                BigDecimal amountInPocket = pocket.getAmount();
                sumAmountInPockets = sumAmountInPockets.add(amountInPocket);
            }
        }
        return sumAmountInPockets;
    }
}
