CREATE TABLE posts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,

    CONSTRAINT fk_posts_user
    FOREIGN KEY (user_id)
    REFERENCES users (id)
    ON DELETE CASCADE -- Если удалить юзера, то его задачи тоже удалятся
);