ALTER TABLE users
    ADD COLUMN profile_emoji VARCHAR(10) NOT NULL DEFAULT '🌞';

CREATE TABLE IF NOT EXISTS images (
    id BIGINT NOT NULL AUTO_INCREMENT,
    original_s3_path VARCHAR(200) NOT NULL,
    thumbnail_s3_path VARCHAR(200) NOT NULL,
    created_at DATETIME NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS diaries (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    type VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    deleted_at DATETIME,
    PRIMARY KEY (`id`),
    CONSTRAINT diaries__user_id FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS diary_images (
    id BIGINT NOT NULL AUTO_INCREMENT,
    diary_id BIGINT NOT NULL,
    image_id BIGINT NOT NULL,
    type VARCHAR(20) NOT NULL,
    minus_score INTEGER DEFAULT 0 NOT NULL,
    created_at DATETIME NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT diary_images__diary_id FOREIGN KEY (diary_id) REFERENCES diaries(id),
    CONSTRAINT diary_images__image_id FOREIGN KEY (image_id) REFERENCES images(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS diary_contents (
    id BIGINT NOT NULL AUTO_INCREMENT,
    diary_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    minus_score INTEGER DEFAULT 0 NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT diary_contents__diary_id FOREIGN KEY (diary_id) REFERENCES diaries(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS diary_likes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    diary_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT diary_likes__diary_id FOREIGN KEY (diary_id) REFERENCES diaries(id),
    CONSTRAINT diary_likes__user_id FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS diary_comments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    diary_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    comment TEXT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    deleted_at DATETIME,
    PRIMARY KEY (`id`),
    CONSTRAINT diary_comments__diary_id FOREIGN KEY (diary_id) REFERENCES diaries(id),
    CONSTRAINT diary_comments__user_id FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS diary_comment_replies (
    id BIGINT NOT NULL AUTO_INCREMENT,
    diary_comment_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    reply TEXT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    deleted_at DATETIME,
    PRIMARY KEY (`id`),
    CONSTRAINT diary_comment_replies__diary_comment_id FOREIGN KEY (diary_comment_id) REFERENCES diary_comments(id),
    CONSTRAINT diary_comment_replies__user_id FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS user_scores (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    year INTEGER NOT NULL,
    month INTEGER NOT NULL,
    score INTEGER NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT user_scores__user_id FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;