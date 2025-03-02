package com.fptaptech.atmsys.service;

import com.fptaptech.atmsys.entity.Account;
import com.fptaptech.atmsys.entity.Transaction;
import com.fptaptech.atmsys.entity.TransactionType;
import com.fptaptech.atmsys.entity.User;
import com.fptaptech.atmsys.repository.AccountRepository;
import com.fptaptech.atmsys.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


// Service xử lý các thao tác liên quan đến tài khoản
@Service// Đánh dấu đây là một service
public class AccountService {
    // Tiêm các phụ thuộc vào để dùng
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionService transactionService;

    // Tạo tài khoản mới
    @Transactional // Đánh dấu phương thức này sẽ thực hiện trong một transaction
    public Account createAccount(String accountNumber, Double balance, Long userId) {
        // Lấy ra user từ id
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        // Tạo mới một tài khoản
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(balance);
        account.setUser(user);
        // Lưu tài khoản vào database
        return accountRepository.save(account);
    }

    // Nạp tiền vào tài khoản
    @Transactional
    public void deposit(String accountNumber, Double amount) {
        // Lấy ra tài khoản từ số tài khoản
        Account account = accountRepository.findByAccountNumber(accountNumber);
        // Nếu không tìm thấy tài khoản thì thông báo lỗi
        if (account == null) {
            // Ném ra một ngoại lệ nếu không tìm thấy tài khoản
            throw new IllegalArgumentException("Account not found");
        }
        // Cộng tiền vào tài khoản
        account.setBalance(account.getBalance() + amount);
        // Lưu tài khoản vào database
        accountRepository.save(account);
        // Lưu lịch sử giao dịch
        transactionService.saveTransaction(account, TransactionType.DEPOSIT, amount);
    }

    // Rút tiền từ tài khoản
    @Transactional
    public void withdraw(String accountNumber, Double amount) {
        // Lấy ra tài khoản từ số tài khoản
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            // Ném ra một ngoại lệ nếu không tìm thấy tài khoản
            throw new IllegalArgumentException("Account not found");
        }
        if (account.getBalance() < amount) {
            // Ném ra một ngoại lệ nếu số dư không đủ
            throw new IllegalArgumentException("Insufficient balance");
        }
        // Trừ tiền từ tài khoản
        account.setBalance(account.getBalance() - amount);
        // Lưu tài khoản vào database
        accountRepository.save(account);
        // Lưu lịch sử giao dịch
        transactionService.saveTransaction(account, TransactionType.WITHDRAW, amount);
    }
    // Lấy ra tài khoản từ số tài khoản
    public Account getAccountByNumber(String accountNumber) {
        // Gọi hàm findByAccountNumber từ accountRepository để lấy ra tài khoản
        return accountRepository.findByAccountNumber(accountNumber);
    }

    // Chuyển tiền từ tài khoản này sang tài khoản khác
    @Transactional// Đánh dấu phương thức này sẽ thực hiện trong một transaction
    public void transfer(String fromAccountNumber, String toAccountNumber, Double amount) {
        // Lấy ra tài khoản người gửi
        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber);
        // Lấy ra tài khoản người nhận
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber);

        // Kiểm tra tài khoản người gửi và người nhận
        if (fromAccount == null || toAccount == null) {
            // Ném ra một ngoại lệ nếu không tìm thấy tài khoản
            throw new IllegalArgumentException("Số tài khoản không tồn tại");
        }

        // Kiểm tra số dư người gửi
        if (fromAccount.getBalance() < amount) {
            // Ném ra một ngoại lệ nếu số dư không đủ
            throw new IllegalArgumentException("Số dư không đủ");
        }

        // Trừ tiền từ tài khoản người gửi và cộng tiền vào tài khoản người nhận
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        // Cộng tiền vào tài khoản người nhận
        toAccount.setBalance(toAccount.getBalance() + amount);

        // Lưu tài khoản vào database
        accountRepository.save(fromAccount);
        // Lưu tài khoản vào database
        accountRepository.save(toAccount);

        //lưu giao dịch người gửi
        transactionService.saveTransaction(fromAccount, TransactionType.TRANSFER, amount);
        //lưu giao dịch người nhận
        transactionService.saveTransaction(toAccount, TransactionType.RECEIVE, amount);
    }

    // Tiết kiệm tiền vào tài khoản
    @Transactional
    public void saving(String accountNumber, Double amount) {
        // Lấy ra tài khoản từ số tài khoản
        Account account = accountRepository.findByAccountNumber(accountNumber);
        // Nếu không tìm thấy tài khoản thì thông báo lỗi
        if (account == null) {
            // Ném ra một ngoại lệ nếu không tìm thấy tài khoản
            throw new IllegalArgumentException("Tài khoản không tồn tại");
        }

        // Kiểm tra số dư
        if (account.getBalance() < amount) {
            // Ném ra một ngoại lệ nếu số dư không đủ
            throw new IllegalArgumentException("Số dư không đủ");
        }

        // Trừ tiền từ tài khoản chính
        account.setBalance(account.getBalance() - amount);
        // Lưu tài khoản vào database
        accountRepository.save(account);
        // Lưu lịch sử giao dịch
        transactionService.saveTransaction(account, TransactionType.SAVING, amount);
    }

    // Lấy ra số dư tiết kiệm
    public Double getSavingBalance(String accountNumber) {
        //Lấy danh sách giao dịch tiết kiệm
        List<Transaction> transactions = transactionService.getTransactionsByAccountNumber(accountNumber);
        //Tính số dư tiết kiệm
        Double savingBalance = 0.0;

        //Duyệt qua từng giao dịch
        for (Transaction transaction : transactions) {
            //Nếu là giao dịch tiết kiệm thì cộng tiền vào số dư tiết kiệm
            if (transaction.getType() == TransactionType.SAVING) {
                savingBalance += transaction.getAmount();
                //Nếu là giao dịch rút tiết kiệm thì trừ tiền vào số dư tiết kiệm
            } else if (transaction.getType() == TransactionType.WITHDRAW_SAVING) {
                savingBalance -= transaction.getAmount();
            }
        }
        //Trả về số dư tiết kiệm
        return savingBalance;
    }

    // Rút tiền tiết kiệm
    @Transactional
    public void withdrawSaving(String accountNumber, Double amount) {
        // Lấy ra tài khoản từ số tài khoản
        Account account = accountRepository.findByAccountNumber(accountNumber);
        // Nếu không tìm thấy tài khoản thì thông báo lỗi
        if (account == null) {
            throw new IllegalArgumentException("Tài khoản không tồn tại");
        }

        // Kiểm tra số dư tiết kiệm
        Double savingBalance = getSavingBalance(accountNumber);
        if (savingBalance < amount) {
            throw new IllegalArgumentException("Số tiền tiết kiệm không đủ. Số dư tiết kiệm: " + savingBalance);
        }

        // Cộng tiền vào tài khoản chính
        account.setBalance(account.getBalance() + amount);
        // Lưu tài khoản vào database
        accountRepository.save(account);

        // Lưu lịch sử giao dịch
        transactionService.saveTransaction(account, TransactionType.WITHDRAW_SAVING, amount);
    }
}