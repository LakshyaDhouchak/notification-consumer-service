# 📧 Car-Go Notification Consumer Service: Event-Driven Email Dispatcher

<div align="center">
  <img src="https://img.shields.io/badge/Spring_Boot-3.2+-success.svg" alt="Spring Boot 3.2+">
  <img src="https://img.shields.io/badge/Kafka-Event--Driven-blue.svg" alt="Kafka Event-Driven">
  <img src="https://img.shields.io/badge/Service-Microservice-informational.svg" alt="Microservice">
  <img src="https://img-url-placeholder" alt="Email Icon">
  <img src="https://img.shields.io/badge/Fault_Tolerance-Enabled-critical.svg" alt="Fault Tolerance Enabled">
</div>

> A scalable Spring Boot microservice for consuming Kafka events and dispatching email notifications. Seamlessly integrates with Car-Go for **booking confirmations** and **loyalty rewards**, ensuring fault-tolerant, real-time communication!
>
> **Elevate user experience with instant, reliable email alerts. From Kafka events to inboxes—notify smarter, scale faster!**

---

## 🌟 Key Features

| Feature | Description | Implementation Detail |
| :--- | :--- | :--- |
| **📨 Event-Driven Notifications** | Consumes `BookingConfirmationEvent` and `RewardEarnedEvent` from dedicated Kafka topics for real-time processing. | Uses **Spring Kafka** listeners. |
| **✉️ Personalized Email Dispatch** | Sends tailored confirmation and reward emails with dynamic details and timestamps. | Uses **Spring Mail** with **Gmail SMTP** (requires App Password). |
| **🔄 Fault Tolerance & Retries** | Automatic retry mechanism for transient email dispatch failures to prevent data loss. | Throws a `RuntimeException` to trigger **Kafka Retries**. |
| **📊 Scalability** | Supports multiple consumer instances for high throughput and load balancing. | Configured with Kafka consumer group **`notification-dispatchers-v1`**. |
| **🛡️ Security & Validation** | Secure SMTP configuration, input validation for events, and centralized exception handling. | Uses secure configuration properties and validation logic. |
| **🔗 Integration Ready** | Decoupled and extensible for future multi-channel alerts (SMS, push). | Designed to work with the Car-Go Producer and Loyalty Rewards System. |

---

## ⚙️ Event Processing Flow

The service listens to Kafka, processes the event, and ensures delivery or robust retry.

| Step | Description | Notes |
| :--- | :--- | :--- |
| **1. Consume Event** | Listens to Kafka topic (e.g., `Booking-Confirmation`). | Group ID ensures load balancing. |
| **2. Validate & Log** | Prints event details and validates essential fields (e.g., email format). | **Skips invalid events** (logs warning). |
| **3. Send Email** | Constructs and dispatches email via `EmailNotificationService`. | Uses `SimpleMailMessage` for quick delivery. |
| **4. Handle Success/Failure** | Logs success. On failure, throws a `RuntimeException`. | **Forces Kafka retry** to guarantee delivery. |

### 🔗 Integration Details

| Detail | Value |
| :--- | :--- |
| **Kafka Topic (Booking)** | `Booking-Confirmation` |
| **Kafka Topic (Loyalty)** | `Loyalty-Rewards` |
| **Consumer Group** | `notification-dispatchers-v1` |
| **Event Types** | `BookingConfirmationEvent`, `RewardEarnedEvent` (JSON) |
| **Email Provider** | Gmail SMTP |

---

## ⚠️ Exception Handling Strategy

The service utilizes **centralized global exception handling** for robust error management and guaranteed data reliability.

| Exception Type | Action Taken | Reliability Goal |
| :--- | :--- | :--- |
| `RuntimeException` (on email send) | Throws exception to Kafka layer $\rightarrow$ **Triggers Retry**. | Prevents data loss from transient network/SMTP errors. |
| `JsonProcessingException` | Skips processing the event $\rightarrow$ **Logs Warning**. | Prevents service crash from malformed Kafka messages. |
| `MailException` | Logs specific error (e.g., invalid recipient). | Provides detailed debug logging. |

**Sample Critical Error Log:**
