package com.fptaptech.atmsys.repository;

import com.fptaptech.atmsys.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository xử lý các thao tác với bảng user trong database
@Repository// Đánh dấu đây là một repository
public interface UserRepository  extends JpaRepository<User,Long> {
    User findByUsername(String username);
}
