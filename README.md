# Spring Boot User Management Service (Standard 2026)

Современное REST API приложение для управления пользователями. Проект демонстрирует современную многослойную архитектуру и контейнеризацию.

## Стек технологий
* **Java 21** (LTS) —  использование Records, Pattern Matching и Virtual Threads.
* **Spring Boot 4.0.3** — ядро системы (Web, Data JPA, Security, Validation).
* **PostgreSQL 17** — реляционное хранилище данных.
* **Flyway** — эволюционный подход к миграциям базы данных (DDL-auto: none).
* **Docker & Docker Compose** — Multi-stage builds с использованием BellSoft Liberica JDK 21/25.
* **OpenAPI 3 (Swagger)** — интерактивная документация и контрактное тестирование.
* **JUnit 5 & Mockito** —  покрытие бизнес-логики Unit-тестами.
* **Lombok & SLF4J** — чистый код и структурированное логирование.
* **Redis 7** — высокопроизводительное кэширование данных (Spring Cache).
* **Spring AOP** — аспектно-ориентированное программирование для мониторинга производительности.
* **Spring Security** — авторизация через Basic Auth и ролевая модель (RBAC).

## Ключевые архитектурные решения
- **Layered Architecture**: Четкое разделение на контроллеры, сервисы и репозитории.
- **DTO Pattern**: Использование `Java Records` для иммутабельности и безопасности данных.
- **Global Error Handling**: Ответы в формате **RFC 9457 (Problem Details)** через `@RestControllerAdvice`.
- **Validation**: Каскадная проверка данных (`@Valid`, `@Email`, `@NotBlank`, `@Min`).
- **Logs**: Разделение событий по уровням важности (INFO для успеха, WARN для бизнес-ошибок, ERROR для сбоев).
- **Security-First Approach**: Интеграция Spring Security. Пароли хранятся в виде хешей BCrypt. Реализована ролевая модель доступа (RBAC).
- **Infrastructure as Code**: Полная готовность к деплою «одной кнопкой» через Docker Compose с настроенными Healthchecks.

## Эндпоинты (API)
- **GET** `/api/users` — получить список всех пользователей.
- **GET** `/api/users/{id}` — получить пользователя по ID.
- **POST** `/api/users` — создать нового пользователя.
- **PUT** `/api/users/{id}` — обновить данные существующего пользователя.
- **DELETE** `/api/users/{id}` — удалить пользователя.
- **GET** `/api/tasks` — получить список всех задач.
- **POST** `/api/tasks` — создать новую задачу.
- **GET** `/api/posts` — получить список всех постов.
- **POST** `/api/posts` — создать новый пост.

Все маршруты имеют префикс `/api`.

| Метод     | URL               | Описание                                              |
|-----------|-------------------|-------------------------------------------------------|
| GET       | `/api/users`      | Получить список всех пользователей                    |
| GET       | `/api/users/{id}` | Получить пользователя по ID (валидация ID > 0)        |
| POST      | `/api/users`      | Создать нового пользователя (валидация email и имени) |
| PUT       | `/api/users/{id}` | Обновить данные существующего пользователя            |
| DELETE    | `/api/users/{id}` | Удалить пользователя                                  |
| GET       | `/api/tasks`      | Получить список всех задач	                           |   
| POST	     |`/api/tasks`  	 | Создать новую задачу                                  |     
| GET       | `/api/posts`      | Получить список всех постов	                          |   
| POST	     |`/api/posts`  	 | Создать новый пост                                    |  

## Как запустить проект?

Для запуска вам понадобятся только установленные **Git** и **Docker Desktop**.

1. **Клонируйте репозиторий:**
   ```bash
   git clone https://github.com/danworldman/spring-boot-webapp
2. **Настройте окружение:**
   выполните команду
    ```bash 
   cp .env.example .env
3. **Запустите инфраструктуру и приложение:**
    ```bash
   docker-compose up --build

После запуска API будет доступно по адресу: http://localhost:8080/api/users<br>
Документация Swagger (если включена): http://localhost:8080/swagger-ui/index.html

### Данные для входа (Admin):
- **Логин**: admin
- **Пароль**: pass<br>
  (Данные автоматически подтягиваются из `init_data.sql` при первом запуске)

## Работа с Docker
- **Проверка статуса**: docker-compose ps
- **Удалить контейнеры и очистить ресурсы**: docker-compose down
- **Посмотреть логи**: docker-compose logs -f app

Проект разработан в учебных целях как пример высокоуровневого подхода к разработке на Spring Boot.