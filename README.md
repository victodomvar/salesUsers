# User Management Service

Initial template for a user-management microservice focused on the sales domain.

## Scope of this first step

- Spring Boot application bootstrap
- Base package structure
- Placeholder REST endpoint
- Basic actuator exposure
- Test skeleton

## Planned next steps

- Define the user aggregate and persistence model
- Add a database with migrations
- Implement CRUD operations
- Add validation, exception handling, and security
- Add Docker and CI support

## Run locally

```bash
mvn spring-boot:run
```

The service starts on port `8080`.

## Placeholder endpoint

```text
GET /api/v1/users
```

Current behavior: returns `501 Not Implemented` to mark the API entry point without committing to the final contract yet.

