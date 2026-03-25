# User Management Service

`user-management-service` is a small Spring Boot 3.3.5 microservice built with Java 17. It exposes a simple user-creation endpoint and is structured as a conventional layered service with controller, service, DTO, domain, repository, mapper, configuration, and exception packages.

## Project structure

```text
src
├── main
│   ├── java/com/salesusers/usermanagement
│   │   ├── config
│   │   ├── controller
│   │   ├── domain
│   │   ├── dto
│   │   │   ├── request
│   │   │   └── response
│   │   ├── exception
│   │   ├── mapper
│   │   ├── repository
│   │   ├── service
│   │   └── UserManagementServiceApplication.java
│   └── resources
│       ├── application.yml
│       └── openapi/user-management.yaml
└── test
    └── java/com/salesusers/usermanagement
        ├── controller
        └── UserManagementServiceApplicationTests.java
```

## API

The service currently keeps users in memory and exposes:

- `POST /users`
- `GET /actuator/health`
- `GET /actuator/health/liveness`
- `GET /actuator/health/readiness`

Example request:

```bash
curl -X POST http://localhost:8080/users \
  -H 'Content-Type: application/json' \
  -d '{"name":"Alice Doe","email":"alice@example.com"}'
```

## Build and run

Run tests:

```bash
mvn test
```

Run locally:

```bash
mvn spring-boot:run
```

The application listens on port `8080` by default.

## Container build

Build the image locally:

```bash
docker build -t salesusers:local .
```

Run the container:

```bash
docker run --rm -p 8080:8080 salesusers:local
```

## Kubernetes deployment

Apply the included manifests:

```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
```

Port-forward locally:

```bash
kubectl -n deibidsales port-forward service/salesusers 8080:80
```

## Notes

- Persistence is intentionally in-memory for now.
- Security and database integration are intentionally not included in this refactor.
- The OpenAPI contract remains under `src/main/resources/openapi/user-management.yaml`.
