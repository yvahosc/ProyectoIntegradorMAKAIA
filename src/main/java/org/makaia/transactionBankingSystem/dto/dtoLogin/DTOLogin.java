package org.makaia.transactionBankingSystem.dto.dtoLogin;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "DTOLogin: Objeto de transferencia de datos" +
        " para realizar el ingreso al sistema por medio de usuario y " +
        "contraseña.")
public class DTOLogin {
    private String userName;
    private String password;

    public DTOLogin() {
    }

    public DTOLogin(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
