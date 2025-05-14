CREATE DATABASE IF NOT EXISTS guestbook;
USE guestbook;

CREATE TABLE IF NOT EXISTS entries (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255), -- ✅ New column for storing email
    message TEXT
);
