package org.makaia.transactionBankingSystem.dto.dtoPerson;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "DTOPersonCreate: Objeto de transferencia de datos para la" +
        " actualización de la información de una persona en el sistema, " +
        "contiene la información de la persona que puede actualizarse.")
public class DTOPersonUpdate {
    private String id;
    private String phone;
    private String email;

    public DTOPersonUpdate() {
    }

    public DTOPersonUpdate(String id, String phone, String email) {
        this.id = id;
        this.phone = phone;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
