package org.makaia.transactionBankingSystem.dto.dtoPocket;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(title = "DTOPocketTransfer: Objeto de transferencia de datos para la" +
        " transferencia a un bolsillo, contiene la información de su nombre," +
        " la cuenta bancaria a la que está asociado y la cantidad a " +
        "transferir a él.")
public class DTOPocketTransfer {
    private Long accountNumber;
    private Long pocketNumber;
    private BigDecimal amount;

    public DTOPocketTransfer() {
    }

    public DTOPocketTransfer(Long accountNumber, Long pocketNumber, BigDecimal amount) {
        this.accountNumber = accountNumber;
        this.pocketNumber = pocketNumber;
        this.amount = amount;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getPocketNumber() {
        return pocketNumber;
    }

    public void setPocketNumber(Long pocketNumber) {
        this.pocketNumber = pocketNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
