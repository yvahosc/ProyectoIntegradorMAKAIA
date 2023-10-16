package org.makaia.transactionBankingSystem.dto.dtoPocket;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(title = "DTOPocketConsultOut: Objeto de transferencia de datos para la" +
        " visualización de la lista de bolsillos correspondientes a una misma" +
        " cuenta bancaria.")
public class DTOPocketConsultOut {
    private List<DTOPocketConsultIn> pockets;

    public DTOPocketConsultOut() {
    }

    public DTOPocketConsultOut(List<DTOPocketConsultIn> pockets) {
        this.pockets = pockets;
    }

    public List<DTOPocketConsultIn> getPockets() {
        return pockets;
    }

    public void setPockets(List<DTOPocketConsultIn> pockets) {
        this.pockets = pockets;
    }
}
