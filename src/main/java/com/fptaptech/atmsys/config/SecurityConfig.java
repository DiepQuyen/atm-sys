package com.fptaptech.atmsys.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration //Đánh dấu đây là class cấu hình
@EnableWebSecurity //Bật tính năng bảo mật
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() //Tất cả các request đều được phép truy cập
                );

        return http.build();
    }

    //hàm để mã hóa mật khẩu
    @Bean
    public PasswordEncoder passwordEncoder() {
        //Sử dụng BCrypt để mã hóa mật khẩu
        return new BCryptPasswordEncoder();
    }
}