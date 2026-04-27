# ToDoRails Elite Solution

A secure, scalable backend for a task management application built as part of the **Amazon Coursera Junior Software Developer Course**.

## About the Project
This backend system was developed to demonstrate core backend development skills including security,
data management, validation, and testing using Java and the Spring ecosystem.

## Features
- 🔒 **Security** — Role-based authentication, protected endpoints & secure password encryption
- ✅ **Task Management** — Full CRUD operations with seamless database interaction & error handling
- 📋 **Validation & Logging** — Meaningful input validation, useful error responses & essential event logging
- 🧪 **Testing** — Comprehensive unit & integration tests with high coverage across service and controller classes

## Tech Stack
| Technology | Purpose |
|------------|---------|
| Java | Core language |
| Spring Boot | Backend framework |
| Spring Security | Authentication & authorization |
| MySQL | Relational database |
| JUnit | Testing |

## Getting Started (Local Setup)
1. Clone the repo
```bash
   git clone https://github.com/Tek-Sheng-Qi/todorails-elite-solution.git
```
2. Make sure MySQL is running locally
3. Update `application.properties` with your local credentials if different
4. Run the app
```bash
   ./mvnw spring-boot:run
```

## Deployment
This app is deployed on Railway with a managed MySQL instance.
All sensitive configuration is handled via environment variables.

## Live Demo
🔗 [ToDoRails Elite Solution](todorails-elite-solution-production.up.railway.app)