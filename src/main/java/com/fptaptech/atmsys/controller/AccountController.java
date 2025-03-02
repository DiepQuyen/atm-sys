// File: src/main/java/com/fptaptech/atmsys/controller/AccountController.java
package com.fptaptech.atmsys.controller;

import com.fptaptech.atmsys.entity.Account;
import com.fptaptech.atmsys.entity.Transaction;
import com.fptaptech.atmsys.service.AccountService;
import com.fptaptech.atmsys.service.TransactionService;
import com.fptaptech.atmsys.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// Controller xử lý các request từ trang account
@Controller// Đánh dấu đây là một controller
@RequestMapping("/api/accounts") // Đường dẫn mà controller xử lý
public class AccountController {
    // Tiêm các phụ thuộc vào để dùng
    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    // Hiển thị form tạo tài khoản
    @GetMapping("/create-account")
    public String showCreateAccountPage() {
        //trả về form tạo tài khoản là file create-account.html
        return "create-account";
    }

    // Xem lịch sử giao dịch của tài khoản
    @GetMapping("/transactions/{accountNumber}")
    public String viewTransactions(@PathVariable String accountNumber, Model model) {
        // Lấy ra danh sách các giao dịch của tài khoản
        List<Transaction> transactions = transactionService.getTransactionsByAccountNumber(accountNumber);
        // Đưa danh sách giao dịch vào model để hiển thị lên view
        model.addAttribute("transactions", transactions);
        model.addAttribute("accountNumber", accountNumber);
        // Trả về view transaction-history.html
        return "transaction-history";
    }

    // Xử lý tạo tài khoản mới, nhận vào 2 param là accountNumber và balance
    @PostMapping("/create")
    public String createAccount(@RequestParam String accountNumber,
                                @RequestParam Double balance,
                                Model model, HttpSession session) {
        // Lấy ra username từ session đăng nhập
        String username = (String) session.getAttribute("username");
        // Nếu chưa đăng nhập thì trả về trang login
        if (username == null) {
            // Thông báo lỗi
            model.addAttribute("error", "Please log in first.");
            // Trả về trang login
            return "login";
        }
        // Tạo tài khoản mới
        accountService.createAccount(accountNumber, balance, userService.findByUsername(username).getId());
        // Thông báo tạo tài khoản thành công
        return "redirect:/list";
    }

    // Hiển thị form nạp tiền
    @GetMapping("/deposit-form")
    public String showDepositForm(@RequestParam String accountNumber, Model model) {
        // Truyền số tài khoản vào model để hiển thị lên view
        model.addAttribute("accountNumber", accountNumber);
        // Trả về view deposit.html
        return "deposit";
    }

    // Hiển thị form rút tiền
    @GetMapping("/withdraw-form")
    public String showWithdrawForm(@RequestParam String accountNumber, Model model) {
        // Truyền số tài khoản vào model để hiển thị lên view
        model.addAttribute("accountNumber", accountNumber);
        return "withdraw";
    }

    // Nạp tiền vào tài khoản
    @PostMapping("/deposit")
    public String deposit(@RequestParam String accountNumber, @RequestParam Double amount, Model model) {
        // Gọi hàm deposit từ AccountService để thực hiện nạp tiền
        accountService.deposit(accountNumber, amount);
        // Thông báo nạp tiền thành công
        model.addAttribute("message", "Deposit successful");
        // Chuyển hướng về trang list
        return "redirect:/list";
    }

    // Rút tiền khỏi tài khoản
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String accountNumber, @RequestParam Double amount, Model model) {
        // Gọi hàm withdraw từ AccountService để thực hiện rút tiền
        accountService.withdraw(accountNumber, amount);
        // Thông báo rút tiền thành công
        model.addAttribute("message", "Withdrawal successful");
        // Chuyển hướng về trang list
        return "redirect:/list";
    }

    // Xem thông tin tài khoản
    @GetMapping("/{accountNumber}")
    public String getAccount(@PathVariable String accountNumber, Model model) {
        // Gọi hàm getAccountByNumber từ AccountService để lấy thông tin tài khoản
        Account account = accountService.getAccountByNumber(accountNumber);
        // Nếu không tìm thấy tài khoản thì hiển thị thông báo lỗi
        if (account == null) {
            // Truyền thông báo lỗi vào model để hiển thị lên view
            model.addAttribute("message", "Account not found");
            // Trả về trang error.html
            return "error";
        }
        // Truyền thông tin tài khoản vào model để hiển thị lên view
        model.addAttribute("account", account);
        // Trả về view account.html
        return "account";
    }

    // Hiển thị form chuyển tiền
    @GetMapping("/transfer-form")
    public String showTransferForm(@RequestParam String accountNumber, Model model) {
        // Truyền số tài khoản vào model để hiển thị lên view
        model.addAttribute("fromAccountNumber", accountNumber);
        // Trả về view transfer.html
        return "transfer";
    }

    // Chuyển tiền từ tài khoản này sang tài khoản khác
    @PostMapping("/transfer")
    public String transfer(@RequestParam String fromAccountNumber,
                           @RequestParam String toAccountNumber,
                           @RequestParam Double amount) {
        // Gọi hàm transfer từ AccountService để thực hiện chuyển tiền
        accountService.transfer(fromAccountNumber, toAccountNumber, amount);
        //Xong thì chuyển về trang list
        return "redirect:/list";
    }

    // Hiển thị form rút tiền tiết kiệm
    @GetMapping("/saving-form")
    public String showSavingForm(@RequestParam String accountNumber, Model model) {
        // Truyền số tài khoản vào model để hiển thị lên view
        model.addAttribute("accountNumber", accountNumber);
        // Trả về view saving.html
        return "saving";
    }

    // Tiết kiệm tiền vào tài khoản
    @PostMapping("/saving")
    public String saving(@RequestParam String accountNumber,
                         @RequestParam Double amount) {
        // Gọi hàm saving từ AccountService để thực hiện tiết kiệm
        accountService.saving(accountNumber, amount);
        // Xong thì chuyển về trang list
        return "redirect:/list";
    }


    // Hiển thị form rút tiền tiết kiệm
    @GetMapping("/withdraw-saving-form")
    public String showWithdrawSavingForm(@RequestParam String accountNumber, Model model) {
        // Truyền số tài khoản vào model để hiển thị lên view
        Double savingBalance = accountService.getSavingBalance(accountNumber);
        model.addAttribute("accountNumber", accountNumber);
        // Truyền số dư tiết kiệm vào model để hiển thị lên view
        model.addAttribute("savingBalance", savingBalance);
        // Trả về view withdraw-saving.html
        return "withdraw-saving";
    }

    // Rút tiền tiết kiệm khỏi tài khoản
    @PostMapping("/withdraw-saving")
    public String withdrawSaving(@RequestParam String accountNumber,
                                 @RequestParam Double amount,
                                 Model model) {
        try {
            // Gọi hàm withdrawSaving từ AccountService để thực hiện rút tiền tiết kiệm
            accountService.withdrawSaving(accountNumber, amount);
            // Nếu không có lỗi thì chuyển về trang list
            return "redirect:/list";
        } catch (IllegalArgumentException e) {
            // Nếu có lỗi thì hiển thị thông báo lỗi
            model.addAttribute("error", e.getMessage());
            // Truyền số tài khoản vào model để hiển thị lên view
            model.addAttribute("accountNumber", accountNumber);
            // Truyền số dư tiết kiệm vào model để hiển thị lên view
            model.addAttribute("savingBalance", accountService.getSavingBalance(accountNumber));
            // Trả về view withdraw-saving.html
            return "withdraw-saving";
        }
    }
}