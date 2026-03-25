# User Management Service

`user-management-service` is a small Spring Boot 3.3.5 API built with Java 17 and Maven. It exposes a single user-creation endpoint, persists users with Spring Data JPA, initializes its schema with Flyway, and generates its API contract types from OpenAPI Generator during the Maven build.

## API

Create a user:

```http
POST /api/v1/users
Content-Type: application/json
```

Request body:

```json
{
  "name": "Jane Doe",
  "email": "jane@example.com"
}
```

Successful response:

```json
{
  "id": "0d9d6f53-719d-4af3-9ff4-67b6dfb1db2d",
  "name": "Jane Doe",
  "email": "jane@example.com",
  "createdAt": "2026-03-25T12:00:00Z"
}
```

## Database

The service supports:

- local/test default: H2
- Kubernetes deployment: PostgreSQL

Flyway initializes the schema from:

```text
src/main/resources/db/migration/V1__create_users_table.sql
```

## Build and run

Verify the project:

```bash
mvn verify
```

Run locally with the default H2 configuration:

```bash
mvn spring-boot:run
```

The application listens on port `8080` by default and exposes:

- `POST /api/v1/users`
- `GET /actuator/health`
- `GET /actuator/health/liveness`
- `GET /actuator/health/readiness`

Example request:

```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H 'Content-Type: application/json' \
  -d '{"name":"Jane Doe","email":"jane@example.com"}'
```

## Docker

Application image name:

```text
victodomvar/salesusers
```

Build locally:

```bash
docker build -t victodomvar/salesusers:local .
```

Run locally:

```bash
docker run --rm -p 8080:8080 victodomvar/salesusers:local
```

## GitHub Actions

The workflow in `.github/workflows/develop.yml`:

1. runs `mvn verify`
2. builds and pushes `victodomvar/salesusers`
3. deploys PostgreSQL and the application manifests from `k8s/`

Published tags:

- `victodomvar/salesusers:<git-sha>`
- `victodomvar/salesusers:develop-latest`

Required repository secrets:

- `DOCKERHUB_USERNAME`
- `DOCKERHUB_TOKEN`
- `KUBE_CONFIG_BASE64`

## Kubernetes deployment

The included manifests deploy:

- `salesusers-postgres` using `postgres:16-alpine`
- `salesusers` configured to connect to that PostgreSQL service

Apply manually if needed:

```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/postgres-secret.yaml
kubectl apply -f k8s/postgres-deployment.yaml
kubectl apply -f k8s/postgres-service.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
```

Port-forward locally:

```bash
kubectl -n deibidsales port-forward service/salesusers 8080:80
```

## Notes

- The PostgreSQL manifest uses a simple in-cluster deployment with development credentials.
- Security is intentionally not included.
- The OpenAPI contract remains under `src/main/resources/openapi/user-management.yaml`.
- The request/response contract used by the controller is generated at build time by the OpenAPI Generator Maven plugin.
