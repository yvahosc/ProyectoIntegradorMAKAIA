package org.makaia.transactionBankingSystem.model;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "account")
@Schema(title = "Account: Entidad para el almacenamiento de información de " +
        "una cuenta bancaria en el sistema, contiene la información completa " +
        "de la cuenta bancaria.")
public class Account {

    @Id
    @Column(name = "accountNumber", length = 15)
    private Long accountNumber;
    @Column(name = "balance", nullable = false, scale = 2)
    private BigDecimal balance;

    @ManyToOne()
    private Person person;

    @OneToMany(mappedBy = "account" , cascade = CascadeType.ALL)
    private List<Pocket> pockets;


    public Account() {

    }

    public Account(Long accountNumber, BigDecimal balance, Person person) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.person = person;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}
