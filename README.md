
````markdown
# 📝 Collaborative Editing System

A Spring Boot + React microservices-based collaborative document editor with real-time editing, version control, and automated JUnit testing.  
Supports WebSocket-based live collaboration and REST APIs for managing users, documents, and versions.  

---
## 🚀 Features

- Microservices Architecture
  - User Service — manages registration, login, and user profiles.
  - Document Service — handles document creation, editing, and retrieval.
  - Version Service — stores and manages version history of documents.
- API Gateway + WebSocket Hub
  - Unified entry point for all HTTP/WebSocket communication.
- Real-Time Collaboration
  - WebSocket-powered document editing with instant updates.
- Automated Testing
  - JUnit tests run automatically on `mvn clean install`.
- Modern Frontend
  - React.js frontend with clean UI, glassmorphism enhancements, and light/dark mode.

---

## 🏗 Architecture Overview

```text
+----------------+   +----------------+   +----------------+
| User Service   |<->| Document Svc   |<->| Version Svc    |
+----------------+   +----------------+   +----------------+
       ^                      ^                     ^
       |                      |                     |
+------------- API Gateway / WebSocket Hub -------------+
                           ^
                           |
                   +----------------+
                   |   Frontend     |
                   +----------------+
````

---

<img src="https://github.com/Pbhacks/collab-editing-system/blob/main/Project_Images/8.png" alt="Architecture Diagram" width="600"/>

## 📦 Microservices Overview

### 1️⃣ User Service

**Purpose:** Manage user accounts, authentication, and roles.
**Key APIs:**

* `POST /api/users/register` — Register new user.
* `POST /api/users/login` — Authenticate and generate session token.
* `GET /api/users/{id}` — Fetch user profile.

### 2️⃣ Document Service

**Purpose:** Handle document CRUD operations.
**Key APIs:**

* `POST /api/docs/create` — Create a document.
* `PUT /api/docs/edit/{id}` — Update document content.
* `GET /api/docs/user/{ownerId}` — Get documents by owner.

### 3️⃣ Version Service

**Purpose:** Manage document version history.
**Key APIs:**

* `POST /api/versions/save` — Save document version.
* `GET /api/versions/history/{docId}` — Retrieve version history.
* `GET /api/versions/{versionId}` — Get specific version content.

---

## ⚙️ Tech Stack

* **Backend:** Java 17, Spring Boot 3.x, Spring Data JPA, WebSockets
* **Frontend:** React.js, Axios, Styled Components
* **Database:** H2 (dev) (temporary, can use MongoDB in advanced stage)
* **Build Tool:** Maven
* **Testing:** JUnit 5, Mockito
* **Gateway:** Spring Cloud Gateway

---

## 🧪 Automated Testing

JUnit tests are integrated into each microservice.
They run automatically when executing:

```bash
mvn clean install
```

You can run tests for a single service:

```bash
cd user-service
mvn clean install
mvn spring-boot:run
```
<img src="https://github.com/Pbhacks/collab-editing-system/blob/main/Project_Images/2.png" alt="Architecture Diagram" width="600"/>
---

## ▶️ Running the Project

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/yourusername/collab-editing-system.git
cd collab-editing-system
```

### 2️⃣ Start Backend Services

In separate terminals (or using an IDE like IntelliJ):

```bash
cd user-service
mvn clean install
mvn spring-boot:run
```
<img src="https://github.com/Pbhacks/collab-editing-system/blob/main/Project_Images/1.png" alt="Architecture Diagram" width="600"/>

```bash
cd document-service
mvn clean install
mvn spring-boot:run
```

```bash
cd version-service
mvn clean install
mvn spring-boot:run
```

```bash
cd api-gateway
mvn clean install
mvn spring-boot:run
```

### 3️⃣ Start Frontend

<img src="https://github.com/Pbhacks/collab-editing-system/blob/main/Project_Images/4.png" alt="Architecture Diagram" width="600"/>
```bash
cd frontend
npm install
npm start
```

---

## 🎨 UI Modes

* **Light Mode:** Soft peach background for a warm, minimal look.
* **Dark Mode:** Navy blue + black gradient for reduced eye strain.
* **Glassmorphism:** Applied to key UI components like modals, toolbars, and dashboards.

---

## 🔌 API Gateway Configuration

Routes all frontend requests to backend services:

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: http://localhost:8081
          predicates:
            - Path=/api/users/**
        - id: document-service
          uri: http://localhost:8082
          predicates:
            - Path=/api/docs/**
        - id: version-service
          uri: http://localhost:8083
          predicates:
            - Path=/api/versions/**
```

---

## 🧩 WebSocket Endpoints

* `ws://localhost:8080/ws` — Real-time editing channel.

---

## 🛡 Security

* JWT-based authentication for protected routes.
* CORS configured for frontend-backend communication.
* Input sanitization to prevent XSS.
* CORS testing framework structured and used

<img src="https://github.com/Pbhacks/collab-editing-system/blob/main/Project_Images/7.png" alt="Architecture Diagram" width="600"/>
---

## 📜 License

MIT License — feel free to modify and distribute.

---

```

If you want, I can also **embed screenshots** of the running app and a **GIF of real-time editing** into this README so it looks even better on GitHub. That would make it stand out.
```
