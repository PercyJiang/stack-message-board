# message-board

## tech stack

### frontend

- React

### backend

- Auth

- CRUD Operations

- Spring Boot

- Django

### cloud

- RDS

- ECS, EC2

- EKS, EC2

- LMM

## to run

clone the repo

### the-spring

**java version** - coretto-17

```
cd the-spring
mvn install
```

create a local postgres database with docker
`docker run --name test_postgres --publish 5432:5432 --env POSTGRES_DB=test_postgres --env POSTGRES_USER=localuser --env POSTGRES_PASSWORD=localpassword --detach postgres:16.2
`

run the java app from intellij with the following environment variables
DB_HOST=localhost;
DB_NAME=test_postgres;
DB_PASSWORD=localpassword;
DB_PORT=5432;
DB_USERNAME=localuser;
