package com.fptaptech.atmsys.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id//Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Giá trị tự động tăng
    private Long id;

    @Column(name = "username",nullable = false, unique = true) //Không được null, duy nhất
    private String username;
    @Column(name = "password", nullable = false) //Không được null
    private String password;

    //Mapping to Account
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) //1 user có nhiều account
    private List<Account> accounts = new ArrayList<>();

    public User() {
    }

    public User(Long id, String username, String password, List<Account> accounts) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.accounts = accounts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
