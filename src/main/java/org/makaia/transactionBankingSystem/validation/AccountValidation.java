package org.makaia.transactionBankingSystem.validation;

import org.makaia.transactionBankingSystem.dto.dtoAccount.DTOAccountCreation;
import org.makaia.transactionBankingSystem.dto.dtoAccount.DTOAccountDeposit;
import org.makaia.transactionBankingSystem.dto.dtoAccount.DTOAccountTransfer;
import org.makaia.transactionBankingSystem.dto.dtoPerson.DTOPersonCreate;
import org.makaia.transactionBankingSystem.exception.ApiException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class AccountValidation {

    private List<String> error;
    public void isValidBalance(BigDecimal balance){
        if(balance == null){
            this.error.add("- Error al ingresar el saldo. Ingrese información no nula.");
        }
        else if(balance.compareTo(BigDecimal.valueOf(0)) < 0){
            this.error.add("- Error. El saldo inicial de la cuenta debe ser mayor o igual a $0.");
        }
    }

    public void isValidAmount(BigDecimal balance){
        if(balance == null){
            this.error.add("- Error al ingresar la cantidad. Ingrese información no nula.");
        }
        else if(balance.compareTo(BigDecimal.valueOf(2000)) < 0){
            this.error.add("- Error. La cantidad a transferir debe ser mayor o igual a $2000.");
        }
    }

    public void isValidOwner(DTOPersonCreate owner){
        if(owner == null){
            this.error.add("- Error al ingresar el propietario. Ingrese información no nula.");
        }
    }

    public void isValidAccountNumber(String accountNumber){
        String regx = "^([0-9]{15})$";
        Pattern pattern = Pattern.compile(regx);
        if(accountNumber == null){
            this.error.add("- Error al ingresar el numero de cuenta. Ingrese información no nula.");
        }
        else if(!pattern.matcher(accountNumber).matches()){
            this.error.add("- Error al ingresar el numero de la cuenta. Ingrese un valor válido: número" +
                    " de 15 dígitos.");
        }
    }

    public Boolean createAccountValidation(DTOAccountCreation dtoAccountCreation) throws ApiException {
        this.error = new ArrayList<>();
        isValidBalance(dtoAccountCreation.getInitialBalance());
        isValidOwner(dtoAccountCreation.getOwner());

        if(!this.error.isEmpty()){
            throw new ApiException(400, this.error);
        }
        return true;
    }

    public Boolean depositAccountValidation(Long accountNumber, DTOAccountDeposit dtoAccountDeposit) throws ApiException {
        this.error = new ArrayList<>();
        isValidAccountNumber(accountNumber.toString());
        isValidAmount(dtoAccountDeposit.getAmount());

        if(!this.error.isEmpty()){
            throw new ApiException(400, this.error);
        }
        return true;
    }

    public Boolean getAccountValidation(Long id) throws ApiException {
        this.error = new ArrayList<>();
        isValidAccountNumber(id.toString());
        if(!this.error.isEmpty()){
            throw new ApiException(400, this.error);
        }
        return true;
    }

    public Boolean transferAccountValidation(DTOAccountTransfer dtoAccountTransfer) throws ApiException {
        this.error = new ArrayList<>();
        isValidAccountNumber(dtoAccountTransfer.getSourceAccountNumber().toString());
        isValidAccountNumber(dtoAccountTransfer.getDestinationAccountNumber().toString());
        isValidAmount(dtoAccountTransfer.getAmount());

        if(!this.error.isEmpty()){
            throw new ApiException(400, this.error);
        }
        return true;
    }
}
