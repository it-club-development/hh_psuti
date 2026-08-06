# API Авторизации и Регистрации — PROJECT PSUTI

Контракты endpoints для регистрации новых пользователей и авторизации (login).

## Содержание

- [Регистрация](#регистрация)
- [Вход](#вход)
- [Примеры запросов](#примеры-запросов)
- [Обработка ошибок](#обработка-ошибок)

---

## Регистрация

Регистрация нового пользователя. Доступен публично.

**Endpoint:** `POST /api/auth/register`

### Request Body

| Поле | Тип | Обязательно | Описание |
|------|-----|-------------|----------|
| `fullName` | string | Да | ФИО (2-255 символов) |
| `accountNumber` | string | Да | Номер зачетной книжки (3-50 символов) |
| `email` | string | Да | Email (валидный формат) |
| `password` | string | Да | Пароль (6-128 символов) |

**Пример Request:**

```json
{
  "fullName": "Иванов Иван Иванович",
  "accountNumber": "ZK-2026-001",
  "email": "ivanov@psuti.ru",
  "password": "securePass123"
}
```

### Response

**Status: `201 Created`**

| Поле | Тип | Описание |
|------|-----|----------|
| `accessToken` | string | JWT access token (1 час) |
| `refreshToken` | string | JWT refresh token (24 часа) |
| `tokenType` | string | Тип токена (всегда `"Bearer"`) |
| `userId` | long | Идентификатор пользователя |
| `fullName` | string | ФИО |
| `role` | string | Роль (`"STUDENT"`) |

**Пример Response:**

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJaSy0yMDI2LTAwMSIsImlhdCI6MTcyMjAwMDAwMCwiZXhwIjoxNzIyMDAzNjAwLCJ1c2VySWQiOjEsInJvbGUiOiJTVEVERU5UIiwiZnVsbE5hbWUiOiJJdmFub3YgSXZhbiBJdmFub3ZpY2gifQ.abc123",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJaSy0yMDI2LTAwMSIsImlhdCI6MTcyMjAwMDAwMCwiZXhwIjoxNzIyMDg2NDAwfQ.xyz789",
  "tokenType": "Bearer",
  "userId": 1,
  "fullName": "Иванов Иван Иванович",
  "role": "STUDENT"
}
```

---

## Вход

Авторизация пользователя по номеру зачетной книжки ИЛИ email. Доступен публично.

**Endpoint:** `POST /api/auth/login`

### Request Body

| Поле | Тип | Обязательно | Описание |
|------|-----|-------------|----------|
| `login` | string | Да | Номер зачетной книжки ИЛИ email |
| `password` | string | Да | Пароль |

**Пример Request (по зачетной книжке):**

```json
{
  "login": "ZK-2026-001",
  "password": "securePass123"
}
```

**Пример Request (по email):**

```json
{
  "login": "ivanov@psuti.ru",
  "password": "securePass123"
}
```

### Response

**Status: `200 OK`**

| Поле | Тип | Описание |
|------|-----|----------|
| `accessToken` | string | JWT access token (1 час) |
| `refreshToken` | string | JWT refresh token (24 часа) |
| `tokenType` | string | Тип токена (всегда `"Bearer"`) |
| `userId` | long | Идентификатор пользователя |
| `fullName` | string | ФИО |
| `role` | string | Роль (`"STUDENT"`) |

**Пример Response:**

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJaSy0yMDI2LTAwMSIsImlhdCI6MTcyMjAwMDAwMCwiZXhwIjoxNzIyMDAzNjAwLCJ1c2VySWQiOjEsInJvbGUiOiJTVEVERU5UIiwiZnVsbE5hbWUiOiJJdmFub3YgSXZhbiBJdmFub3ZpY2gifQ.abc123",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJaSy0yMDI2LTAwMSIsImlhdCI6MTcyMjAwMDAwMCwiZXhwIjoxNzIyMDg2NDAwfQ.xyz789",
  "tokenType": "Bearer",
  "userId": 1,
  "fullName": "Иванов Иван Иванович",
  "role": "STUDENT"
}
```

---

## Примеры запросов

### cURL

**Регистрация:**

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Иванов Иван Иванович",
    "accountNumber": "ZK-2026-001",
    "email": "ivanov@psuti.ru",
    "password": "securePass123"
  }'
```

**Вход:**

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "login": "ZK-2026-001",
    "password": "securePass123"
  }'
```

**Защищённый endpoint (с токеном):**

```bash
curl -X GET http://localhost:8080/api/protected-resource \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

### JavaScript (Fetch API)

**Регистрация:**

```javascript
const response = await fetch('http://localhost:8080/api/auth/register', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    fullName: 'Иванов Иван Иванович',
    accountNumber: 'ZK-2026-001',
    email: 'ivanov@psuti.ru',
    password: 'securePass123'
  })
});

const data = await response.json();
// data.accessToken, data.refreshToken, data.userId, data.fullName, data.role
```

**Вход:**

```javascript
const response = await fetch('http://localhost:8080/api/auth/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    login: 'ZK-2026-001',
    password: 'securePass123'
  })
});

const data = await response.json();
// data.accessToken, data.refreshToken
```

**Защищённый запрос:**

```javascript
const response = await fetch('http://localhost:8080/api/protected-resource', {
  method: 'GET',
  headers: {
    'Authorization': `Bearer ${accessToken}`,
    'Content-Type': 'application/json'
  }
});
```

### TypeScript (Axios)

```typescript
import axios from 'axios';

const API_URL = 'http://localhost:8080';

// Регистрация
async function register(data: {
  fullName: string;
  accountNumber: string;
  email: string;
  password: string;
}) {
  const response = await axios.post(`${API_URL}/api/auth/register`, data);
  return response.data; // AuthResponse
}

// Вход
async function login(data: {
  login: string;
  password: string;
}) {
  const response = await axios.post(`${API_URL}/api/auth/login`, data);
  return response.data; // AuthResponse
}

// Защищённый запрос
async function getProtectedData(token: string) {
  const response = await axios.get(`${API_URL}/api/protected-resource`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return response.data;
}
```

---

## Обработка ошибок

### 400 Bad Request — Ошибка валидации

**Пример Request:**

```json
{
  "fullName": "",
  "accountNumber": "ZK",
  "email": "invalid-email",
  "password": "123"
}
```

**Response:**

```json
{
  "message": "ФИО обязательно"
}
```

### 409 Conflict — Уже зарегистрирован

**Пример:**

```json
{
  "message": "Зачетная книжка уже зарегистрирована"
}
```

или

```json
{
  "message": "Email уже зарегистрирован"
}
```

### 401 Unauthorized — Неверный пароль или пользователь не найден

**Пример Request:**

```json
{
  "login": "ZK-2026-001",
  "password": "wrongPassword"
}
```

**Response:**

```json
{
  "message": "Неверный пароль"
}
```

или

```json
{
  "message": "Пользователь не найден"
}
```

### 403 Forbidden — Аккаунт заблокирован

```json
{
  "message": "Аккаунт заблокирован"
}
```

### 401 Unauthorized — Токен невалиден

**Пример:**

```bash
curl -X GET http://localhost:8080/api/protected-resource \
  -H "Authorization: Bearer invalid_token"
```

**Response:** `401 Unauthorized` (без тела)

---

## Связанные документы

- [Технический стек](../technical/tech-stack.md)
- [Общая документация](../index.md)

## История изменений

| Дата | Изменение |
|------|-----------|
| 2026-08-05 | Первая версия: register, login endpoints, DTO, примеры |
