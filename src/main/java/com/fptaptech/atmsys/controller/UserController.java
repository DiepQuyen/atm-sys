package com.fptaptech.atmsys.controller;

import com.fptaptech.atmsys.entity.User;
import com.fptaptech.atmsys.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
// Controller xử lý các request từ trang user
@Controller// Đánh dấu đây là một controller
public class UserController {
    // Tiêm các phụ thuộc vào để dùng
    @Autowired
    private UserService userService;

    //hàm xử lý đăng ký
    @PostMapping("/register")
    public String registerUser(User user, HttpSession session, Model model) {
        try {
            // Gọi hàm registerUser từ UserService để đăng ký user
            User registeredUser = userService.registerUser(user);
            //Lưu username vào session
            session.setAttribute("username", registeredUser.getUsername());
            //Chuyển hướng sang trang list
            return "redirect:/list";
            //Nếu thông tin không hợp lệ thì hiển thị thông báo lỗi
        } catch (IllegalArgumentException e) {
            // Trả về trang register
            model.addAttribute("error", e.getMessage());
            // Trả về trang register
            return "register";
        }
    }

    //hàm xử lý đăng nhập
    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session, Model model) {
        try {
            // Gọi hàm loginUser từ UserService để kiểm tra thông tin đăng nhập
            User user = userService.loginUser(username, password);
            // Nếu thông tin đúng thì lưu username vào session
            session.setAttribute("username", user.getUsername());
            // Chuyển hướng sang trang list
            return "redirect:/list";
        } catch (IllegalArgumentException e) {
            // Nếu thông tin không đúng thì hiển thị thông báo lỗi
            model.addAttribute("error", e.getMessage());
            // Trả về trang login
            return "login";
        }
    }
    //hàm hiển thị trang đăng ký
    @GetMapping("/register")
    public String showRegisterPage(@RequestParam(required = false) String error, Model model) {
        // Truyền thông báo lỗi nếu có
        model.addAttribute("error", error);
        // Trả về trang register
        return "register";
    }

    //hàm hiển thị trang đăng nhập
    @GetMapping("/login")
    public String showLoginPage(@RequestParam(required = false) String error, Model model) {
        // Truyền thông báo lỗi nếu có
        model.addAttribute("error", error);
        // Trả về trang login
        return "login";
    }

    //hàm xử lý đăng xuất
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Xóa username khỏi session
        session.invalidate(); // Xóa toàn bộ session
        // Chuyển hướng về trang login
        return "redirect:/login";
    }
}