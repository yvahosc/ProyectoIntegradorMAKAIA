package org.makaia.transactionBankingSystem.dto.dtoPocket;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(title = "DTOPocketConsultIn: Objeto de transferencia de datos para la" +
        " consulta de un bolsillo, contiene la información de su nombre y la " +
        "cantidad guardada en él.")
public class DTOPocketConsultIn {
    private Long numberPocket;
    private String name;
    private BigDecimal amount;

    public DTOPocketConsultIn() {
    }

    public DTOPocketConsultIn(Long numberPocket, String name, BigDecimal amount) {
        this.numberPocket = numberPocket;
        this.name = name;
        this.amount = amount;
    }

    public Long getNumberPocket() {
        return numberPocket;
    }

    public void setNumberPocket(Long numberPocket) {
        this.numberPocket = numberPocket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
