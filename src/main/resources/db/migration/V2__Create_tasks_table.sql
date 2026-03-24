CREATE TABLE IF NOT EXISTS tasks (
    id   BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    text VARCHAR(255) NOT NULL,

        CONSTRAINT fk_tasks_user
            FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE -- Если удалить юзера, то его задачи тоже удалятся
    );