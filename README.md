# Tiny Ledger

* [Features](#features)
* [Tech Stack](#tech-stack)
* [Running](#running)
* [API](#api)
* [Status Codes](#status-codes)
* [Examples](#examples)
* [API Docs](#api-docs)
* [Tests](#tests)
* [Assumptions](#assumptions)
* [Decisions](#decisions)

## Features

* Deposit money
* Withdraw money
* View current balance
* View transaction history

## Tech Stack

* Java
* Maven
* Spring Boot

## Running

Needs Java 17 and Maven.

```
mvn spring-boot:run
```

Starts on http://localhost:8080.

## API

| Method | Path            | Description              | Success     |
|--------|-----------------|--------------------------|-------------|
| POST   | `/deposits`     | Record a deposit         | 201 Created |
| POST   | `/withdrawals`  | Record a withdrawal      | 201 Created |
| GET    | `/balance`      | Get the current balance  | 200 OK      |
| GET    | `/transactions` | List transaction history | 200 OK      |

## Status Codes

| Code | Meaning               | When it occurs                                            |
|------|-----------------------|-----------------------------------------------------------|
| 200  | OK                    | Balance or history returned                               |
| 201  | Created               | Transaction successfully recorded                         |
| 400  | Bad Request           | Missing, negative, or more than two decimal places amount |
| 422  | Unprocessable Content | Withdrawal exceeds current balance                        |

## Examples

Deposit money:

```
curl -X POST http://localhost:8080/deposits \
  -H "Content-Type: application/json" \
  -d '{"amount": 100.00}'
```

Withdraw money:

```
curl -X POST http://localhost:8080/withdrawals \
  -H "Content-Type: application/json" \
  -d '{"amount": 30.00}'
```

Check the balance:

```
curl http://localhost:8080/balance
```

```
{"balance": 70.00, "currency": "EUR"}
```

List the transaction history:

```
curl http://localhost:8080/transactions
```

Withdrawing more than the balance returns `422`:

```
curl -X POST http://localhost:8080/withdrawals \
  -H "Content-Type: application/json" \
  -d '{"amount": 999.00}'
```

## API Docs

With the app running, the OpenAPI spec and Swagger UI are available at:

| What         | URL                                    |
|--------------|----------------------------------------|
| Swagger UI   | http://localhost:8080/swagger-ui.html  |
| OpenAPI JSON | http://localhost:8080/v3/api-docs      |
| OpenAPI YAML | http://localhost:8080/v3/api-docs.yaml |

## Tests

```
mvn test
```

## Assumptions

* For simplicity, the tiny-ledger will be defined for a single user
* Concurrent operations were considered for that single user
* Single currency with EUR considered
* Only positive amounts were considered
* All money is limited to 2 decimal places
* Transaction history is returned in insertion order
* Deposit and withdrawals are separate api calls for extra security when making the api call
* In memory data, so everything will reset on restart
* No authentication/authorisation, logging/monitoring, or atomic/transactional operations, since the task states these are not expected

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

* The app should be simple, so it does not need a Map where each key would be an account
* An ArrayList could be enough, but it is not thread safe
* A synchronized list was also considered, but it would need dedicated error handling and a locking strategy with
  synchronized blocks
* CopyOnWriteArrayList was chosen because it does not need to lock during operations. It is slower to use because it
  copies the array each time, but that is a tradeoff for safer operations and simplicity
