# API Backend — PROJECT PSUTI

Контракты REST API серверной части: endpoints, DTO, примеры запросов и ответов.

## Содержание

| Документ | Назначение | Аудитория |
|----------|------------|-----------|
| [auth-api.md](auth-api.md) | Авторизация и регистрация | Frontend-разработчик |

## Общие сведения

| Параметр | Значение |
|----------|----------|
| Базовый URL | `http://localhost:8080` (dev) |
| Формат данных | `application/json` |
| Аутентификация | Bearer token в заголовке `Authorization` |
| Кодировка | UTF-8 |

### Структура ответов

**Успешный ответ:**

```json
{
  "field": "value"
}
```

**Ошибка валидации (400 Bad Request):**

```json
{
  "message": "Описание ошибки"
}
```

### Защита endpoints

Все endpoints, кроме `/api/auth/*`, требуют токен:

```
Authorization: Bearer <access_token>
```

---

## Связанные документы

- [Технический стек](../technical/tech-stack.md)
- [Общая документация](../index.md)

## История изменений

| Дата | Изменение |
|------|-----------|
| 2026-08-05 | Первая версия: auth endpoints (register, login) |
