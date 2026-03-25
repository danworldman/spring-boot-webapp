CREATE TABLE IF NOT EXISTS subscriptions (
    follower_id  BIGINT NOT NULL,
    following_id BIGINT NOT NULL,

    PRIMARY KEY (follower_id, following_id),
    CONSTRAINT fk_follower  FOREIGN KEY (follower_id)  REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_following FOREIGN KEY (following_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT check_self_follow CHECK (follower_id <> following_id) -- гарантия, что не будет самоподписки
);