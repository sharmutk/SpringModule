
![image](https://github.com/user-attachments/assets/6f36bf31-e97f-45ed-9cf7-3685857db57b)


# javaSLP Library Management REST API

## Overview

This project is a Spring Boot RESTful API for managing a library system. It supports multiple user roles (Student, Professor, Admin, Librarian) and allows for book management, borrowing, returning, and reporting. The API demonstrates clear separation of concerns through layered architecture: **Controller → Service → Model**.

---

## API Request Lifecycle Explained

Whenever an API call is made (e.g., from Insomnia, curl, frontend), the following internal flow is followed:

1. **User or Client**
    - Sends an HTTP request to a specific REST API endpoint.
2. **Controller Layer**
    - Receives the HTTP request.
    - Performs request parameter extraction and basic validation.
    - Delegates business logic execution to the Service layer.
3. **Service Layer**
    - Implements all business rules and main application logic.
    - Updates, queries, or aggregates data using the in-memory data structures (no database, unless extended).
    - Returns processed results or responses to the Controller.
4. **Model Layer**
    - Represents the data structure (e.g., Book, User, IssuedBook).
    - Used throughout service and controller logic for data transfer.
5. **Response**
    - Controller sends back a JSON response to the user/client.

---

## Lifecycle Example: Registering a User

1. **Client** sends:
    ```
    POST /api/javaslp/users/register?username=alice&role=STUDENT
    ```

2. **Controller** (`JavaSlpController.java`)
    - Maps the POST request to the `registerUser()` method.
    - Reads `username` and `role` from the request.
    - Calls `registerUser(username, role)` in the service.

3. **Service** (`JavaSlpService.java`)
    - Receives the username and role.
    - Checks if this user exists.
    - If not, it creates a `User` object and adds it to the in-memory user map.
    - Returns a success/failure message to the controller.

4. **Controller**
    - Prepares a JSON message.
    - Sends the HTTP response to the client.

---

## Lifecycle Example: Borrowing (Requesting) a Book

1. **Client** sends:
    ```
    POST /api/javaslp/users/alice/request/Harry%20Potter
    ```

2. **Controller**
    - Maps the endpoint to `borrowBook()`.
    - Receives path variables for user and book.
    - Calls `requestBook(username, bookName)` in service.

3. **Service**
    - Checks if the user exists and is allowed another book.
    - Searches for the requested book in the inventory.
    - If available, issues the book and updates related data structures.
    - Adds the entry to issuedBooks.
    - Returns a result message.

4. **Controller**
    - Sends the result as JSON back to the caller.

---

## Visual Flow: Example (for `/users/register`)

```text
      Client
        |
        v
  -----------------------
  |  Controller Layer   | <-- receives request, parses params
  -----------------------
        |
        v
  -----------------------
  |   Service Layer     | <-- business logic, checks, updates
  -----------------------
        |
        v
  -----------------------
  |     Model Data      | <-- Book, User, IssuedBook objects
  -----------------------
        |
        v
      Controller
        |
        v
     HTTP JSON response


