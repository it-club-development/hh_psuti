# Техническая документация Backend — PROJECT PSUTI

Серверная часть веб-приложения ПГУТИ: стек технологий, архитектура, инфраструктура.

## Содержание

| Документ | Назначение | Аудитория |
|----------|------------|-----------|
| [tech-stack.md](tech-stack.md) | Технологический стек и слои | Разработчик, DevOps, архитектор |

## Структура каталога

```text
backend/
├── docs/
│   ├── index.md
│   ├── technical/
│   │   ├── index.md
│   │   └── tech-stack.md
│   └── api/
│       ├── index.md
│       └── auth-api.md
├── src/main/java/ru/psuti/
│   ├── config/
│   ├── controller/
│   ├── dto/
│   ├── model/
│   ├── repository/
│   ├── security/
│   └── service/
├── src/main/resources/
│   ├── application.yml
│   ├── application-test.yml
│   └── db/changelog/
├── docker-compose.yml
├── Dockerfile
└── build.gradle.kts
```

## Связанные разделы

- [Документация Backend](../index.md) — корневой указатель
- [API-контракты](api/index.md) — endpoints, DTO, примеры запросов
- [Общая документация проекта](../../super-app-psuti-docs/) — продукт и стек

## Статус

| Документ | Статус |
|----------|--------|
| tech-stack.md | Актуально: Java 26, Spring Boot 4.1, PostgreSQL, ClickHouse, Docker |
