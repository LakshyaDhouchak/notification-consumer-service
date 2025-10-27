
# 📧 Car-Go Notification Consumer Service: Event-Driven Email Dispatcher

<div align="center">
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-success.svg" alt="Spring Boot 3.x">
  <img src="https://img.shields.io/badge/Kafka-Event--Driven-blue.svg" alt="Kafka Event-Driven">
  <img src="https://img.shields.io/badge/Fault_Tolerance-Enabled-critical.svg" alt="Fault Tolerance Enabled">
  <img src="https://img.shields.io/badge/Service-Microservice-informational.svg" alt="Microservice">
</div>

> A **scalable Spring Boot microservice** for consuming Kafka events and dispatching email notifications. Seamlessly integrates with Car-Go for booking confirmations, ensuring **fault-tolerant, real-time communication!**
>
> Elevate user experience with instant email alerts for bookings. From Kafka events to inboxes – **notify smarter, scale faster!**

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
