# Ticket Management System

## Project Description

The Ticket Management System is a Spring Boot based application used to manage support tickets within an organization.

The application allows employees to raise tickets, support engineers to work on assigned tickets, and administrators/managers to track the complete lifecycle of a ticket.

The system supports ticket assignment, status tracking, ticket history management, searching/filtering, validation, exception handling and automatic ticket assignment using different assignment strategies.


## Features

### User Management
- Create User
- View User
- Update User
- Delete User

### Ticket Management
- Create Ticket
- Assign Ticket to Support Engineer
- Auto Assign Ticket
- Resolve Ticket
- Close Ticket
- Reopen Ticket

### Search & Filtering
- Search Tickets by Status
- Search Tickets by Priority
- View All Tickets

### Tracking
- Maintain Ticket History
- Status Transition Tracking

### Assignment Strategies
- Round Robin Assignment
- Least Loaded Assignment


## Technology Stack

### Backend
- Java 21
- Spring Boot

### Database
- MySQL

### Persistence
- Spring Data JPA
- Hibernate

### Build Tool
- Gradle

### Testing
- JUnit 5
- Mockito

### API Testing
- Postman

### Additional Libraries
- Lombok


## Architecture

The application follows a layered architecture:

Controller Layer
↓
Service Layer
↓
Repository Layer
↓
Database

### Controller Layer
Handles incoming REST API requests and returns responses.

### Service Layer
Contains business logic, validations, workflow rules and transaction management.

### Repository Layer
Performs database operations using Spring Data JPA.

### Database Layer
Stores Users, Tickets and Ticket History data in MySQL.


### Application Flow

```text
Client
  │
  ▼
Controller
  │
  ▼
Service
  │
  ▼
Repository
  │
  ▼
MySQL Database


## Database Design

The application consists of three main entities:

### User

Represents employees and support engineers.

Fields:

- id
- employeeId
- name
- email
- department
- role

### Ticket

Represents a support ticket raised by an employee.

Fields:

- id
- ticketNumber
- title
- description
- priority
- status
- createdDate
- createdBy
- assignedTo

### TicketHistory

Maintains the audit history of ticket updates.

Fields:

- id
- ticket
- oldStatus
- newStatus
- performedBy
- comments
- createdDate

## Entity Relationship Diagram

```text
+------------------+
|      User        |
+------------------+
| id (PK)          |
| employeeId       |
| name             |
| email            |
| department       |
| role             |
+------------------+

         1
         |
         |
         *
+------------------+
|     Ticket       |
+------------------+
| id (PK)          |
| ticketNumber     |
| title            |
| description      |
| priority         |
| status           |
| createdDate      |
| createdBy (FK)   |
| assignedTo (FK)  |
+------------------+

         1
         |
         |
         *
+------------------+
|  TicketHistory   |
+------------------+
| id (PK)          |
| ticketId (FK)    |
| oldStatus        |
| newStatus        |
| performedBy      |
| comments         |
| createdDate      |
+------------------+
```

## JPA Relationships

### Ticket → User

Many tickets can be created by one user.

```java
@ManyToOne
private User createdBy;
```

### Ticket → Support Engineer

Many tickets can be assigned to one support engineer.

```java
@ManyToOne
private User assignedTo;
```

### TicketHistory → Ticket

Many history records belong to one ticket.

```java
@ManyToOne
private Ticket ticket;
```

## API List

### User Management APIs

#### Create User

```http
POST /api/users
```

#### Get All Users

```http
GET /api/users
```

#### Get User By ID

```http
GET /api/users/{id}
```

#### Update User

```http
PUT /api/users/{id}
```

#### Delete User

```http
DELETE /api/users/{id}
```

---

### Ticket Management APIs

#### Create Ticket

```http
POST /api/tickets
```

#### Get Ticket By ID

```http
GET /api/tickets/{id}
```

#### Assign Ticket

```http
PUT /api/tickets/{ticketId}/assign/{userId}
```

#### Auto Assign Ticket

```http
PUT /api/tickets/{ticketId}/auto-assign
```

---

### Ticket Workflow APIs

#### Resolve Ticket

```http
PUT /api/tickets/{ticketId}/resolve
```

#### Close Ticket

```http
PUT /api/tickets/{ticketId}/close
```

#### Reopen Ticket

```http
PUT /api/tickets/{ticketId}/reopen
```

---

### Search APIs

#### Search By Status

```http
GET /api/tickets/search?status=OPEN
```

#### Search By Priority

```http
GET /api/tickets/search?priority=HIGH
```

#### Get All Tickets

```http
GET /api/tickets/search
```


## Setup Instructions

### Prerequisites

Make sure the following software is installed:

- Java 21
- MySQL
- Gradle
- IntelliJ IDEA (Recommended)
- Postman

### Clone Repository

```bash
git clone <repository-url>
```

### Navigate to Project

```bash
cd ticket-management
```

### Configure Database

Update the database configuration in:

```properties
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ticket_management
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```


## How To Run

### Build Project

Windows:

```bash
.\gradlew build
```

### Run Application

Windows:

```bash
.\gradlew bootRun
```

### Run Unit Tests

Windows:

```bash
.\gradlew test
```

### Application URL

```text
http://localhost:8080
```


## Sample Requests and Responses

### Create User

#### Request

```json
{
  "employeeId": "EMP001",
  "name": "Prajna",
  "email": "prajna@gmail.com",
  "department": "IT",
  "role": "EMPLOYEE"
}
```

#### Response

```json
{
  "id": 1,
  "employeeId": "EMP001",
  "name": "Prajna",
  "email": "prajna@gmail.com",
  "department": "IT",
  "role": "EMPLOYEE"
}
```

---

### Create Ticket

#### Request

```json
{
  "title": "Server Down",
  "description": "Production server unavailable",
  "priority": "HIGH",
  "createdBy": 1
}
```

#### Response

```json
{
  "id": 1,
  "ticketNumber": "TKT-1001",
  "title": "Server Down",
  "priority": "HIGH",
  "status": "OPEN"
}
```

---

### Auto Assign Ticket

#### Response

```json
{
  "id": 1,
  "ticketNumber": "TKT-1001",
  "status": "ASSIGNED",
  "assignedToName": "Krishna"
}
```

---

### Search Tickets

#### Request

```http
GET /api/tickets/search?status=OPEN
```

#### Response

```json
[
  {
    "id": 1,
    "ticketNumber": "TKT-1001",
    "status": "OPEN"
  }
]
```


## Testing Details

### Unit Testing

Service layer testing was implemented using:

- JUnit 5
- Mockito

### User Service Tests

Covered scenarios:

- Create User
- Get User By ID
- Get All Users
- Update User
- Delete User
- User Not Found

### Ticket Service Tests

Covered scenarios:

- Create Ticket
- Get Ticket By ID
- Assign Ticket
- Auto Assign Ticket
- Resolve Ticket
- Close Ticket
- Reopen Ticket
- Search Tickets

### Business Rule Testing

Covered scenarios:

- Invalid Status Transitions
- Assigning Non Support Engineer
- Ticket Already Assigned
- Reopening Non Closed Ticket
- Closing Non Resolved Ticket
- Critical Ticket Resolution Validation

### Test Execution

Run all tests using:

```bash
.\gradlew test
```

Expected output:

```text
BUILD SUCCESSFUL
```

