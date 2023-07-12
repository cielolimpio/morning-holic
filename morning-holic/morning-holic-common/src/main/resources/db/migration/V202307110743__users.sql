CREATE TABLE IF NOT EXISTS users (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    role VARCHAR(20) NOT NULL,
    nickname VARCHAR(50) NOT NULL UNIQUE,
    target_wake_up_time DATETIME,
    refund_account VARCHAR(100),
    mode VARCHAR(20),
    status VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    deleted_at DATETIME,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB;