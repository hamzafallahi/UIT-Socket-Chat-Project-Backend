# UITProject Microservices (Empty Starter)

This workspace contains 3 empty Spring Boot microservices:

- chat-ms (port 8082)

Each microservice has its own PostgreSQL database and all services can be started using Docker Compose.

## Prerequisites

- Docker Desktop

## Run everything

```bash
docker compose up --build -d
```

## Check microservices


- Chat: http://localhost:8082/api/ping


## pgAdmin

- URL: http://localhost:5050
- Email: admin@uit.local
- Password: admin123

### DB connection settings inside pgAdmin

Hostnames:

- chat-db


Ports inside Docker network:
- 5432 for each container

Credentials:

- chat-db: chat_user / chat_pass / chat_db


## Stop everything

```bash
docker compose down
```

If you also want to remove DB volumes:

```bash
docker compose down -v
```
