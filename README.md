# Core Product Management Shred

Hexagonal Architecture implementation for product catalog management

## Setup

- Ensure Maven and Docker are installed.
- Configure database in `src/main/resources/application.yml` or via environment variables.

## Running

- Build the application:

  ```bash
  mvn clean package
  ```

- Start with Docker Compose:

  ```bash
  docker-compose up
  ```

## API Documentation

Swagger UI available at: `http://localhost:8080/swagger-ui.html`

## Testing

Run unit and integration tests with:

```bash
mvn test
```

## Endpoints

- POST `/api/products`
- GET `/api/products/{id}`
- PUT `/api/products/{id}`
- DELETE `/api/products/{id}`
- GET `/api/products`
- POST `/api/skus`
- POST `/api/categories`
- GET `/api/categories/tree`
