# Module Loans - Biblioteca API

## Table of Contents
- [Overview](#overview)
- [Team Members](#team-members)
- [Features](#features)
- [Technologies](#technologies)
- [Getting Started](#getting-started)
- [Endpoints](#endpoints)
- [Class Diagram](#class-diagram)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

The **Module Loans** API manages the loan and return process for books in a library. It supports functionalities such as registering loans, managing due dates, and imposing fines for late returns. The system is designed to ensure a streamlined and efficient process for library management.

---


## Team Members

- [Zayra Gutierres](https://github.com/ZayraGS1403)    
- [Sebastian Cardona](https://github.com/SebastianCardona-P)
- [Diego Cardenas](https://github.com/diegcard)
- [Andres Serrato](https://github.com/andresserrato2004)


---

## Features
- **Loan Management**: Create, update, and delete book loans.
- **Fine Calculation**: Automatically calculates fines for overdue books.
- **Status Tracking**: Tracks loans by their status (`Prestado`, `Vencido`, `Devuelto`).
- **Student and Book Validations**: Ensures a student cannot borrow multiple books at the same time and checks book availability.
- **API Documentation**: Fully documented endpoints using Swagger/OpenAPI.

---

## Technologies
- **Java 17**
- **Spring Boot 3.0**
- **MongoDB**: For loan persistence.
- **Swagger/OpenAPI**: For API documentation.
- **Docker** (optional): For deployment and containerization.

---

## Getting Started

### Prerequisites
1. Java 17 or higher
2. MongoDB instance running locally or remotely
3. Maven 3.x for dependency management

### Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/Bichotas-Season/BichotaB
   cd moduloprestamos

2. Update the environment variables in the application.properties file. For example:
    ```bash
    FRONTEND_URL=http://your-frontend-url.com
    SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/modulo_prestamos

3. Run the application:
    ```bash
    mvn spring-boot:run

4. Access the API documentation:
    * Swagger URL: https://app.swaggerhub.com/apis-docs/DIEGOSP778/modulo-prestamos_api/1.0#/

## Endpoints

#### Loan Operations

| Method | Endpoint                   | Description                                         |
|--------|----------------------------|-----------------------------------------------------|
| POST   | `/prestamos`               | Create a new loan                                   |
| GET    | `/prestamos`               | Retrieve all loans                                  |
| GET    | `/prestamos-prestados`     | Retrieve loans with status `Prestado`              |
| GET    | `/prestamos/{id}`          | Retrieve loan details by ID                        |
| GET    | `/prestamos/libro/{isbn}`  | Retrieve loans by book ISBN                        |
| GET    | `/prestamos/estudiante/{id}` | Retrieve loans by student ID                      |
| DELETE | `/prestamos/delete-{id}`   | Delete a loan (if conditions are met)              |

### Aditional Configurations
1. **CORS:** Configured to allow only frontend requests from the defined FRONTEND_URL.
2. **Swagger Config:** API details and team contact information included.

## Class Diagram
The following UML diagram provides an overview of the module's main components:


## Contributing

Contributions are welcome! Please follow the GitHub Flow.
1. Fork the project.
2. Create a feature branch.
3. Commit your changes.
4. Open a pull request.

## License

This project don´t have license yet.
