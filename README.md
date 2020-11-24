# tx-playground

A little project that persists an entity and forces a UK violation exception, with different ways to handle rollbacks.

## Pre-requisites

- Java 11+
- MySQL 8+

## Running

```sh
./mvnw spring-boot:run
```

It uses `root` without any password while connecting to MYSQL.

You can change the MySQL's user and password with something like:

```sh
SPRING_DATASOURCE_USERNAME=another-user SPRING_DATASOURCE_PASSWORD=another-pass ./mvnw spring-boot:run
```

## Testing

1. When calling http://localhost:9090/same-tx-without-shut-up-catch you'll get an `HTTP 500` with the original exception: `org.hibernate.exception.ConstraintViolationException`. The code does not catch the exception.
2. When calling http://localhost:9090/same-tx-with-shut-up-catch you'll get an `HTTP 500` with an `org.springframework.transaction.UnexpectedRollbackException: Transaction silently rolled back because it has been marked as rollback-only`. The code catches the exception and does not with it. Even so, as the transaction is marked as rolled back, Spring throws `UnexpectedRollbackException`.
3. When calling http://localhost:9090/another-tx-with-shut-up-catch you'll get an `HTTP 200`. The service which throws the original exception uses another transaction, with `@Transactional(propagation = Propagation.REQUIRES_NEW)`.