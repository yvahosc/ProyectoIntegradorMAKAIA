package org.makaia.transactionBankingSystem.dto.dtoLogin;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "DTOToken: Objeto de transferencia de datos para visualizar " +
        "el token de autorización generado al ingresar al sistema con el " +
        "usuario y la contraseña.")
public class DTOToken {

    private String jwtToken;

    public DTOToken() {
    }

    public DTOToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
