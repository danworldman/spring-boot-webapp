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
* **Micrometer & Actuator** — сбор телеметрии и мониторинг Virtual Threads.
* **Prometheus & Grafana** — визуализация телеметрии и мониторинг состояния системы.

## Ключевые архитектурные решения
- **Layered Architecture**: Четкое разделение на контроллеры, сервисы и репозитории.
- **DTO Pattern**: Использование `Java Records` для иммутабельности и безопасности данных.
- **Global Error Handling**: Ответы в формате **RFC 9457 (Problem Details)** через `@RestControllerAdvice`.
- **Validation**: Каскадная проверка данных (`@Valid`, `@Email`, `@NotBlank`, `@Min`).
- **Logs**: Разделение событий по уровням важности (INFO для успеха, WARN для бизнес-ошибок, ERROR для сбоев).
- **Security-First Approach**: Интеграция Spring Security. Пароли хранятся в виде хешей BCrypt. Реализована ролевая модель доступа (RBAC).
- **Infrastructure as Code**: Развертывание всей среды (БД, Redis, Мониторинг) одной командой.
- **Event-Driven & Async**: Асинхронная обработка событий в Virtual Threads через TransactionalEventListener.
- **Observability**: Глубокий мониторинг JVM и бизнес-метрик с визуализацией в Grafana.
- **Data Persistence**: Использование Bind Mounts для БД и Named Volumes для Grafana, обеспечивающее сохранность данных и логов при перезапуске.

## Эндпоинты (API)
- **GET** `/api/users` — получить список всех пользователей.
- **GET** `/api/users/{id}` — получить пользователя по ID.
- **POST** `/api/users` — создать нового пользователя.
- **PUT** `/api/users/{id}` — обновить данные существующего пользователя.
- **DELETE** `/api/users/{id}` — удалить пользователя.
- **POST** `/api/users/{id}/follow` — подписаться на другого пользователя
- **DELETE** `/api/users/{id}/unfollow` — отписаться от пользователя
- **GET** `/api/posts` — получить список всех постов.
- **POST** `/api/posts` — создать новый пост.
- **GET** `/api/posts/feed` — лента постов от авторов, на которых подписан пользователь.

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

### Доступные инструменты:
- **REST API**: http://localhost:8080/api/users
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **Grafana (Dashboards)**: http://localhost:3000 (Login: admin, Pass: из .env)
- **Prometheus (Metrics)**: http://localhost:9090
> **Примечание**: Grafana и Prometheus конфигурируются автоматически при старте

### Данные для авторизации (API):
- **Логин**: admin
- **Пароль**: pass
> **Примечание**: Данные автоматически подтягиваются из `init_data.sql` при первом запуске

## Работа с Docker
- **Проверка статуса**: docker-compose ps
- **Удалить контейнеры и очистить ресурсы**: docker-compose down
- **Посмотреть логи**: docker-compose logs -f app
- **Очистка данных (Volume)**: docker-compose down -v

Проект разработан в учебных целях как пример высокоуровневого подхода к разработке на Spring Boot.