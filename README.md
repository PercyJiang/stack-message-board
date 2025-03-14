# message-board

## workflow

1. create a client with username and password
2. login as the client (gets jwt)
3. operate messages once logged in

## tech stack

- **operations** - authn, crud, rest api, graphql, queue

- **frontend** - react

- **backend** - spring + java, django + python

- **async** - rabbitmq

- **data store** - sqlite, postgres, redis, minio

- **aws** - rds, ec2, ecs, eks, sqs, lambda, dynamodb, s3

## to run locally

### the-spring

1. set java version to 17

2. run `mvn install`

3. create a local postgres database with docker like `docker run --name test_postgres --publish 5432:5432 --env POSTGRES_DB=test_postgres --env POSTGRES_USER=localuser --env POSTGRES_PASSWORD=localpassword --detach postgres:16.2
`

4. run the java app with the following environment 
```
DB_HOST=localhost;
DB_NAME=test_postgres;
DB_PASSWORD=localpassword;
DB_PORT=5432;
DB_USERNAME=localuser;
USE_RABBITMQ=false;
```

5. hit http://localhost:8080/swagger-ui/index.html

### the-react

1. setup the-spring locally at 8080

2. run `npm install`

3. run `npm run start`

### the-express

1. run rabbitmq locally `docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4.0-management`

2. run minio locally `docker run -it --rm --name minio -p 9000:9000 -p 9001:9001 quay.io/minio/minio server /data --console-address ":9001"
`

3. in `index.ts` replace redis password and host with your redis account

4. run `npm run start`

5. in the-spring, change environment variable `USE_RABBITMQ=false;`

6. setup the-spring locally at 8080

7. post user brief and profile pic with spring apis

## to run on aws

ensure aws cli is logged in

### ecs

1. in `main.tf`, set `deploy_rds` and `deploy_ecs` to true, other deploys to false

2. run `terraform init`

3. run `terraform apply`

4. get the ec2 Public IPv4 DNS, for example: ec2-12-345-678-90.compute-1.amazonaws.com

5. hit http://ec2-12-345-678-90.compute-1.amazonaws.com:8080/swagger-ui/index.html

6. when finished, run `terraform destroy`
