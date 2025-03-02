CREATE DATABASE fptaptech2025;

USE fptaptech2025;

CREATE TABLE User (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      username VARCHAR(255) NOT NULL,
                      password VARCHAR(255) NOT NULL,
                      email VARCHAR(255) NOT NULL
);

CREATE TABLE Account (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         account_number VARCHAR(255) NOT NULL,
                         balance DOUBLE NOT NULL,
                         user_id BIGINT,
                         FOREIGN KEY (user_id) REFERENCES User(id)
);

CREATE TABLE Transaction (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             account_id BIGINT NOT NULL,
                             amount DOUBLE NOT NULL,
                             type VARCHAR(255) NOT NULL,
                             FOREIGN KEY (account_id) REFERENCES Account(id)
);