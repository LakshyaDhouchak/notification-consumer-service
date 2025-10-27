<div align="center">
  
# 📧  **🚀 Event-Driven Email Dispatcher**

<div align="center">
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-success.svg" alt="Spring Boot 3.x">
  <img src="https://img.shields.io/badge/Kafka-Event--Driven-blue.svg" alt="Kafka Event-Driven">
  <img src="https://img.shields.io/badge/Fault_Tolerance-Enabled-critical.svg" alt="Fault Tolerance Enabled">
  <img src="https://img.shields.io/badge/Service-Microservice-informational.svg" alt="Microservice">
</div>

> A **scalable Spring Boot microservice** for consuming Kafka events and dispatching email notifications. Seamlessly integrates with Car-Go for booking confirmations, ensuring **fault-tolerant, real-time communication!**
>
> Elevate user experience with instant email alerts for bookings. From Kafka events to inboxes – **notify smarter, scale faster!**

</div>
---

## 🌟 Key Features

| Feature | Description | Implementation Detail |
| :--- | :--- | :--- |
| **📨 Event-Driven Notifications** | Consumes `BookingConfirmationEvent` from Kafka topic **`Booking-Confirmation`** for real-time processing. | Uses Spring Kafka listeners. |
| **✉️ Email Dispatch** | Sends personalized confirmation emails via **Gmail SMTP** with booking details and timestamps. | Utilizes **Spring Mail** (`SimpleMailMessage`) for plain text reliability. |
| **🔄 Fault Tolerance & Retries** | Automatic Kafka retries on email failures, with comprehensive error logging. | Throwing a **`RuntimeException`** triggers the configurable Kafka retry mechanism. |
| **📊 Scalability** | Supports multiple consumer instances for robust load balancing. | Uses Kafka consumer group **`notification-dispatchers-v1`**. |
| **🛡️ Security & Validation** | Secure SMTP configuration, input validation for events, and centralized exception handling. | Uses **App Passwords** for Gmail; extensible to OAuth2. |
| **📈 Monitoring** | Provides health checks and detailed operational metrics. | Enabled via **Spring Boot Actuator** and debug logging. |

---

## ⚠️ Exception Handling Strategy

The service uses **centralized global exception handling** for robust error management, ensuring: **Reliable event processing**, **Detailed logging**, and **No data loss** (failed events are retried via Kafka).

| Exception Type | Action Taken | Result |
| :--- | :--- | :--- |
| **`RuntimeException`** | For email dispatch failures (e.g., SMTP errors) $\rightarrow$ **Triggers Kafka retry** and logs critical errors. | **Guaranteed Delivery**. |
| **`JsonProcessingException`** | For malformed Kafka events $\rightarrow$ **Skips processing** and logs warnings. | **Service Stability**. |
| **`MailException`** | For email-specific issues (e.g., invalid recipient) $\rightarrow$ Logs critical error with stack trace. | **Auditability** $\text{/}$ **Retry**. |

**Sample Error Log:**
```bash

CRITICAL ERROR: Failed to send email for booking 12345
org.springframework.mail.MailSendException: Mail server connection failed...

```
---

# 📨 Notification Consumer Service

The Notification Consumer Service is a dedicated microservice responsible for processing booking confirmation events from **Apache Kafka** and dispatching corresponding email notifications. It employs a robust **no-drop strategy** to ensure guaranteed email delivery through Kafka's built-in retry mechanism.

## ⚙️ Event Processing Flow

The consumer follows a critical four-step flow for every event:

1.  **Consume Event:** The **`BookingConfirmationConsumer`** listens to the **`Booking-Confirmation`** topic.
2.  **Deserialize:** The raw message is deserialized into a **`BookingConfirmationEvent`** Java object.
3.  **Dispatch:** The event is passed to the **`EmailNotificationService`** for sending the email.
4.  **Handle Failure:** If the email dispatch fails (e.g., SMTP error), a **`RuntimeException`** is thrown. This signals the Kafka container to **retry** the message delivery, ensuring fault tolerance.

---

## 🛠 Project Setup & Quick Start

Get the notification engine listening for events in minutes!

### 1. Prerequisites 📋

Ensure you have the following installed and accessible:

* **Java 20+**
* **Apache Maven 3.6+**
* An accessible **Apache Kafka** cluster (default configuration points to `localhost:9092`).
* A dedicated **SMTP account** (e.g., Gmail with a secure **App Password**).

### 2. Key Configuration Points (`application.properties`) ⚙️

Configuration is vital for connectivity and data processing. Review and update the following properties in `src/main/resources/application.properties`:

| Section | Key Property | Value Example | Purpose |
| :--- | :--- | :--- | :--- |
| **Kafka** | `spring.kafka.consumer.group-id` | `notification-dispatchers-v1` | Unique consumer group for load balancing. |
| **Kafka** | `spring.kafka.consumer.bootstrap-servers` | `localhost:9092,...` | List of Kafka broker addresses. |
| **Kafka** | `spring.json.value.default.type` | `com.carGo...BookingConfirmationEvent` | **Crucial:** Specifies the Java class for JSON deserialization. |
| **Email** | `spring.mail.username` | `cargounofficial@gmail.com` | Sender email address. |
| **Email** | `spring.mail.password` | `dayrkqlhrrdjeuul` | **SMTP Secret:** *Must* be an **App Password** for Gmail or equivalent. |

### 3. Build and Run 🚀

Follow these steps to build and start the service:

1.  **Clone the Repository:**
    ```bash
    git clone [https://github.com/LakshyaDhouchak/notification-consumer-service](https://github.com/LakshyaDhouchak/notification-consumer-service)
    cd notification-consumer-service
    ```
2.  **Build the Project:**
    ```bash
    mvn clean install
    ```
3.  **Start the Service:**
    ```bash
    mvn spring-boot:run # Starts on http://localhost:8082
    ```
The service will immediately establish connections to Kafka, ready to consume events from the **`Booking-Confirmation`** topic.

---

## ⚠️ Fault Tolerance: The No-Drop Strategy

We prioritize **guaranteed delivery** of essential booking confirmations. The service implements an explicit failure handling mechanism in the **`BookingConfirmationConsumer`** to trigger Kafka retries instead of losing messages.

```java
// Logic inside BookingConfirmationConsumer.java (Failure Handling)

catch (Exception e) {
    System.err.println("CRITICAL ERROR: Failed to send email for booking " + event.getBookingId());
    e.printStackTrace();
    
    // **This is the key:** Throwing a RuntimeException signals Kafka to trigger a retry.
    throw new RuntimeException("Email dispatch failed, initiating Kafka retry.", e); 
}
```
# ⚙️ Configuration Essentials

Edit `src/main/resources/application.properties` to configure the notification service.

| Area | Key Properties | Default Values (Change These!) |
| :--- | :--- | :--- |
| **Kafka** | `spring.kafka.consumer.bootstrap-servers` | `localhost:9092,localhost:9094,localhost:9096,localhost:9098` |
|  | `spring.kafka.consumer.group-id` | `notification-dispatchers-v1` |
| **Email (Gmail)** | `spring.mail.username` | `cargounofficial@gmail.com` |
|  | `spring.mail.password` | `your-app-password` |
| **Server** | `server.port` | `8082` |
| **Logging** | `logging.level.com.carGo` | `DEBUG` |

---

## 🧩 Kafka Setup (4 Brokers)

Use **Docker Compose** for a multi-broker Kafka cluster (as defined in your Car-Go project).  
Ensure the topic **`Booking-Confirmation`** is created with proper partitions and replication.

---

## 🧠 Event Entities

✅ **BookingConfirmationEvent** – Core event example:

```json
{
  "bookingId": "B123",
  "userEmail": "user@example.com",
  "bookingSummary": "Hyundai Venue Rental - 2 days",
  "timeStamp": "2024-09-28T10:00:00Z"
}
```

---

## 📌 Conclusion: A Game-Changing Strategy

The core game-changing strategy for a CNS is to shift from being a mere delivery mechanism (just sending an email or push) to being a personalized, contextual intelligence layer that enhances the customer experience.

---

## 👋 Get in Touch & Contribute!

We're an open-source project and welcome community support and contributions!

| Resource | Link/Details |
| :--- | :--- |
| **Repo** | `https://github.com/LakshyaDhouchak/notification-consumer-service` |
| **Issues** | **`https://github.com/LakshyaDhouchak/notification-consumer-service/issues`** |
| **Email** | `lakshya10171@gmail.com` |

### 📄 License

This project is **MIT licensed**. See the `LICENSE` file for full details. Feel free to fork, modify, and rock!

**Stars & Forks:** Help us grow – star this repo! ⭐

<p align="center">
Built with ❤️ by Lakshya – Happy Notifying!!
</p>

***
*Updated: October 2024 | Version: 0.0.1-SNAPSHOT*

