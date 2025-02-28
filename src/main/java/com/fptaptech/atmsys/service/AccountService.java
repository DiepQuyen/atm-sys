package com.fptaptech.atmsys.service;

//Triển khai biz trực tiếp của model

import com.fptaptech.atmsys.entity.Account;
import com.fptaptech.atmsys.entity.Transaction;
import com.fptaptech.atmsys.entity.TransactionType;
import com.fptaptech.atmsys.repository.AccountRepository;
import com.fptaptech.atmsys.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {
    //Tính chất DI của springboot:
    //Dependency Injection?
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public void deposit(String accountNumber, Double amount){
        Account account = accountRepository.findByAccountNumber(accountNumber);
        //check dk co hoac ko tim thay acc...tai day
        //ex: if(account == null)...
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);//luu tu Entity to db

        //Kiem soat bang dung Transactional annotation
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.DEPOSIT);
        transactionRepository.save(transaction);
    }
    @Transactional
    public void withdraw(String accountNumber, Double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.WITHDRAW);
        transactionRepository.save(transaction);
    }

    public Account getAccountByNumber(String accountNumber){
        return accountRepository.findByAccountNumber(accountNumber);
    }

    @Transactional
    public void saveAmount(String accountNumber, Double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.SAVING);
        transactionRepository.save(transaction);
    }


}
