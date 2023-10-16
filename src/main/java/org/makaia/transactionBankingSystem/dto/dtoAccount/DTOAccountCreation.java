package org.makaia.transactionBankingSystem.dto.dtoAccount;

import io.swagger.v3.oas.annotations.media.Schema;
import org.makaia.transactionBankingSystem.dto.dtoPerson.DTOPersonCreate;

import java.math.BigDecimal;

@Schema(title = "DTOAccountCreation: Objeto de transferencia de datos" +
        " para la creación de una cuenta bancaria, contiene la información " +
        "del propietario y el saldo inicial de la cuenta.")
public class DTOAccountCreation {
    private DTOPersonCreate owner;
    private BigDecimal initialBalance;

    public DTOAccountCreation() {
    }

    public DTOAccountCreation(DTOPersonCreate owner, BigDecimal initialBalance) {
        this.owner = owner;
        this.initialBalance = initialBalance;
    }

    public DTOPersonCreate getOwner() {
        return owner;
    }

    public void setOwner(DTOPersonCreate owner) {
        this.owner = owner;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }
}
