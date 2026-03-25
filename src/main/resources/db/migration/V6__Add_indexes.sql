CREATE INDEX IF NOT EXISTS idx_posts_user_id ON posts(user_id);
CREATE INDEX IF NOT EXISTS idx_subscriptions_following_id ON subscriptions(following_id);
CREATE INDEX IF NOT EXISTS idx_posts_created_at ON posts(created_at DESC);