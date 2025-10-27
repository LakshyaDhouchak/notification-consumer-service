<div align="center">

# 📧 Notification Consumer Service  
### Event-Driven Email Dispatcher for Car-Go  

[![Java](https://img.shields.io/badge/Java-20+-red?logo=java)](https://www.oracle.com/java/)  
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)  
[![Kafka](https://img.shields.io/badge/Apache%20Kafka-Event%20Driven-black?logo=apachekafka)](https://kafka.apache.org/)  
[![Maven](https://img.shields.io/badge/Maven-Build%20Tool-orange?logo=apachemaven)](https://maven.apache.org/)  
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)  

---

A scalable **Spring Boot microservice** for consuming Kafka events and dispatching email notifications.  
Seamlessly integrates with **Car-Go** for booking confirmations, ensuring fault-tolerant, real-time communication!

> Elevate user experience with instant email alerts for bookings.  
> From Kafka events to inboxes – notify smarter, scale faster!

</div>

---

## 🌟 Key Features

- 📨 **Event-Driven Notifications:**  
  Consumes `BookingConfirmationEvent` from Kafka topic **"Booking-Confirmation"** for real-time processing.

- ✉️ **Email Dispatch:**  
  Sends personalized confirmation emails via **Gmail SMTP** with booking details and timestamps.

- 🔄 **Fault Tolerance & Retries:**  
  Automatic Kafka retries on email failures, with comprehensive error logging and exception handling.

- 📊 **Scalability:**  
  Supports multiple consumer instances via Kafka consumer groups (`notification-dispatchers-v1`) for load balancing.

- 🛡️ **Security & Validation:**  
  Secure SMTP configuration, input validation for events, and centralized exception handling.

- 🔗 **Integration Ready:**  
  Designed to work with Car-Go producer service; extensible for SMS, push notifications, or multi-channel alerts.

- 📈 **Monitoring:**  
  Spring Boot Actuator for health checks, with debug logging for event processing and email status.

---

## ⚠️ Exception Handling

Centralized global exception handling ensures robust error management:

| Exception Type | Scenario | Handling |
| :--- | :--- | :--- |
| `RuntimeException` | Email dispatch failures (SMTP issues) | Triggers Kafka retry & logs critical errors |
| `JsonProcessingException` | Malformed Kafka events | Skips event, logs warning |
| `MailException` | Email-specific issues (invalid recipient, etc.) | Returns 500 & retries |
| `Exception` | Unexpected errors | Logs stack trace, ensures service stability |

✅ **Benefits:**
- Reliable event processing without crashes  
- Detailed error logs for debugging  
- No data loss – failed events retried via Kafka  

**Sample Error Log:**
