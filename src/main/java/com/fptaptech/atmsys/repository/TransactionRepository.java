package com.fptaptech.atmsys.repository;

import com.fptaptech.atmsys.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
}
