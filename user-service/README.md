# User Service

Микросервис для управления пользователями спортивной платформы.

## Основной функционал
- Регистрация и аутентификация (email/password, JWT)
- JWT авторизация
- RBAC (роли: участник, организатор, админ)
- Управление профилем пользователя

## Быстрый старт (Docker Compose)

1. **Создайте секретный ключ для JWT:**
   ```sh
   mkdir -p secrets
   head -c 64 /dev/urandom | base64 > secrets/jwt_secret.key
   ```

2. **Соберите и запустите сервисы:**
   ```sh
   docker-compose up --build -d
   ```

3. **Проверьте логи приложения:**
   ```sh
   docker-compose logs -f user-service
   ```

4. **Проверьте доступность API:**
   - [http://localhost:8080/](http://localhost:8080/)
   - Swagger (если включён): [http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/)

## Переменные окружения (docker-compose)

- `SPRING_DATASOURCE_URL` — строка подключения к БД (пример: `jdbc:postgresql://userdb:5432/users`)
- `SPRING_DATASOURCE_USERNAME` — имя пользователя БД
- `SPRING_DATASOURCE_PASSWORD` — пароль БД
- `JWT_SECRET_PATH` — путь к секретному ключу для JWT (по умолчанию `/run/secrets/jwt_secret`)

## Структура docker-compose

```yaml
secrets:
  jwt_secret:
    file: ./secrets/jwt_secret.key

services:
  userdb:
    image: postgres:16-alpine
    container_name: user-postgres
    restart: unless-stopped
    environment:
      POSTGRES_DB: users
      POSTGRES_USER: myuser
      POSTGRES_PASSWORD: mypassword
    ports:
      - "5433:5432"
    volumes:
      - user_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U myuser -d users"]
      interval: 10s
      retries: 5

  user-service:
    build: .
    container_name: user-service
    restart: on-failure
    depends_on:
      userdb:
        condition: service_healthy
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://userdb:5432/users
      SPRING_DATASOURCE_USERNAME: myuser
      SPRING_DATASOURCE_PASSWORD: mypassword
      JWT_SECRET_PATH: /run/secrets/jwt_secret
    ports:
      - "8080:8080"
    secrets:
      - jwt_secret

volumes:
  user_data:
```

## Примеры запросов (Postman/cURL)

### Регистрация пользователя
```
POST http://localhost:8080/api/v1/auth/register
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "password123",
  "name": "Test",
  "surname": "User"
}
```

### Логин
```
POST http://localhost:8080/api/v1/auth/login
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "password123"
}
```

### Получить профиль
```
GET http://localhost:8080/api/v1/auth/me
Authorization: Bearer <ваш_JWT_токен>
```

### Обновить профиль
```
PATCH http://localhost:8080/api/v1/auth/me
Authorization: Bearer <ваш_JWT_токен>
Content-Type: application/json

{
  "name": "NewName"
}
```

### Смена пароля
```
PATCH http://localhost:8080/api/v1/auth/change-password
Authorization: Bearer <ваш_JWT_токен>
Content-Type: application/json

{
  "oldPassword": "password123",
  "newPassword": "newpassword456"
}
```

## Импорт коллекции Postman

В проекте есть файл `user-service.postman_collection.json`. Импортируйте его в Postman для быстрого тестирования всех эндпоинтов.

## Контракты и OpenAPI
- Описание API: `openapi.yaml`
- Контракты взаимодействия: `CONTRACTS.yaml`

## Разработка и тесты
- Для локальной разработки можно использовать стандартные Spring Boot команды (`mvn spring-boot:run`), но рекомендуется запуск через Docker Compose для полной совместимости с окружением.

---

**Вопросы и предложения — в Issues или Pull Requests!** 