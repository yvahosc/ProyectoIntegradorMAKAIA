package org.makaia.transactionBankingSystem.dto.dtoAccount;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(title = "DTOAccountDeposit: Objeto de transferencia de datos" +
        " para el depósito a una cuenta bancaria, contiene la información " +
        "sobre la cantidad a depositar en la cuenta bancaria.")
public class DTOAccountDeposit {
    private BigDecimal amount;

    public DTOAccountDeposit() {
    }

    public DTOAccountDeposit(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
