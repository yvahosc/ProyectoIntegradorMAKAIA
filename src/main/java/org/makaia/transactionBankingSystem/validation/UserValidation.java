package org.makaia.transactionBankingSystem.validation;

import org.makaia.transactionBankingSystem.exception.ApiException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class UserValidation {

    private List<String> error;

    public void isValidUserName(String userName){
        String regx = "^[A-Za-z]+$";
        Pattern pattern = Pattern.compile(regx);
        if(userName == null){
            this.error.add("- Error al ingresar el nombre de usuario. Ingrese información no nula.");
        }
        else if(!pattern.matcher(userName).matches()){
            this.error.add("- Error al ingresar el nombre de usuario. Ingrese información válida: " +
                    "solo letras.");
        }
    }
    public void isValidPassword(String password){
        String regx = "^([0-9]{4})$";
        Pattern pattern = Pattern.compile(regx);
        if(password == null){
            this.error.add("- Error al ingresar la contraseña. Ingrese información no nula.");
        }
        else if(!pattern.matcher(password).matches()){
           this.error.add("- Error al ingresar la contraseña. Ingrese uno válido: cuatro digitos.");
        }
    }

    public boolean getUserValidation(String userName) throws ApiException {
        this.error = new ArrayList<>();
        isValidUserName(userName);
        if(!this.error.isEmpty()){
            throw new ApiException(400, this.error);
        }
        return true;
    }

    public boolean createUserValidation(String password) throws ApiException {
        this.error = new ArrayList<>();
        isValidPassword(password);

        if(!this.error.isEmpty()){
            throw new ApiException(400, this.error);
        }
        return true;
    }
}
