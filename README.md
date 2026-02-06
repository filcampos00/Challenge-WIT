# Challenge-WIT
Multi-module Spring Boot project with a REST API and a calculator worker communicating via Kafka.

## Modules
- `rest`: REST API service (port 8080)
- `calculator`: worker service that processes operations
- `common`: shared DTOs and enums

## Build and run (Docker Compose)
From the project root:
```
docker-compose up --build
```
To stop services:
```
docker-compose down
```

## Access and logs
- REST API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- Logs are persisted to `rest/logs` and `calculator/logs` on the host.
