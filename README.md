<div align="center">

# 🔔 Car-Go: Notification Consumer Service

[![Java](https://img.shields.io/badge/Java-20-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-green.svg)](https://spring.io/projects/spring-boot)
[![Kafka](https://img.shields.io/badge/Apache%20Kafka-2.x%2B-lightgrey.svg)](https://kafka.apache.org/)
[![Spring Mail](https://img.shields.io/badge/Spring%20Mail-Enabled-orange.svg)](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#mail)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**A fault-tolerant microservice dedicated to consuming Kafka events and dispatching real-time email notifications for the Car-Go platform.**

> *Ensuring every user gets their booking confirmation instantly. Reliable communication powered by Kafka and Spring Boot.*

</div>

---

## 🚀 Service Overview

The `notification-consumer-service` is a critical component in the Car-Go microservice architecture. It operates as a **Kafka Consumer**, listening for events from other core services (like the Booking Service). Its primary role is to ensure **Asynchronous and Decoupled** notification delivery, specifically for **Booking Confirmations** via email.

It utilizes **Spring for Apache Kafka** for event consumption and **Spring Boot Starter Mail** for reliable email dispatching via Gmail's SMTP.

---

## ✨ Key Features

- **✅ Asynchronous Processing**: Decouples the booking process from email sending, ensuring the core booking API remains fast.
- **📧 Email Dispatch**: Sends well-formatted, transactional emails (booking ID, summary, timestamp) using `EmailNotificationService`.
- **🔄 Fault Tolerance & Retries**: On email failure (e.g., connection issue), the `RuntimeException` is thrown, which instructs Kafka to **retry** processing the failed message, ensuring no notification is lost.
- **🧩 Event-Driven Architecture**: Consumes events from the dedicated `Booking-Confirmation` Kafka topic.
- **📦 JSON Deserialization**: Automatically converts the incoming JSON Kafka payload into the `BookingConfirmationEvent` Java object using Spring's `JsonDeserializer`.
- **🛠 Health Monitoring**: Includes `spring-boot-starter-actuator` for monitoring the service health.

---

## ⚙️ Component Breakdown

| Component | Package | Role | Details |
| :--- | :--- | :--- | :--- |
| **Consumer** | `BookingConfirmationConsumer` | Listens to Kafka | Uses `@KafkaListener` to read from the `"Booking-Confirmation"` topic. It delegates email sending to the service layer and handles email failure logic. |
| **Event DTO** | `BookingConfirmationEvent` | Data Structure | Represents the Kafka message payload. Contains `bookingId`, `userEmail`, `bookingSummary`, and `timeStamp`. |
| **Service** | `EmailNotificationService` | Business Logic | Autowires `JavaMailSender` to construct and send the `SimpleMailMessage` with all booking details. |
| **Application** | `NotificationConsumerServiceApplication` | Entry Point | Standard Spring Boot Application class. |

---

## 🛠 Project Setup & Configuration

This service requires a running **Kafka Cluster** and **SMTP credentials** for email delivery.

### 1. Prerequisites

- **Java 20** or higher
- **Apache Maven 3.6+**
- **Apache Kafka** cluster running (e.g., on `localhost:9092`)
- A **Gmail/SMTP** account for sending emails.

### 2. Configuration (`application.properties`)

The service is configured to use the following settings. **Note: The provided Gmail password (`dayrkqlhrrdjeuul`) is likely an App Password and should be treated as sensitive data and managed securely (e.g., via environment variables or a secret manager) in production.**

#### Kafka Setup

| Key | Value | Purpose |
| :--- | :--- | :--- |
| `spring.kafka.consumer.group-id` | `notification-dispatchers-v1` | Unique consumer group identifier. |
| `spring.kafka.consumer.bootstrap-servers` | `localhost:9092,...` | List of Kafka brokers. |
| `spring.kafka.consumer.value-deserializer` | `JsonDeserializer` | Configures the deserializer for the message body. |
| `spring.kafka.consumer.properties.spring.json.value.default.type` | `com.carGo...BookingConfirmationEvent` | **Crucial** for successful JSON to POJO conversion. |

#### Email/SMTP Setup

| Key | Value | Purpose |
| :--- | :--- | :--- |
| `spring.mail.host` | `smtp.gmail.com` | SMTP Server address. |
| `spring.mail.port` | `587` | Standard TLS port. |
| `spring.mail.username` | `cargounofficial@gmail.com` | Sender email address. |
| `spring.mail.password` | `dayrkqlhrrdjeuul` | **App-Specific Password** (Highly sensitive). |
| `spring.mail.properties.mail.smtp.starttls.enable` | `true` | Enables secure connection. |

### 3. Build and Run

1.  **Build the Project:**
    ```bash
    mvn clean install
    ```
2.  **Run the Service:**
    ```bash
    java -jar target/notification-consumer-service-0.0.1-SNAPSHOT.jar
    # OR:
    mvn spring-boot:run
    ```
The service will start and immediately connect to Kafka, ready to listen for events on the `Booking-Confirmation` topic.

---

## ⚠️ Fault Tolerance and Retries

The `BookingConfirmationConsumer` implements a critical retry mechanism:

```java
// ... within listenForBookingEvents method
catch (Exception e) {
    // ... logging error
    throw new RuntimeException("Email dispatch failed, initiating Kafka retry.", e);
}

# 🚀 Notification Consumer Service — Car-Go Integration

A **Spring Boot Kafka consumer microservice** for dispatching **email notifications** (Booking Confirmations, Rewards, and more) as part of the **Car-Go ecosystem**.  
This service listens to Kafka topics and sends notifications using Gmail SMTP — fully extensible to SMS, push, or loyalty updates.

---

## 🔗 Integration Details

| Detail | Value |
| :--- | :--- |
| **Kafka Topic** | `Booking-Confirmation` |
| **Consumer Group** | `notification-dispatchers-v1` |
| **Event Type** | `BookingConfirmationEvent (JSON)` |
| **Email Provider** | Gmail SMTP |
| **Security Note** | Use **app passwords** for Gmail; extend with OAuth for production. |

---

## 📨 Event Processing Flow

| Step | Description | Notes |
| :--- | :--- | :--- |
| **1. Consume Event** | Listens to Kafka topic for `BookingConfirmationEvent` (bookingId, userEmail, bookingSummary, timeStamp). | Group ID ensures load balancing. |
| **2. Validate & Log** | Prints event details; validates email format. | Skips invalid events. |
| **3. Send Email** | Constructs and dispatches email via `EmailNotificationService`. | Uses `SimpleMailMessage` for plain text. |
| **4. Handle Success/Failure** | Logs success; on failure, throws `RuntimeException` for Kafka retry. | Retries up to Kafka’s default (configurable). |

---

## 🚀 Notification Types (Extensible)

✅ **Booking Confirmation** – Default email for new bookings.  
🔄 **Future Expansions** – Add events for cancellations, reminders, or loyalty rewards.  
📱 **Multi-Channel** – Ready for SMS (Twilio) or push notifications (Firebase) by extending `NotificationService`.

---

## 🛠 Project Setup & Quick Start

Get the Notification Consumer Service running alongside **Car-Go** and **Kafka**!

### 📋 Prerequisites

- Java 20+  
- Maven 3.6+  
- Apache Kafka 2.8+ (4 brokers)  
- Gmail account with app password (SMTP access)

---

### ⚙️ Build and Run

```bash
git clone https://github.com/your-org/notification-consumer-service.git
cd notification-consumer-service
mvn clean install
mvn spring-boot:run  # Starts on http://localhost:8082

