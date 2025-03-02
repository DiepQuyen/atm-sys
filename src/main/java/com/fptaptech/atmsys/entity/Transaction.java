package com.fptaptech.atmsys.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "transactions")
public class Transaction {
    @Id //Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Giá trị Tự động tăng
    private Long id;

    @Enumerated(EnumType.STRING) //Lưu dạng String
    @Column(nullable = false, length = 50) //Không được null và độ dài tối đa 50
    private TransactionType type;

    @Column(nullable = false) //Không được null
    private Double amount;

    @CreationTimestamp //Tự động tạo thời gian
    @Column(updatable = false) //Không được update
    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.LAZY) //Nhiều transaction thuộc về 1 account
    @JoinColumn(name = "account_id") //Khóa ngoại
    private Account account;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Transaction() {
    }

    public Transaction(Long id, TransactionType type, Double amount, LocalDateTime createAt, Account account) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.createAt = createAt;
        this.account = account;
    }
}
