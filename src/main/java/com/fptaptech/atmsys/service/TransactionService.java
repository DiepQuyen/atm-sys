package com.fptaptech.atmsys.service;

import com.fptaptech.atmsys.entity.Account;
import com.fptaptech.atmsys.entity.Transaction;
import com.fptaptech.atmsys.entity.TransactionType;
import com.fptaptech.atmsys.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Service xử lý các thao tác liên quan đến giao dịch
@Service// Đánh dấu đây là một service
public class TransactionService {
    // Tiêm các phụ thuộc vào để dùng
    @Autowired
    private TransactionRepository transactionRepository;

    // Lưu thông tin giao dịch vào database
    public void saveTransaction(Account account, TransactionType type, Double amount) {
        // Tạo mới một giao dịch
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType(type);
        transaction.setAmount(amount);
        // Lưu giao dịch vào database
        transactionRepository.save(transaction);
    }

    // Lấy ra danh sách các giao dịch của một tài khoản
    public List<Transaction> getTransactionsByAccountNumber(String accountNumber) {
        // Gọi phương thức của repository để lấy ra danh sách giao dịch
        return transactionRepository.findByAccount_AccountNumberOrderByCreateAtDesc(accountNumber);
    }
}