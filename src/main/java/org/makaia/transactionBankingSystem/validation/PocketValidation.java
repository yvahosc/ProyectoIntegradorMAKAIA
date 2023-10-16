package org.makaia.transactionBankingSystem.validation;

import org.makaia.transactionBankingSystem.dto.dtoPocket.DTOPocketCreation;
import org.makaia.transactionBankingSystem.dto.dtoPocket.DTOPocketTransfer;
import org.makaia.transactionBankingSystem.exception.ApiException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class PocketValidation {

    private List<String> error;

    public void isValidAmount(BigDecimal amount){
        if(amount == null){
            this.error.add("- Error al ingresar la cantidad. Ingrese información no nula.");
        }
        else if(amount.compareTo(BigDecimal.valueOf(0)) <= 0){
            this.error.add("- Error. La cantidad a transferir debe ser mayor a $0.");
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
    public void isValidName(String name){
        String regx = "^[A-Za-z]+$";
        Pattern pattern = Pattern.compile(regx);
        if(name == null){
            this.error.add("- Error al ingresar el nombre del bolsillo. Ingrese información no nula.");
        }
        else if(!pattern.matcher(name).matches()){
            this.error.add("- Error al ingresar el nombre del bolsillo. Ingrese información válida: " +
                    "solo letras.");
        }
    }

    public void isValidPocketNumber(String numberPocket){
        String regx = "^([0-9])+$";
        Pattern pattern = Pattern.compile(regx);
        if(numberPocket == null){
            this.error.add("- Error al ingresar el numero del bolsillo. Ingrese información no nula.");
        }
        else if(!pattern.matcher(numberPocket).matches()){
            this.error.add("- Error al ingresar el numero del bolsillo. Ingrese un valor válido: " +
                    "un número.");
        }
    }

    public boolean createPocketValidation(DTOPocketCreation dtoPocketCreation) throws ApiException {
        this.error = new ArrayList<>();
        isValidAccountNumber(dtoPocketCreation.getAccountNumber().toString());
        isValidName(dtoPocketCreation.getName());
        isValidAmount(dtoPocketCreation.getInitialBalance());

        if(!this.error.isEmpty()){
            throw new ApiException(400, this.error);
        }
        return true;
    }

    public boolean transferPocketValidation(DTOPocketTransfer dtoPocketTransfer) throws ApiException {
        this.error = new ArrayList<>();
        isValidPocketNumber(dtoPocketTransfer.getPocketNumber().toString());
        isValidAccountNumber(dtoPocketTransfer.getAccountNumber().toString());
        isValidAmount(dtoPocketTransfer.getAmount());
        if(!this.error.isEmpty()){
            throw new ApiException(400, this.error);
        }
        return true;
    }
}
