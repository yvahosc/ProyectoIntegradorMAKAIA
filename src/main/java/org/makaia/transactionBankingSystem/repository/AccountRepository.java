package org.makaia.transactionBankingSystem.repository;

import org.makaia.transactionBankingSystem.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByPersonId(String personId);
}
