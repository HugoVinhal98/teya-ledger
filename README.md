# Teya Ledger

## Quick Start

### Option 1: Using Docker (Recommended)

```bash
docker compose up
```

The API will be available at `http://localhost:8080`.

**Note:** The JAR file is pre-built and committed to the repository for instant Docker startup. If you modify the code, rebuild with `./gradlew bootJar` before running Docker.

### Option 2: Using Gradle

```bash
./gradlew bootRun
```

## Prerequisites

- **Docker & Docker Compose** (for Docker option)
- **Java 21** (for Gradle option)

## API

Once the application is running, access the interactive API documentation:

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI Spec**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

The Swagger UI provides an interactive interface to explore and test all API endpoints.

## Available Endpoints

- `POST /api/transactions` - Record a `DEPOSIT` or `WITHDRAWAL` transaction
- `GET /api/balance` - Retrieve the current account balance
- `GET /api/transactions` - Get transaction history (sorted by timestamp, newest first)

## Architecture Overview

This project follows **Clean Architecture** principles with clear separation of concerns:

### Layers

- **Presentation Layer** (`presentation/`)
  - REST controllers with OpenAPI documentation
  - Request/Response DTOs
  - Input validation

- **Application Layer** (`application/`)
  - Application services orchestrating business logic
  - Command objects
  - Response DTOs

- **Domain Layer** (`domain/`)
  - Core business models and rules
  - Repository interfaces
  - Domain factories and validation
  - Custom exceptions

- **Infrastructure Layer** (`infrastructure/`)
  - Repository implementations (in-memory)

### Design Decisions

- **In-Memory Storage**: As suggested in the coding assignment
- **Validated Wrapper Pattern**: Custom validation framework ensuring domain invariants are always maintained
- **Dependency Inversion**: Domain layer defines interfaces; infrastructure layer provides implementations

## Testing

The project includes comprehensive test coverage across multiple levels:

### Unit Tests
- **Domain Logic**: Transaction validation, business rules
- **Services**: Application service behavior with mocked dependencies
- **Repositories**: In-memory repository operations

### Integration Tests
- **Controllers**: End-to-end API tests without mocks

### Running Tests

```bash
./gradlew test
```
