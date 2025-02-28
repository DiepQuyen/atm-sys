package com.fptaptech.atmsys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fptaptech.atmsys.entity.Account;
import com.fptaptech.atmsys.service.AccountService;

@Controller
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/deposit")
    public String deposit(@RequestParam String accountNumber, @RequestParam Double amount, Model model) {
        accountService.deposit(accountNumber, amount);
        model.addAttribute("message", "Deposit successful");
        return "result";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String accountNumber, @RequestParam Double amount, Model model) {
        accountService.withdraw(accountNumber, amount);
        model.addAttribute("message", "Withdrawal successful");
        return "result";
    }

    @PostMapping("/save")
    public String saveAmount(@RequestParam String accountNumber, @RequestParam Double amount, Model model) {
        accountService.saveAmount(accountNumber, amount);
        model.addAttribute("message", "Saving successful");
        return "result";
    }

    @GetMapping("/{accountNumber}")
    public String getAccount(@PathVariable String accountNumber, Model model) {
        Account account = accountService.getAccountByNumber(accountNumber);
        if (account == null) {
            model.addAttribute("message", "Account not found");
            return "error";
        }
        model.addAttribute("account", account);
        return "account";
    }
}