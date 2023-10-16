package org.makaia.transactionBankingSystem.repository;

import org.makaia.transactionBankingSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByPersonId(String userName);
}
