# Технологический стек Backend — PROJECT PSUTI

Серверная часть веб-приложения ПГУТИ: Java, Spring Boot, PostgreSQL, ClickHouse, Docker.

| | |
|--|--|
| **Статус** | Актуально (реализовано: регистрация, авторизация, JWT, Docker) |
| **Источники** | `build.gradle.kts`, `docker-compose.yml`, исходный код `src/` |

## Содержание

- [Сводка](#сводка)
- [Архитектура](#архитектура)
- [Сервер (Backend)](#сервер-backend)
- [База данных](#база-данных)
- [Инфраструктура и Docker](#инфраструктура-и-docker)
- [Запуск и разработка](#запуск-и-разработка)
- [Нефункциональные требования](#нефункциональные-требования)
- [Открытые вопросы](#открытые-вопросы)

---

## Сводка

| Слой | Технологии |
|------|------------|
| Язык | **Java 26** |
| Фреймворк | **Spring Boot 4.1.0** |
| Безопасность | **Spring Security + JWT** (jjwt 0.12.5) |
| Основная БД | **PostgreSQL 17** (JPA/Hibernate, Liquibase) |
| Аналитика | **ClickHouse** (прямые JDBC-запросы) |
| Инфраструктура | **Docker, Docker Compose** |
| Качество кода | **Checkstyle, PMD 7.21, SpotBugs 4.10, Allure** |

---

## Архитектура

```text
┌────────────────────────────────────────────────────────────┐
│  Frontend (React/Vite/TS)                                  │
│  REST API, JSON, JWT Bearer tokens                         │
└──────────────────────────┬─────────────────────────────────┘
                           │ HTTPS, REST, JSON, JWT
┌──────────────────────────▼─────────────────────────────────┐
│  Java 26 · Spring Boot 4.1 · Spring Security · JWT         │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐  │
│  │ AuthController│  │ UserService  │  │ SecurityConfig   │  │
│  │ /api/auth/*  │  │ register/login│  │ JwtAuthFilter    │  │
│  └──────────────┘  └──────────────┘  └──────────────────┘  │
│         │                 │                    │            │
│         │            UserRepository          PasswordEncoder│
│         │                 │                    │            │
│         │            PostgreSQL              BCrypt         │
│         │            (JPA/Hibernate)         hashing       │
│         │                 │                            │    │
│         │            Liquibase migrations              │    │
│         └─────────────────┼────────────────────────────┘    │
│                           │                                 │
│  ┌──────────────────────┐ │ ┌──────────────────────────┐   │
│  │ ClickHouseConfig     │ │ │ AnalyticsService         │   │
│  │ clickHouseDataSource │ │ │ countByDay()             │   │
│  │ clickHouseJdbcTemplate│ │ │ (conditional)           │   │
│  └──────────────────────┘ └─┴──────────────────────────┘   │
└──────────────────────────┬─────────────────────────────────┘
                           │
        ┌──────────────────┼──────────────────┐
        ▼                  ▼                  ▼
  PostgreSQL         ClickHouse        Docker
  (5432)             (8123/9000)       Compose
```

---

## Сервер (Backend)

### Язык и фреймворк

| Компонент | Технология | Версия |
|-----------|------------|--------|
| Язык | **Java** | 26 |
| Фреймворк | **Spring Boot** | 4.1.0 |
| Build | **Gradle Kotlin DSL** | 9.5.1 |

### Зависимости

| Область | Модули |
|---------|--------|
| Web API | `spring-boot-starter-webmvc` |
| Security | `spring-boot-starter-security` |
| Data JPA | `spring-boot-starter-data-jpa` |
| Validation | `spring-boot-starter-validation` |
| Liquibase | `spring-boot-starter-liquibase` |
| JWT | `jjwt-api:0.12.5`, `jjwt-impl`, `jjwt-jackson` |
| Lombok | `compileOnly` |
| PostgreSQL | `runtimeOnly postgresql` |
| ClickHouse | `runtimeOnly clickhouse-jdbc:0.9.0` |

### Структура пакетов

```text
ru.psuti/
├── config/              # Конфигурация (ClickHouse, DataSource)
├── controller/          # REST-контроллеры (AuthController)
├── dto/                 # Data Transfer Objects
│   ├── LoginRequest
│   ├── RegisterRequest
│   └── AuthResponse
├── model/               # JPA-сущности (User)
├── repository/          # JPA-репозитории (UserRepository)
├── security/            # Spring Security (SecurityConfig, JwtAuthFilter)
└── service/             # Бизнес-логика (UserService, JwtService, AnalyticsService)
```

### Безопасность

| Компонент | Технология | Назначение |
|-----------|------------|------------|
| Аутентификация | **Spring Security** | Фильтры, провайдеры |
| Токены | **JWT** (jjwt 0.12.5) | Stateless-доступ, access + refresh |
| Хеширование | **BCrypt** | Пароли пользователей |
| Фильтр | **JwtAuthFilter** | Валидация токена на каждый запрос |

**JWT-схема:**

| Токен | Срок жизни | Назначение |
|-------|------------|------------|
| Access token | 1 час | Запросы к API |
| Refresh token | 24 часа | Обновление access token |

**Защита endpoints:**

| Endpoint | Доступ |
|----------|--------|
| `POST /api/auth/register` | Публичный |
| `POST /api/auth/login` | Публичный |
| Все остальные | Требует `Authorization: Bearer <token>` |

---

## База данных

### PostgreSQL (основная)

| Компонент | Технология | Назначение |
|-----------|------------|------------|
| СУБД | **PostgreSQL 17** | Хранение пользователей, транзакции |
| ORM | **Hibernate (JPA)** | Маппинг сущностей |
| Миграции | **Liquibase** | Версионирование схемы |

**Таблица `users`:**

| Поле | Тип | Описание |
|------|-----|----------|
| id | BIGINT (PK, auto) | Идентификатор |
| account_number | VARCHAR(50) | Номер зачетной книжки (unique) |
| email | VARCHAR(255) | Email (unique) |
| full_name | VARCHAR(255) | ФИО |
| password_hash | VARCHAR(512) | BCrypt-хешированный пароль |
| role | VARCHAR(50) | Роль (STUDENT по умолчанию) |
| created_at | TIMESTAMP | Дата создания |
| updated_at | TIMESTAMP | Дата обновления |
| is_active | BOOLEAN | Статус аккаунта |

**Liquibase changelog:**

```yaml
db/changelog/
├── changelog-master.yaml
└── 001_init_schema.yaml   # Создание таблицы users
```

### ClickHouse (аналитика)

| Компонент | Технология | Назначение |
|-----------|------------|------------|
| СУБД | **ClickHouse** | Аналитика, логи, отчёты |
| Доступ | **JDBC** | Прямые SQL-запросы (без JPA) |

**Конфигурация:**

- Включается через `SPRING_CLICKHOUSE_ENABLED=true`
- Отключён в тестах (`application-test.yml`)
- Сервис `AnalyticsService` — условно загружается (`@ConditionalOnBean`)

---

## Инфраструктура и Docker

### docker-compose.yml

| Сервис | Образ | Порт | Назначение |
|--------|-------|------|------------|
| `postgres` | `postgres:17-alpine` | `5432` | Основная БД |
| `clickhouse` | `clickhouse/clickhouse-server:latest` | `8123`, `9000` | Аналитика |
| `backend` | `Dockerfile` (сборка из context) | `8080` | Spring Boot |

### Переменные окружения

| Переменная | По умолчанию | Описание |
|------------|--------------|----------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/psuti` | URL PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Логин БД |
| `SPRING_DATASOURCE_PASSWORD` | `postgres` | Пароль БД |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `validate` | Режим Hibernate |
| `SPRING_CLICKHOUSE_URL` | `jdbc:clickhouse://localhost:8123/psuti` | URL ClickHouse |
| `SPRING_CLICKHOUSE_ENABLED` | `false` | Включить ClickHouse |
| `APP_JWT_SECRET` | `Xk9#mP2...` | Секрет JWT |
| `APP_JWT_EXPIRATION_MS` | `3600000` | TTL access token (1ч) |
| `APP_JWT_REFRESH_EXPIRATION_MS` | `86400000` | TTL refresh token (24ч) |

### Dockerfile

```dockerfile
# Этап сборки
FROM eclipse-temurin:26-jdk-alpine
COPY gradlew gradle build.gradle.kts settings.gradle.kts ./
COPY src src
RUN ./gradlew bootJar

# Рантайм
FROM eclipse-temurin:26-jre-alpine
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## Запуск и разработка

### Локальный запуск (без Docker)

```powershell
# 1. Запустить PostgreSQL и ClickHouse вручную
# 2. Обновить application.yml (если нужно)
# 3. Собрать и запустить
cd backend
./gradlew.bat bootRun
```

### Запуск через Docker

```powershell
# 1. Собрать и запустить всё
cd backend
docker-compose up -d

# 2. Проверить статус
docker-compose ps

# 3. Посмотреть логи backend
docker-compose logs -f backend

# 4. Остановить
docker-compose down

# 5. Остановить + удалить volumes (чистая БД)
docker-compose down -v
```

### Тесты

```powershell
# Запустить тесты (H2 in-memory)
./gradlew.bat test

# Полный цикл проверок (Checkstyle, PMD, SpotBugs, тесты)
./gradlew.bat check
```

### Проверка API

```powershell
# Регистрация
curl -X POST http://localhost:8080/api/auth/register `
  -H "Content-Type: application/json" `
  -d '{"fullName":"Иванов И.И.","accountNumber":"ZK-001","email":"ivanov@psuti.ru","password":"pass123"}'

# Вход
curl -X POST http://localhost:8080/api/auth/login `
  -H "Content-Type: application/json" `
  -d '{"login":"ZK-001","password":"pass123"}'
```

---

## Нефункциональные требования

| Область | Требование |
|---------|------------|
| Безопасность | BCrypt-хеширование паролей, JWT tokens, stateless-сессии |
| Масштабируемость | Горизонтальное масштабирование backend, разделение БД и аналитики |
| Надёжность | Healthcheck для сервисов, retry-логики, валидация входных данных |
| Качество кода | Checkstyle, PMD 7.21, SpotBugs 4.10, Allure-отчёты |

---

## Открытые вопросы

- [ ] OpenAPI/Swagger-спецификация для REST API
- [ ] CI/CD пайплайн (GitHub Actions / GitLab CI)
- [ ] Мониторинг (Prometheus + Grafana)
- [ ] Логирование (ELK / Loki)
- [ ] Refresh token endpoint (`POST /api/auth/refresh`)
- [ ] Восстановление пароля (`POST /api/auth/forgot-password`)
- [ ] Ролевая модель (STUDENT, TEACHER, ADMIN)

---

## Связанные документы

- [Документация Backend](../index.md)
- [API-контракты](../api/index.md)
- [Общая документация проекта](../../super-app-psuti-docs/)
- [Техническое задание](../../source/task.md)

## История изменений

| Дата | Изменение |
|------|-----------|
| 2026-08-05 | Первая версия: регистрация, авторизация, JWT, Docker |
| 2026-08-05 | Стек: Java 26, Spring Boot 4.1, PostgreSQL, ClickHouse |
