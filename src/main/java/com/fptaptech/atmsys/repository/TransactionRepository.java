package com.fptaptech.atmsys.repository;

import com.fptaptech.atmsys.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repository xử lý các thao tác với bảng transaction trong database
@Repository // Đánh dấu đây là một repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByAccount_AccountNumberOrderByCreateAtDesc(String accountNumber);
}