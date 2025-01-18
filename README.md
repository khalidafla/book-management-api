# Book Management API

This is a REST API for managing books, built using **Spring Boot**. The API allows users to perform CRUD operations on books and authors, including functionality for updating book details, retrieving book information, and managing authors.

## Author

This project was created by **Khalid Ait Laasri**.

## Features

- **Create** a new book with author details.
- **Retrieve** book information by ID.
- **Update** book details including title, type, publication date, and author.
- **Delete** books from the system.
- **Handle** exceptions such as missing authors or non-existing books gracefully.

## Technologies Used

- **Spring Boot**: The core framework used to build the REST API.
- **Spring Data JPA**: For database interaction and entity management.
- **H2 Database**: An in-memory database used for testing and development purposes.
- **JUnit 5**: For unit testing and test-driven development.
- **Postman**: Used to test the API endpoints. A Postman collection is included for easy testing.

## Setup & Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/khalidafla/book-management-api.git
    cd book-management-api
    ```

2. Build the project:

    ```bash
    ./mvnw clean install
    ```

3. Run the application:

    ```bash
    ./mvnw spring-boot:run
    ```

   The API will be accessible at `http://localhost:8080/api/books`.

## API Documentation

The project includes a Postman collection to test the API endpoints. You can import the collection into Postman and start testing right away.

- File name: **mini-project.postman_collection.json**
- To import into Postman:
    1. Open Postman.
    2. Click on the **Import** button.
    3. Select the **mini-project.postman_collection.json** file.