# Code Review — Backend (hh_psuti)

- **Дата:** 10.09.2026
- **Объём ревью:** весь `src/main`, `src/test`, `build.gradle.kts`, `application*.yml`, `docker-compose.yaml`
- **Статус сборки:** ❌ **ПРОЕКТ НЕ КОМПИЛИРУЕТСЯ** (`gradlew compileJava` → ~60 ошибок MapStruct + отсутствующий enum `Status`)

---

## Сводка

| Категория | Кол-во |
|---|---|
| 🔴 Блокеры сборки (не компилируется) | 5 |
| 🔴 Критические баги (упадёт при первом же запуске/запросе) | 8 |
| 🟠 Критические уязвимости безопасности | 8 |
| 🟡 Высокий приоритет | 9 |
| 🔵 Средний / низкий (стиль, мёртвый код, конфиг) | 10 |

Главная проблема — **три параллельных слоя моделей данных** (`Models/*_entity`, `student|company|response/model/*`, `profile_company`) не согласованы между собой, а половина сервисов недоступна из API. Безопасность (JWT-фильтр) фактически не работает: аутентификация не попадает в SecurityContext.

---

## 1. Блокеры сборки (проект не компилируется)

### BUG-001 — `Vacancy_entity` пустая, при этом мапперы ссылаются на её поля
- **Файл:** `src/main/java/com/example/demo/Models/Vacancy_entity.java:3`
- Тело класса пустое, нет аннотации `@Entity`, нет полей. `IVacancyMapper` маппирует `Title`, `Salary`, `City` и т.д. → ~25 ошибок MapStruct.
- **Фикс:** либо заполнить сущность полями из `VacancyResponseDto` + `@Entity @Table(name = "vacancies")`, либо (рекомендую) удалить `Vacancy_entity` и использовать единую сущность `company/model/Vacancy.java`, поправив маппер.

### BUG-002 — Не существует enum `General.Status`
- **Файл:** `src/main/java/com/example/demo/Models/Response_entity.java:3`
- `import com.example.demo.General.Status;` — класс отсутствует → ошибка компиляции.
- **Фикс:** создать `General/Status.java`: `public enum Status { PENDING, INVITED, REJECTED, ACCEPTED }` (или взять `ResponseStatus` из `response/model/Response.java`).

### BUG-003 — Сломанные JPA-ассоциации: `@ManyToOne` + `@MapsId` на `UUID`
- **Файлы:** `Models/Resume_entity.java:19-22`, `Models/Response_entity.java:21-28`
```java
@ManyToOne(fetch = FetchType.LAZY)
@MapsId
@JoinColumn(name = "User_ID")
private UUID Student_ID;   // ← @MapsId/ассоциация не может быть UUID
```
- В `Response_entity` два `@MapsId` на два поля, `@JoinColumn(name = "ID")` совпадает с именем PK. Hibernate не сможет замапить.
- **Фикс:** ассоциации на сущности, а не на ID:
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "student_id", nullable = false)
private Student_entity student;
```
(вариант с `@MapsId` — только если реально нужен производный PK, и тогда `@Id private UUID id;` + `@MapsId` на одной ассоциации).

### BUG-004 — MapStruct не видит Lombok-геттеры: нет `lombok-mapstruct-binding`
- **Файл:** `build.gradle.kts:21-46`
- Аннотации `@Mapping(target = "Skills", source = "Skills")` ссылаются на имена **DB-колонок** (`Skills`, `Created_at`), а не на Java-поля. Плюс Lombok-геттеры не видны процессору MapStruct без биндинга.
- **Фикс:**
```kotlin
annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
implementation("org.mapstruct:mapstruct:1.6.3")
annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
```
- И **переписать мапперы**: в `source`/`target` указывать имена Java-полей в camelCase (`skills`, `createdAt`), а не имена колонок. Референс-пример полей сущности: `Resume_entity.skills`, `createdAt`. Все `@Mapping(target = "Skills", ...)` неверны.

### BUG-005 — `System.out.println` + эмодзи в логах вместо логгера
- **Файлы:** `AuthController.java:48,99,152`, `JwtFilter.java:32`, `JWT_util.java:85`
- Не ошибка компиляции, но логи через stdout не попадают в файл, ломают паттерн `logging.pattern`.
- **Фикс:** внедрить `private static final Logger log = LoggerFactory.getLogger(...)` (или `@Slf4j`) и заменить все `System.out.println`.

*(Компиляцию также блокирует ENV: `JAVA_HOME` указывает на несуществующий `jdk-26.0.1`. Рабочий JDK: `C:\Users\OLEGZ\.jdks\axiomjdk-21.0.10` — исправить переменную окружения или положиться на Gradle toolchain.)*

---

## 2. Критические баги (упадёт при запуске / первом запросе)

### BUG-006 — Spring Data не сможет создать бины репозиториев: метод `create()`
- **Файлы:** `Repositories/IVacancyRepository.java:12`, `IResumeRepository.java:10`, `IResponseRepository.java:10`, `ICompanyRepository.java:14`, `Profile_student/IStudentRepository.java:11`
- `Resume_entity create(Resume_entity entity);` — Spring Data пытается вывести JPQL-запрос из имени `create` → падение при старте приложения (`PropertyReferenceException`).
- **Фикс:** удалить методы `create()`, в сервисах вызывать стандартный `save()`:
```java
Resume_entity created = _repository.save(response);
```

### BUG-007 — Сервисы без `@Service`: бины не создаются
- **Файлы:** `Services/VacancyService.java:15`, `Profile_student/StudentService.java:12`
- Плюс нет **ни одного контроллера** для Vacancy / Resume / Response / Student — весь CRUD недоступен из API.
- **Фикс:** добавить `@Service`, написать REST-контроллеры (`/api/vacancies`, `/api/resumes`, ...), с `@PreAuthorize` на запись.

### BUG-008 — `Resume_entity.Created_at` / `Response_entity.Created_at`: `nullable = false`, но никогда не проставляется
- **Файлы:** `Resume_entity.java:29-30`, `Response_entity.java:33-34`
- Маппер ставит `@Mapping(target = "created_at", ignore = true)`, сервис не выставляет → INSERT упадёт с `not-null violation`.
- **Фикс:** lifecycle-хуки (как уже сделано в `CompanyProfile.java:66-75`):
```java
@PrePersist void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
@PreUpdate void onUpdate() { updatedAt = LocalDateTime.now(); }
```

### BUG-009 — Дублирование таблиц: два набора сущностей на одни и те же таблицы
- **Файлы:** `Models/Resume_entity.java:11` (`@Table(name="Resumes")`) ↔ `student/model/Resume.java:10` (`@Table(name = "resumes")`) — в PostgreSQL это **одна и та же таблица**. То же с `Responses`/`responses` (`Response_entity.java:13` ↔ `response/model/Response.java:10`).
- Hibernate в лучшем случае даст конфликт маппинга, в худшем — невидимые перезаписи данных.
- **Фикс (важное архитектурное решение):** оставить **один** набор сущностей. Рекомендация: новые пакеты (`student/model`, `company/model`, `response/model`, `notification/model`) с camelCase-полями и `Long`-id — либо мигрировать на них целиком, либо удалить. Не держать оба.

### BUG-010 — Несовместимость типов FK: `Notification.userId` — `Long`, а у пользователей `UUID`
- **Файл:** `notification/model/Notification.java:19-20` (и `student/model/Resume.java:19-20` — `Long studentId` при UUID-пользователях)
- **Фикс:** привести все внешние ключи к `UUID` (или к ассоциациям на сущности).

### BUG-011 — Тесты бьют по боевой БД: `application-test.yml` подключает реальный PostgreSQL с `ddl-auto: create-drop`
- **Файл:** `src/test/resources/application-test.yml:4-16`
- Запуск `gradlew test` **сносит все таблицы** в `hh_psuti` на localhost. H2 объявлен в зависимостях (`build.gradle.kts:53`), но не используется.
- **Фикс:**
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:test;MODE=PostgreSQL
    driver-class-name: org.h2.Driver
    username: sa
    password: ""
  jpa:
    hibernate:
      ddl-auto: create-drop
```

### BUG-012 — Токен выдаётся с захардкоженной ролью `STUDENT`
- **Файл:** `auth/security/JWT_util.java:55-57`
- `generateToken(email)` всегда кладёт `"STUDENT"`. Компания, залогинившись, получает токен студента → все `hasAnyRole('COMPANY','ADMIN')` проверки отвергнут её.
- **Фикс:** в `AuthController.login:72` передавать реальную роль: `jwtUtil.generateAccessToken(email, user.getRole().name())` и удалить legacy-метод `generateToken`.

### BUG-013 — `Roles` в PascalCase vs `hasAnyRole("COMPANY", "ADMIN")`
- **Файлы:** `General/Roles.java:3-4` (`Admin, Student, Company, Anonym`), `SecurityConfig.java:37`
- Spring строит authority как `ROLE_<claim>`. Заявка `"Company"` ≠ `"COMPANY"` → проверка ролей **никогда** не пройдёт, даже с правильным фильтром.
- **Фикс:** переименовать enum в верхний регистр (`STUDENT, COMPANY, ADMIN, ANONYM`) — значения в БД хранятся строкой, понадобится миграция данных, зато весь остальной код станет консистентен (`SecurityConfig`, claim, `AuthResponse.role`).

### BUG-014 — Двойной POST `/api/my-ip` и «публичный» эндпоинт за авторизацией
- **Файлы:** `AuthController.java:195-202` (`/api/auth/my-ip`) и `IpController.java:21` (`/api/my-ip`)
- `/api/my-ip` из `IpController` не попадает под `permitAll` → требует авторизацию, при том что `JwtFilter` аутентификацию не устанавливает (см. SEC-001) → всегда 401. Дубликат эндпоинта.
- **Фикс:** удалить `IpController` (или убрать дубль из `AuthController`), при необходимости добавить `/api/my-ip` в permitAll.

---

## 3. Критические уязвимости безопасности

### SEC-001 — `JwtFilter` не устанавливает `SecurityContext` → авторизация не работает вообще
- **Файл:** `auth/security/JwtFilter.java:30-34`
```java
if (jwtUtil.validateToken(token)) {
    request.setAttribute("email", email);  // ← attribute, не Authentication!
}
```
- `anyRequest().authenticated()` (`SecurityConfig.java:38`) смотрит в `SecurityContextHolder`. Все защищённые эндпоинты возвращают 401 даже с валидным токеном.
- **Фикс:**
```java
if (jwtUtil.validateToken(token)) {
    String email = jwtUtil.extractEmail(token);
    String role = jwtUtil.extractRole(token);           // e.g. "COMPANY"
    List<SimpleGrantedAuthority> authorities =
            List.of(new SimpleGrantedAuthority("ROLE_" + role));
    UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(email, null, authorities);
    SecurityContextHolder.getContext().setAuthentication(auth);
}
// и в конце: SecurityContextHolder.clearContext() в finally — иначе утечка между потоками Tomcat
```
- Плюс: `validateToken` принимает **любой** токен, включая refresh-токен (30 дней). Проверять claim `type == "access"`.

### SEC-002 — Самоназначение роли при регистрации: любой может стать `ADMIN`
- **Файл:** `AuthController.java:129-138`
- Клиент передаёт `role` в теле `/api/auth/register`; строка `role.equalsIgnoreCase("ADMIN")` создаёт админа без какой-либо проверки.
- **Фикс:** убрать выбор роли из публичного запроса. Регистрация по умолчанию `STUDENT`; `COMPANY` — отдельным эндпоинтом/флагом; `ADMIN` — только вручную (SQL/скрипт/закрытый эндпоинт).

### SEC-003 — `GET /api/auth/users` публичен и отдаёт сущности с хэшами паролей
- **Файлы:** `SecurityConfig.java:35` (`/api/auth/**` → `permitAll`), `AuthController.java:223-226`, `UserService.getAllUsers`
- `permitAll` на весь `/api/auth/**` открывает также `/users`, `/check/{email}`, `/accept-terms`. `getAllUsers()` возвращает `User_entity` → в JSON уходит `passwordHash`, `ipAddress`, `lastLogin`.
- **Фикс:** в `SecurityConfig` точно перечислить публичные пути:
```java
.requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/refresh",
                 "/api/auth/accept-terms", "/api/auth/check-terms/**", "/api/health").permitAll()
.requestMatchers("/api/auth/users/**").hasRole("ADMIN")
```
и возвращать DTO без `passwordHash`/`ipAddress`.

### SEC-004 — `accept-terms` / `check-terms` без аутентификации → любой может «принять соглашение» за любого
- **Файлы:** `AuthController.java:170-193`
- Метод принимает произвольный `userId` из query-параметра.
- **Фикс:** требовать Bearer-токен и брать `userId` из аутентификации (см. SEC-001), а не из запроса. `UUID.fromString(userId)` без обработки → 500 при невалидном UUID (`IllegalArgumentException`) — обернуть и вернуть 400.

### SEC-005 — IDOR в `CompanyProfileController`: доступ к чужим профилям
- **Файлы:** `CompanyProfileController.java:28,46,63`
- Любой COMPANY/ADMIN читает/пишет профиль **любого** `userId` из query-параметра. Учебно-чувствительные данные (ИНН, контакты) утекут между компаниями.
- **Фикс:** `userId` из `Authentication` (токена); ADMIN — отдельная ветка с явной проверкой роли. То же правило заранее применить к будущим студенческим/вакансионным эндпоинтам (`ResumeRequestDto.Student_ID` тоже приходит от клиента — `Models/DTOs/Resume/ResumeRequestDto.java:5`).

### SEC-006 — Секрет JWT захардкожен в коде и конфиге
- **Файлы:** `JWT_util.java:16` (`@Value` с default `mySecretKey123...`), `application.yml:63`
- Если на сервере не задан `JWT_SECRET`, приложение молча работает с публично известным секретом → любой может подделать токен любого пользователя.
- **Фикс:** убрать default-значения (`@Value("${jwt.secret}")`), добавить валидацию при старте: секрет обязателен, ≥ 32 байт; для dev — отдельный `application-dev.yml`.

### SEC-007 — User enumeration в логине
- **Файл:** `AuthController.java:52-60`
- Разные сообщения «Пользователь не найден» / «Неверный пароль» позволяют перебирать email-адреса.
- **Фикс:** единый ответ `401 {"error": "Неверный email или пароль"}`. (Регистрация тоже раскрывает существование email — это обычно допустимо, но пометить как осознанное решение.)

### SEC-008 — Нет rate limiting на логин/регистрацию + пароль от 6 символов
- **Файл:** `AuthController.java:113-117`
- Перебор паролей ничем не ограничен; 6 символов — слабая политика.
- **Фикс:** минимум 8 символов + `@Size`/regex в DTO; простейший локальный лимитер (например, счётчик неудач в Redis или bucket на `Bucket4j`), при недоступности — хотя бы лог-алерты.

---

## 4. Высокий приоритет

### BUG-015 — `catch (RuntimeException) → 404` глотает всё в `CompanyProfileController`
- **Файлы:** `CompanyProfileController.java:32-36,51-55`; `CompanyProfileService.java:29,33,43` (`throw new RuntimeException`)
- Ошибка валидации, NPE, ошибка БД — всё вернётся как 404 «Профиль компании не найден».
- **Фикс:** типизированные исключения (`EntityNotFoundException` → 404, `IllegalArgumentException` → 400) и глобальный `@RestControllerAdvice`, возвращающий единый JSON ошибок.

### BUG-016 — Нет глобального обработчика ошибок
- Все сервисы кидают `EntityNotFoundException` (Vacancy/Resume/Response/Student), но контроллеров для них нет, а в существующих try/catch по месту. Нет единого формата ошибок.
- **Фикс:** `@RestControllerAdvice` + `ProblemDetail` (Spring 6+), обработка: 400/401/403/404/409/500.

### BUG-017 — Refresh-токены не реализованы, хотя заявлены в API
- **Файлы:** `JWT_util.java:43-52` (не используется), `AuthResponse.java:31-37` (`refreshToken` никогда не проставляется), `RefreshTokenRequest.java` — мёртвый DTO; `docs/api/auth-api.md:47,106` и `docs/technical/tech-stack.md:305` обещают `POST /api/auth/refresh`
- Пользователь будет разлогиниваться каждые 15 минут (access TTL).
- **Фикс:** эндпоинт `POST /api/auth/refresh`: принимает refresh, проверяет `type == "refresh"`, выдаёт новую пару токенов; `AuthResponse` заполнять обоими токенами.

### BUG-018 — NPE-риск после регистрации
- **Файл:** `AuthController.java:160`
- `userService.getUserByEmail(email).getId().toString()` — лишний запрос к БД; при гонке/удалении записи вернёт NPE → 500. `registerUser` уже возвращает boolean, ID нужно вернуть из сервиса.
- **Фикс:** `registerUser` возвращает `User_entity` (или `Optional<User_entity>`), контроллер не ходит в БД повторно.

### BUG-019 — `AuthController.login` использует `RegisterRequest` вместо `AuthRequest`
- **Файл:** `AuthController.java:41`
- DTO для входа и регистрации один и тот же; `AuthRequest.java` существует, но не используется.
- **Фикс:** login принимает `AuthRequest`.

### BUG-020 — `@Valid` на DTO без единой аннотации валидации
- **Файлы:** `AuthController.java:84`, `auth/dto/RegisterRequest.java` (нет `@Email`, `@NotBlank`, `@Size`)
- Проверки email/пароля вручную в контроллере (строки 101-117) — валидация не декларативна, `confirmPassword` не проверяется аннотациями.
- **Фикс:** аннотации в DTO (`@Email @NotBlank` на email, `@Size(min=8)` на password) + `@PasswordMatch` кастомный или проверка в сервисе.

### BUG-021 — Гонка при регистрации
- **Файлы:** `AuthController.java:119-123`, `UserService.java:46-58`
- `userExists` проверяется дважды (в контроллере и сервисе), между проверкой и `save` параллельный запрос успевает создать дубликат → `DataIntegrityViolationException` → 500 вместо 409.
- **Фикс:** убрать проверку из контроллера; в сервисе ловить `DataIntegrityViolationException` → возвращать «email занят». Один источник истины.

### BUG-022 — CORS выключен целиком
- **Файл:** `SecurityConfig.java:32`
- `cors(cors -> cors.disable())` — фронтенд с другого origin не сможет работать (предполагается отдельный frontend-репозиторий).
- **Фикс:** `CorsConfigurationSource`-бин с явным списком origins (из конфига, `FRONTEND_URL`), методы `GET/POST/PUT/DELETE`, заголовок `Authorization`.

### BUG-023 — `IpUtil` доверяет подделываемым заголовкам
- **Файл:** `auth/security/IpUtil.java:10-24`
- `X-Forwarded-For` задаётся клиентом → в `users.ip_address` пишется произвольное значение (аудит-поле скомпрометировано).
- **Фикс:** если бэкенд за прокси — доверять заголовкам только от известного IP прокси; иначе использовать только `request.getRemoteAddr()`.

---

## 5. Средний / низкий приоритет (чистка)

### BUG-024 — Мёртвый и дублирующийся код — удалить
- `Services/CompanyService.java` — пустой класс
- `Bond_1C/Sync_log.java` — пустой класс; дубль `integration/model/SyncLog.java`
- `auth/repository/StudentRepository.java`, `auth/repository/CompanyRepository.java`, `Repositories/IUserRepository.java` — не используются (`UserRepository` — единственный реальный)
- `auth/dto/StudentRegisterRequest.java`, `auth/dto/CompanyRegisterRequest.java` — не используются
- `config/CacheConfig.java` — пустой, кэширование отключено
- `src/test/java/.../TestConfig.java` — `@TestConfiguration`, не подключён ни одним `@Import` — мёртвый
- `UserService.java:21-22` — внедрённый `PasswordEncoder` не используется

### BUG-025 — `application.properties` + `application.yml` одновременно
- **Файлы:** `src/main/resources/application.properties` (`spring.application.name=demo`) и `application.yml` (`name: backend`)
- `.properties` имеет приоритет → фактически имя всегда `demo`. Удалить `application.properties` (в git остаётся история).

### BUG-026 — `build.gradle.kts`: дубли и мусор зависимостей
- **Файл:** `build.gradle.kts`
- `spring-boot-starter-test` объявлен **4 раза** (строки 49, 58, 60, 101); огромный закомментированный блок (39-96) со всеми версиями; `javax.servlet:javax.servlet-api:4.0.1` (строка 31) — несовместим со Spring Boot 4 (Jakarta), не используется; `jjwt:0.12.6` одной банкой при наличии закомментированного правильного разбиения api/impl/jackson; Redis настроен в yml, но `spring-boot-starter-data-redis` отсутствует (конфиг мёртв).
- **Фикс:** один чистый блок зависимостей (шаблон — в закомментированном блоке, он правильнее активного).

### BUG-027 — `docker-compose.yaml`: mount несуществующей папки и obsolete-ключ
- **Файл:** `docker-compose.yaml:15` (`./initdb` — папки нет в репо), `:1` (`version: '3.9'` — устаревший атрибут Compose)
- Пароль БД `1` слабый; совпадает с default в `application.yml:12`.
- **Фикс:** убрать mount (или добавить `initdb/` с init-скриптами), убрать `version`, пароль — через `.env`.

### BUG-028 — Устаревшие свойства логирования
- **Файл:** `application.yml:103-106`
- `logging.file.max-size`/`max-history` не работают с Spring Boot 2.4+ → ротация логов не настроена, файл растёт бесконечно.
- **Фикс:** `logging.logback.rollingpolicy.max-file-size: 10MB`, `max-history: 30`.

### BUG-029 — Именование против Java-конвенций
- Классы с подчёркиваниями и PascalCase-полями: `JWT_util`, `User_entity`, `Resume_entity`, поля `Student_ID`, `Created_at`, enum `Roles.Admin`.
- Пакеты: `Models`, `Services`, `Repositories`, `Bond_1C` — капсом и вразнобой с `auth`, `profile_company`.
- **Фикс:** планомерно: `User_entity → User`, `JWT_util → JwtUtil`, поля — camelCase (`studentId`, `createdAt`), enum-константы — UPPER_SNAKE. После BUG-009 (единые модели) большая часть переименуется сама.

### BUG-030 — `ResponseRequestDto.Status` — `boolean`, а в сущности enum
- **Файлы:** `Models/DTOs/Response/ResponseRequestDto.java:10`, `ResponseResponseDto.java:11`
- Статус отклика (`PENDING/INVITED/...`) не может быть boolean. Синхронизировать с BUG-002.

### BUG-031 — `ddl-auto: update` в дев-конфиге, применяющемся по умолчанию
- **Файл:** `application.yml:24`, профиль по умолчанию `dev` (`:59`)
- Для учебного проекта приемлемо, но любое переименование поля = осиротевшие колонки; миграции в проде невозможны.
- **Фикс (рекомендация):** Flyway (`org.flywaydb:flyway-core` + `flyway-mysql`… точнее `flyway-database-postgresql`) + `ddl-auto: validate` для боевого профиля.

### BUG-032 — Эндпоинт `/api/auth/check/{email}` публичен
- **Файл:** `AuthController.java:204-221`
- Позволяет перечислять зарегистрированные email и их роли/статусы без авторизации.
- **Фикс:** ограничить (см. SEC-003) или вернуть только `exists: true/false` без роли.

### BUG-033 — `AuthResponse.role` в PascalCase, а claim — UPPER
- **Файл:** `AuthController.java:77` (`user.getRole().name()` → `"Student"`), в JWT при этом `"STUDENT"` (BUG-012). Фронтенд получает разные строки из разных мест.
- **Фикс:** единый UPPER-формат после BUG-013; синхронизировать `docs/api/auth-api.md`.

---

## 6. Как лучше сделать (архитектурные рекомендации)

1. **Единая структура пакетов по фичам** (сейчас 3 параллельных слоя — `Models/*_entity` + `Services` + `Repositories`, отдельно `profile_company`, отдельно `student|company|response|notification/model`):
   ```
   com.example.demo.{auth, student, company, vacancy, response, notification}
   // в каждом: controller / service / repository / dto / model
   ```
2. **Один источник истины для модели данных** — сначала решить судьбу дублей сущностей (BUG-009), потом всё остальное поедет легче.
3. **DTO-records + Bean Validation** на входе всех эндпоинтов; никогда не возвращать JPA-сущности наружу (утечка `passwordHash` — живой пример).
4. **Constructor injection** (`@RequiredArgsConstructor`) вместо `@Autowired` на полях — тестируемость и неизменяемость.
5. **`@RestControllerAdvice` + ProblemDetail** для единообразных ошибок API.
6. **Flyway-миграции** вместо `ddl-auto: update`; `validate` в prod-профиле.
7. **Refresh-токены + ротация** (`POST /api/auth/refresh`), как уже описано в `docs/api`.
8. **Тесты на H2 (или Testcontainers)**: сейчас `contextLoads` и JWT-тест единственные; добавить тесты на login/register/роли — это окупится сразу.
9. **OpenAPI/Swagger** (`springdoc-openapi-starter-webmvc-ui`) — у проекта уже есть ручные доки в `docs/api`, автогенерация снимет рассинхрон.
10. **CI (GitHub Actions):** `./gradlew build` на каждый PR — сборка бы не «молчала» полгода.

---

## 7. Распределение задач между разработчиками

> Задачи внутри направлений независимы, порядок следования = рекомендуемый порядок выполнения. Est — грубая оценка в «идеальных» часах.
> Сначала оба делают п.0 (это распараллеливает работу и убирает взаимоблокировки).

### Разработчик 1 — «Данные, сборка, тесты» (data-layer lead)

| # | Задача | Ссылки | Est |
|---|---|---|---|
| A0 | Синхронизировать ветку, зафиксировать решение о едином наборе сущностей (см. A1) с Разработчиком 2 | BUG-009 | 1h |
| A1 | Удалить дубли сущностей: оставить один набор моделей (`student/model` и др.), удалить `Models/*_entity`-дубли или наоборот — по решению A0; починить таблицы `resumes`/`responses` | BUG-009 | 4h |
| A2 | Создать `General/Status` (PENDING/INVITED/REJECTED/ACCEPTED), исправить `ResponseRequestDto`/`ResponseResponseDto` (boolean → Status) | BUG-002, BUG-030 | 1h |
| A3 | Заполнить/удалить `Vacancy_entity` (по решению A0 — использовать `company/model/Vacancy`), восстановить `@Entity` и поля | BUG-001 | 2h |
| A4 | Починить ассоциации: `@ManyToOne` на сущности вместо `UUID` + `@MapsId`, FK `Long` → `UUID` (`Notification`, `Resume`) | BUG-003, BUG-010 | 4h |
| A5 | Переписать мапперы MapStruct: source/target = Java-поля camelCase; обновить `build.gradle.kts` (mapstruct 1.6.3 + lombok-mapstruct-binding) | BUG-004 | 4h |
| A6 | Репозитории: удалить `create()`, перевести сервисы на `save()`; добавить `@Service` в `VacancyService`, `StudentService` | BUG-006, BUG-007 | 2h |
| A7 | `@PrePersist`/`@PreUpdate` для `createdAt`/`updatedAt` во всех сущностях | BUG-008 | 2h |
| A8 | `build.gradle.kts`: убрать дубли (4× starter-test), мёртвый закомментированный блок, `javax.servlet-api`; один блок зависимостей | BUG-026 | 1h |
| A9 | Удалить `application.properties` (конфликт с yml) | BUG-025 | 0.5h |
| A10 | Тесты: H2 вместо боевого PostgreSQL в `application-test.yml` (сейчас create-drop сносит реальную БД!), удалить мёртвый `TestConfig` | BUG-011, BUG-024 | 2h |
| A11 | Написать CRUD-контроллеры: `/api/vacancies`, `/api/resumes`, `/api/responses`, `/api/students` (роль на запись — согласовать с B: `hasRole('COMPANY')` и т.п.) | BUG-007 | 8h |
| A12 | Чистка мёртвого кода: `CompanyService`, `Sync_log`, `IUserRepository`, `auth/repository/StudentRepository`, `CompanyRepository`, неиспользуемые DTO, пустой `CacheConfig`, неиспользуемый `PasswordEncoder` в `UserService` | BUG-024 | 1.5h |
| A13 | `docker-compose.yaml`: убрать mount несуществующего `./initdb`, `version`, пароль через `.env` | BUG-027 | 0.5h |
| A14 | Свойства ротации логов → `logging.logback.rollingpolicy.*` | BUG-028 | 0.5h |

**Итого ~33h.** Definition of done для A: `./gradlew build` зелёный, приложение стартует, CRUD вакансий/резюме/откликов работает, тесты идут на H2.

### Разработчик 2 — «Безопасность, авторизация, API-контракт» (security lead)

| # | Задача | Ссылки | Est |
|---|---|---|---|
| B0 | Согласовать с A формат единой роли (UPPER: `STUDENT/COMPANY/ADMIN/ANONYM`) | BUG-013 | 0.5h |
| B1 | `Roles` → UPPER_SNAKЕ константы; миграция значений в БД (UPDATE users SET role='STUDENT' WHERE role='Student' и т.д.) | BUG-013 | 2h |
| B2 | `JwtFilter`: устанавливать `SecurityContext` c authority `ROLE_<role>`, брать `userId`/role из токена; проверять `type=access`; clearContext в finally | SEC-001 | 3h |
| B3 | Убрать захардкоженную роль из токена: login выдаёт токен с реальной ролью; удалить `generateToken`-legacy | BUG-012, BUG-033 | 1h |
| B4 | Убрать выбор роли при регистрации (никаких self-ADMIN); register → только STUDENT; COMPANY/ADMIN — отдельные флоу | SEC-002 | 2h |
| B5 | `SecurityConfig`: точечный permitAll (login/register/refresh/health), `/api/auth/users` → hasRole(ADMIN); защитить `accept-terms`, `check-terms`, `check/{email}` (userId из токена) | SEC-003, SEC-004, BUG-032 | 3h |
| B6 | Унифицировать ошибки логина (401 «неверный email или пароль»), починить NPE после регистрации (registerUser возвращает User), login принимает `AuthRequest` | SEC-007, BUG-018, BUG-019 | 2h |
| B7 | Реализовать `POST /api/auth/refresh` (type=refresh → новая пара токенов), заполнять `refreshToken` в `AuthResponse` | BUG-017 | 4h |
| B8 | Убрать default JWT-секрета из кода и yml; fail-fast при старте; секрет ≥ 32 байт | SEC-006 | 1.5h |
| B9 | IDOR: `CompanyProfileController` — userId из Authentication, а не query-параметра; типизированные исключения вместо `RuntimeException` + 404 на всё | SEC-005, BUG-015 | 3h |
| B10 | Глобальный `@RestControllerAdvice` + ProblemDetail (400/401/404/409/500), валидация `UUID.fromString` → 400 | BUG-016, SEC-004 | 3h |
| B11 | CORS-бин с allowlist origins вместо disable; серверы/фронтенд URL из конфига | BUG-022 | 2h |
| B12 | DTO-валидация: `@Email/@NotBlank/@Size(min=8)` в `RegisterRequest`/`CompanyProfileRequest` (ИНН — pattern 10/12 цифр); rate limiting на login (счётчик/lockout) | BUG-020, SEC-008 | 4h |
| B13 | Логгер вместо `System.out.println` (все файлы), убрать логирование IP-адресов с эмодзи; пометить доверие X-Forwarded-For (или фикс IpUtil) | BUG-005, BUG-023 | 1.5h |
| B14 | Удалить дубль `/api/my-ip` (`IpController`), синхронизировать `docs/api/auth-api.md` с реальным контрактом (роли UPPER, refresh) | BUG-014, BUG-033 | 1.5h |

**Итого ~34.5h.** Definition of done для B: компания входит и попадает на `/api/companies/**`; студент не может получить чужой профиль; без токена — 401; перебор ролей на register невозможен; refresh работает.

### Взаимные точки синхронизации
1. **A0/B0** — модель ролей и судьба дублей сущностей (до всего остального).
2. **A11/B5** — при написании контроллеров A берёт правила доступа, уже реализованные B.
3. **A2/B10** — формат ошибок в новых контроллерах A через advice B.

---

## 8. Чек-лист приёмки (когда оба закроют задачи)

- [ ] `./gradlew build` проходит без ошибок (компиляция + тесты)
- [ ] `gradlew test` не трогает PostgreSQL (только H2)
- [ ] Регистрация: студент не может указать роль COMPANY/ADMIN
- [ ] Логин студента/компании: правильная роль в токене и в `AuthResponse`
- [ ] Запрос с токеном проходит `anyRequest().authenticated()` (SecurityContext работает)
- [ ] Без токена: 401; с ролью STUDENT на `/api/companies/**`: 403
- [ ] `GET /api/auth/users` не отдаёт `passwordHash` и недоступен без роли ADMIN
- [ ] `POST /api/auth/refresh` возвращает новую пару токенов
- [ ] Профиль компании доступен только владельцу (IDOR закрыт)
- [ ] CRUD вакансий/резюме/откликов/студентов работает end-to-end
- [ ] Логи пишутся через SLF4J, ротация `logs/application.log` работает
- [ ] Никаких default-секретов в коде; секрет из `.env`

---

*Файл создан по результатам ревью 10.09.2026. Приоритеты: сначала блокеры раздела 1 (сборка), затем разделы 2–3, потом остальное.*
