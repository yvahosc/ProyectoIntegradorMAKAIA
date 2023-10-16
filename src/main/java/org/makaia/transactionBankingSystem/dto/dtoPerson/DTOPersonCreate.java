package org.makaia.transactionBankingSystem.dto.dtoPerson;

import io.swagger.v3.oas.annotations.media.Schema;
import org.makaia.transactionBankingSystem.model.Person;

@Schema(title = "DTOPersonCreate: Objeto de transferencia de datos para el" +
        " registro de una persona en el sistema, contiene la información " +
        "completa de la persona junto con la información de su contraseña " +
        "para ingresar al sistema.")
public class DTOPersonCreate {

    private Person person;
    private String password;

    public DTOPersonCreate() {
    }

    public DTOPersonCreate(Person person, String password) {
        this.person = person;
        this.password = password;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
