package org.makaia.transactionBankingSystem.validation;

import org.makaia.transactionBankingSystem.dto.dtoPerson.DTOPersonUpdate;
import org.makaia.transactionBankingSystem.exception.ApiException;
import org.makaia.transactionBankingSystem.model.Person;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class PersonValidation {

    private List<String> error;
    public void isValidName(String firstName, String lastName){
        String regx = "^[A-Za-z]+$";
        Pattern pattern = Pattern.compile(regx);
        if(firstName == null || lastName == null){
            this.error.add("- Error al ingresar el nombre y/o apellido. Ingrese información no nula.");
        }
        else if(!pattern.matcher(firstName).matches() || !pattern.matcher(lastName).matches()){
            this.error.add("- Error al ingresar el nombre y/o apellido. Ingrese información válida: " +
                    "solo letras.");
        }
    }

    public void isValidId(String id){
        String regx = "^([0-9]{6,10})$";
        Pattern pattern = Pattern.compile(regx);
        if(id == null){
            this.error.add("- Error al ingresar el documento de identidad. Ingrese información no nula.");
        }
        else if(!pattern.matcher(id).matches()){
            this.error.add("- Error al ingresar el documento de identidad. Ingrese un identificador válido: " +
                    "número con entre seis y diez dígitos.");
        }
    }

    public void isValidEmail(String email){
        String regx = "^[A-Za-z]+[A-Za-z0-9+_.-]*@[A-Za-z]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regx);
        if(email == null){
            this.error.add("- Error al ingresar el correo. Ingrese información no nula.");
        }
        else if(!pattern.matcher(email).matches()){
            this.error.add("- Error al ingresar el correo. Ingrese uno válido: con la estructura " +
                    "ejemplo@dominio.com.co");
        }
    }

    public void isValidPhone(String phone){
        String regx = "^\\++([0-9]{2})+-+([0-9]{10})$";
        Pattern pattern = Pattern.compile(regx);
        if(phone == null){
            this.error.add("- Error al ingresar el teléfono. Ingrese información no nula.");
        }
        else if(!pattern.matcher(phone).matches()){
            this.error.add("- Error al ingresar el teléfono. Ingrese uno válido: con la estructura " +
                    "+XX-XXXXXXXXXX");
        }
    }

    public Boolean createPersonValidation(Person person) throws ApiException {
        this.error = new ArrayList<>();
        isValidId(person.getId());
        isValidName(person.getFirstName(), person.getLastName());
        isValidEmail(person.getEmail());
        isValidPhone(person.getPhone());

        if(!this.error.isEmpty()){
            throw new ApiException(400, this.error);
        }
        return true;
    }

    public Boolean getPersonValidation(String id) throws ApiException {
        this.error = new ArrayList<>();
        isValidId(id);
        if(!this.error.isEmpty()){
            throw new ApiException(400, this.error);
        }
        return true;
    }

    public Boolean updatePersonValidation(DTOPersonUpdate person) throws ApiException {
        this.error = new ArrayList<>();
        isValidId(person.getId());
        isValidEmail(person.getEmail());
        isValidPhone(person.getPhone());

        if(!this.error.isEmpty()){
            throw new ApiException(400, this.error);
        }
        return true;
    }
}
