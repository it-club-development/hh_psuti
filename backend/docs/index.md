# Документация Backend — PROJECT PSUTI

Серверная часть веб-приложения ПГУТИ: стек технологий, API-контракты, инфраструктура и инструкции по запуску.

## Содержание

| Документ | Назначение | Аудитория |
|----------|------------|-----------|
| [technical/index.md](technical/index.md) | Технический раздел: стек и архитектура | Разработчик, DevOps, архитектор |
| [technical/tech-stack.md](technical/tech-stack.md) | Технологический стек и слои | Разработчик, DevOps |
| [api/index.md](api/index.md) | API-контракты: endpoints, DTO, примеры запросов | Frontend-разработчик, тестировщик |
| [api/auth-api.md](api/auth-api.md) | Авторизация и регистрация: контракты и примеры | Frontend-разработчик |

## Структура каталога

```text
docs/
├── index.md                    ← вы здесь
├── technical/
│   ├── index.md
│   └── tech-stack.md
└── api/
    ├── index.md
    └── auth-api.md
```

## Связанные разделы

- [super-app-psuti-docs/](../../super-app-psuti-docs/) — общая документация проекта
- [source/task.md](../../source/task.md) — техническое задание

## Статус документов

| Документ | Статус |
|----------|--------|
| technical/tech-stack.md | Актуально: Java 26, Spring Boot 4.1, PostgreSQL, ClickHouse, Docker |
| api/auth-api.md | Актуально: регистрация и авторизация готовы |
