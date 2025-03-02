package com.fptaptech.atmsys.controller;

import com.fptaptech.atmsys.entity.Account;
import com.fptaptech.atmsys.entity.User;
import com.fptaptech.atmsys.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

// Controller xử lý các request từ trang list
@Controller// Đánh dấu đây là một controller
public class ListController {
    // Tiêm các phụ thuộc vào để dùng
    @Autowired
    private UserService userService;

    // Hiển thị trang list
    @GetMapping("/list")
    public String showListPage(HttpSession session, Model model) {
        // Lấy ra username từ session
        String username = (String) session.getAttribute("username");
        // Nếu chưa đăng nhập thì trả về trang login
        if(username == null){
            // Trả về trang login
            return "redirect:/login";
        }
        // Lấy ra thông tin user từ username
        User user = userService.findByUsername(username);
        // Lấy ra danh sách tài khoản của user
        List<Account> accounts = user.getAccounts();
        // Đưa thông tin user và danh sách tài khoản vào model để hiển thị lên view
        model.addAttribute("username", username);
        model.addAttribute("accounts", accounts);
        // Trả về view list.html
        return "list";
    }
}