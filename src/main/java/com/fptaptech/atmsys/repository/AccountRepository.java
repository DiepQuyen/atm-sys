package com.fptaptech.atmsys.repository;

import com.fptaptech.atmsys.entity.Account;
import com.fptaptech.atmsys.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {

   Account findByAccountNumber(String accountNumber);
}
