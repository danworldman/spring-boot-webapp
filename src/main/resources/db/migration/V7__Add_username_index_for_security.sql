-- ускоряет поиск пользователя при авторизации
CREATE UNIQUE INDEX IF NOT EXISTS idx_users_username ON users(username);
