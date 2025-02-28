package com.fptaptech.atmsys.repository;

import com.fptaptech.atmsys.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User,Long> {
}
