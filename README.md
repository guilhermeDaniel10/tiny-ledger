# Tiny Ledger

## Functional Requirements

* Deposit money
* Withdraw money
* View current balance
* View transaction history

## Tech Stack

* Java
* Maven
* Spring

## Running

Needs Java 17 and Maven.

```
mvn spring-boot:run
```

Starts on http://localhost:8080.

## API

| Method | Path             | Description               | Success     |
|--------|------------------|---------------------------|-------------|
| POST   | `/deposits`      | Record a deposit          | 201 Created |
| POST   | `/withdrawals`   | Record a withdrawal       | 201 Created |
| GET    | `/balance`       | Get the current balance   | 200 OK      |
| GET    | `/transactions`  | List transaction history  | 200 OK      |

## API Docs

With the app running, the OpenAPI spec and Swagger UI are available at:

| What         | URL                                            |
|--------------|------------------------------------------------|
| Swagger UI   | http://localhost:8080/swagger-ui.html          |
| OpenAPI JSON | http://localhost:8080/v3/api-docs              |
| OpenAPI YAML | http://localhost:8080/v3/api-docs.yaml         |

## Status Codes

| Code | Meaning              | When it occurs                                                         |
|------|----------------------|------------------------------------------------------------------------|
| 200  | OK                   | Balance or history returned                                            |
| 201  | Created              | Transaction successfully recorded                                      |
| 400  | Bad Request          | Missing, negative amount, more than two decimal places or unknown type |
| 422  | Unprocessable Entity | Withdrawal exceeds current balance                                     |

## Assumptions

* For simplicity, the tiny-ledger will be defined for a single user
* Single currency with € considered
* In memory data, so everything will reset on restart
* No authentication, persistence, logging, according to the task description

## Decisions

### Money type

Floating points would not be precise for decimal values and operations on them because decimal values are rounded
making the operations not precise

Because of that, two options can be considered: **integer with cents** and **BigDecimal**

**Pros:**

* integer
    * fast operations
    * fixed at cents, which means that no rounding is needed
* BigDecimal
    * grows as needed
    * more readable as it saves as base 10
    * rounding

**Cons:**

* integer
    * would force the constant conversions to use cents
    * not as readable
    * small and fixed size
* BigDecimal:
    * speed for operations not as fast as integer

**Decision:**
BigDecimal - For money operations, in java applications, the standard is to use BigDecimal instantiated with Strings.
The pros outweigh the integer ones.

### Transactions

Transactions will be appended only so they can be immutable and keep a clear transaction history.

### Transaction Collection
