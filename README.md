# message-board

1. create a client with username and password
2. login as the client (gets jwt)
3. operate messages once logged in

## tech stack

### frontend

- react

### backend

- authn, crud, rest api, graphql

- spring boot with java, django with python

### cloud

- rds

- ecs, ec2

- eks, ec2

## to run

clone the repo

### the-spring

**java version** - coretto-17

#### local

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

hit http://localhost:8080/swagger-ui/index.html

#### ecs

ensure aws cli is logged in

```
cd terraform
terraform init
```

in main.tf, set deploy_rds and deploy_ecs to true, other deploys to false

`terraform apply`

get the ec2 Public IPv4 DNS
for example: ec2-54-159-193-56.compute-1.amazonaws.com
hit http://ec2-54-159-193-56.compute-1.amazonaws.com:8080/swagger-ui/index.html

`terraform destroy`

### the-react

#### local

setup the-spring locally at 8080

```
npm install
npm run start
```
