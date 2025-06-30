# 🎟️ Event Ticket Token System (Java + Vert.x + MongoDB)

A simple RESTful backend system built using **Java**, **Vert.x**, and **MongoDB**, designed to manage event registrations and token bookings with email notifications via **SMTP**.

---

## 🚀 Features

- ✅ User Registration with email and auto-generated password
- ✅ User Authentication using email & password
- ✅ View list of upcoming events with available token counts
- ✅ Book a token for a specific event (reduces availability)
- ✅ Unique alphanumeric token generation for bookings
- ✅ Email notifications for registration & booking confirmation
- ✅ MongoDB integration for storing users, events, bookings
- ✅ Clean REST API design using Vert.x

---

## 🛠️ Tech Stack

- **Java 17**
- **Vert.x (Core, Web, MongoDB Client)**
- **MongoDB**
- **SMTP (Jakarta Mail)**
- **Postman (for testing)**

---

## 📁 Project Structure

src/
└── main/
└── java/
└── org/example/eventsystem/
├── MainVerticle.java
├── controller/
│ ├── AuthController.java
│ ├── EventController.java
│ └── BookingController.java
├── service/
│ ├── AuthService.java
│ ├── EventService.java
│ └── BookingService.java
├── util/
│ ├── EmailUtil.java
│ ├── PasswordUtil.java
│ ├── TokenUtil.java
│ └── ConfigLoader.java
└── model/
├── User.java
├── Event.java
└── Booking.java

## 📬 API Endpoints

| Method | Endpoint         | Description                          |
|--------|------------------|--------------------------------------|
| POST   | `/register`      | Register user, generate & email password |
| POST   | `/login`         | Authenticate user credentials        |
| POST   | `/addEvent`      | Add a new event                      |
| GET    | `/events`        | List all events                      |
| POST   | `/book`          | Book token for an event              |

---

## 📧 SMTP Setup

In `EmailUtil.java`, configure your SMTP credentials:
```java
String username = "your-email@gmail.com";
String password = "your-app-password"; // NOT your Gmail password

✅ Tip: Use Mailtrap or Gmail App Password if you're using Gmail.

🔧 Run the Project
Clone the repo

Set MongoDB connection in ConfigLoader.java

Build the project:
mvn clean package

Run it:
java -jar target/Day11_EventTicketSystem-1.0-SNAPSHOT.jar


🧪 Testing with Postman
1. Register
POST /register

{ "name": "Praveena", "email": "24mcab44@kristujayanti.com" }

2. Login
POST /login

3. Add Event
POST /addEvent

4. View Events
GET /events

5. Book Token
POST /book

✨ Author
Praveena R (Kristu Jayanti College)
MCA Student – Building real-world Java + MongoDB applications
GitHub: @Praveena23-2003

