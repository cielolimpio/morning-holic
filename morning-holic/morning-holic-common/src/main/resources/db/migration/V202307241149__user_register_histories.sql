CREATE TABLE IF NOT EXISTS user_register_histories (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id LONG NOT NULL,
    status VARCHAR(20) NOT NULL,
    reject_reason VARCHAR(300),
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    deleted_at DATETIME,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB;