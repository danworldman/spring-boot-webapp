-- Колонки для входа
ALTER TABLE users ADD COLUMN username VARCHAR(50) UNIQUE;
ALTER TABLE users ADD COLUMN password VARCHAR(100);
ALTER TABLE users ADD COLUMN role VARCHAR(20) DEFAULT 'ROLE_USER';

-- Тестовый админ
-- Логин: admin, Пароль: pass
INSERT INTO users (name, email, username, password, role)
VALUES ('Admin User', 'admin@example.com', 'admin', '$2a$10$AZg5rtrPgljiIHXzyuel2O4bE5lI2KhL7K08iBej7ykWQqN2Jwcx.', 'ROLE_ADMIN')
ON CONFLICT (username) DO NOTHING;
