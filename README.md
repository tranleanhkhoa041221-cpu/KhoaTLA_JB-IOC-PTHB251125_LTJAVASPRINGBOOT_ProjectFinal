# 🎓 Internship Management System
> Backend REST API — Java Spring Boot · PostgreSQL · JWT · MapStruct

A full-featured backend system for managing student internships at an educational institution. Supports three roles (Admin, Mentor, Student), internship phase management, mentor assignment, weighted evaluation criteria, and assessment result tracking.

---

## 📋 Table of Contents
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Domain Model](#-domain-model)
- [API Overview](#-api-overview)
- [Getting Started](#-getting-started)
- [Environment Variables](#-environment-variables)

---

## ✨ Features

| Module | Description |
|---|---|
| **Authentication** | JWT-based login (HS256, 24h expiry), role-based access control |
| **User Management** | Create/update Admin, Mentor, Student accounts |
| **Internship Phases** | Define phases with start/end dates |
| **Mentor Assignment** | Assign students to mentors per phase (InternshipAssignment) |
| **Evaluation Criteria** | Define weighted scoring criteria per assessment round |
| **Assessment Results** | Mentors score students; students view their own results |
| **Pagination & Search** | Paginated, filtered, sortable list endpoints |
| **Email Notifications** | Gmail SMTP for account notifications (Spring Mail) |
| **Image Upload** | Cloudinary integration for profile photos |

---

## 🛠️ Tech Stack

| Category | Technology |
|---|---|
| **Language** | Java 17 |
| **Framework** | Spring Boot 4.0.6 |
| **Build Tool** | Gradle |
| **Database** | PostgreSQL |
| **ORM** | Spring Data JPA + Hibernate |
| **Security** | Spring Security + JWT (JJWT 0.11.5, HS256) |
| **DTO Mapping** | MapStruct 1.5.5 |
| **Validation** | Jakarta Bean Validation (`@Valid`) |
| **Email** | Spring Mail + Gmail SMTP (STARTTLS) |
| **Image Storage** | Cloudinary |
| **Utilities** | Lombok |

---

## 🏗️ Architecture

```
┌──────────────────────────────────────────────────────┐
│                   REST API (HTTP)                    │
├──────────────────────────────────────────────────────┤
│  Controller Layer  (10 REST Controllers)             │
│  → AuthController, StudentController, MentorController│
│  → InternshipPhaseController, AssignmentController   │
│  → AssessmentRoundController, CriteriaController     │
│  → RoundCriteriaController, AssessmentResultController│
├──────────────────────────────────────────────────────┤
│  Service Layer  (Interface + Implementation)         │
├──────────────────────────────────────────────────────┤
│  Repository Layer  (Spring Data JPA Repositories)    │
├──────────────────────────────────────────────────────┤
│  Entity Layer  (9 JPA Entities)                      │
└──────────────────────────────────────────────────────┘
            │                        │
     [PostgreSQL DB]         [Cloudinary / Gmail]
```

**Security flow:**
```
Request → JwtAuthenticationFilter → SecurityContext → Controller
                ↑
         JwtService (validate HS256 token, extract username + role)
```

---

## 🗂️ Domain Model

```
User ──1:1── Student
User ──1:1── Mentor

InternshipPhase ──1:N── InternshipAssignment ──N:1── Student
                                              ──N:1── Mentor
InternshipPhase ──1:N── AssessmentRound ──1:N── RoundCriteria ──N:1── EvaluationCriteria

InternshipAssignment ──1:N── AssessmentResult
AssessmentRound      ──1:N── AssessmentResult
EvaluationCriteria   ──1:N── AssessmentResult
```

**9 Entities:** `User`, `Student`, `Mentor`, `InternshipPhase`, `InternshipAssignment`, `AssessmentRound`, `EvaluationCriteria`, `RoundCriteria`, `AssessmentResult`

---

## 🌐 API Overview

| Method | Endpoint | Roles | Description |
|---|---|---|---|
| `POST` | `/api/auth/login` | Public | Login, receive JWT token |
| `GET` | `/api/auth/me` | All | Get current user info |
| `GET/POST/PUT` | `/api/users/**` | ADMIN | Manage user accounts |
| `GET/POST/PUT` | `/api/students/**` | ADMIN, MENTOR, STUDENT | Student CRUD (paginated) |
| `GET/POST/PUT` | `/api/mentors/**` | ADMIN, STUDENT | Mentor CRUD |
| `GET/POST/PUT/DELETE` | `/api/internship-phases/**` | ADMIN; GET: All | Phase management |
| `GET/POST/PUT/DELETE` | `/api/internship-assignments/**` | ADMIN; GET: All | Assignment management |
| `GET/POST/PUT/DELETE` | `/api/assessment-rounds/**` | ADMIN; GET: All | Round management |
| `GET/POST/PUT/DELETE` | `/api/evaluation-criteria/**` | ADMIN; GET: All | Criteria management |
| `GET/POST/PUT/DELETE` | `/api/round-criteria/**` | ADMIN; GET: All | Weighted criteria per round |
| `GET` | `/api/assessment-results/**` | ADMIN, MENTOR, STUDENT | View results |
| `POST/PUT/DELETE` | `/api/assessment-results/**` | MENTOR only | Score students |

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- PostgreSQL 15+
- Gradle

### Setup

```bash
# 1. Clone the repo
git clone https://github.com/tranleanhkhoa041221-cpu/KhoaTLA_JB-IOC-PTHB251125_LTJAVASPRINGBOOT_ProjectFinal.git
cd KhoaTLA_JB-IOC-PTHB251125_LTJAVASPRINGBOOT_ProjectFinal

# 2. Create PostgreSQL database
psql -U postgres -c "CREATE DATABASE internship_db;"

# 3. Configure environment (see section below)
cp src/main/resources/application.properties.example src/main/resources/application.properties
# Edit application.properties with your credentials

# 4. Run
./gradlew bootRun
```

API will be available at: `http://localhost:8080`

---

## ⚙️ Environment Variables

Configure in `src/main/resources/application.properties`:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5433/internship_db
spring.datasource.username=postgres
spring.datasource.password=your_password

# JWT
app.jwt.secret=your_jwt_secret_key
app.jwt.expiration=86400000

# Email (Gmail)
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password

# Cloudinary
cloudinary.cloud-name=your_cloud_name
cloudinary.api-key=your_api_key
cloudinary.api-secret=your_api_secret
```

---

## 👤 Author

**Trần Lê Anh Khoa** — Backend Developer (Java Spring Boot)  
📧 tranleanhkhoa041221@gmail.com
