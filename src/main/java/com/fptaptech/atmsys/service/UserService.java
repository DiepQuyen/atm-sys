package com.fptaptech.atmsys.service;

import com.fptaptech.atmsys.entity.User;
import com.fptaptech.atmsys.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Service xử lý các thao tác liên quan đến user
@Service// Đánh dấu đây là một service
public class UserService {
    // Tiêm các phụ thuộc vào để dùng
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Tìm user theo username
    public User findByUsername(String username) {
        // Gọi phương thức của repository để lấy ra user theo username
        return userRepository.findByUsername(username);
    }


    // Đăng ký user mới
    @Transactional
    public User registerUser(User user) {
        // Kiểm tra xem username đã tồn tại chưa
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        // Mã hóa mật khẩu trước khi lưu vào database
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Lưu user vào database
        return userRepository.save(user);
    }

    // Kiểm tra thông tin đăng nhập
    @Transactional
    public User loginUser(String username, String password) {
        // Tìm user theo username
        User user = findByUsername(username);
        // Kiểm tra mật khẩu
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // Trả về user nếu thông tin đúng
            return user;
        }
        // Ném ra ngoại lệ nếu thông tin không đúng
        throw new IllegalArgumentException("Invalid credentials");
    }
}