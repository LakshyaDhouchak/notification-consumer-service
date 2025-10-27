<div align="center">

# ⚡ Car-Go: Real-Time Notification Engine (Consumer Service)

[![Java](https://img.shields.io/badge/Java-20+-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-green.svg)](https://spring.io/projects/spring-boot)
[![Kafka](https://imgiums.io/badge/Apache%20Kafka-Reliable%20Events-lightgrey.svg)](https://kafka.apache.org/)
[![Spring Mail](https://img.shields.io/badge/Email%20Dispatch-Spring%20Mail-orange.svg)](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#mail)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**The dedicated, highly resilient microservice ensuring zero dropped notifications. We consume Kafka events and instantly dispatch transactional emails for the Car-Go platform.**

> *Decoupled, dependable, and designed for scale. When a booking happens, we make sure the customer knows—instantly.*

</div>

***

## 🎯 Mission Statement & Overview

The `notification-consumer-service` is the dedicated communication layer of the Car-Go architecture. It completely **decouples** the core booking logic from the time-consuming process of sending emails. This ensures the booking API is blazing fast and provides **ultimate resilience** against service outages through **Kafka's built-in retry mechanism**.

It uses **Spring for Apache Kafka** to process events and **Spring Boot Starter Mail** for reliable email dispatching via Gmail's SMTP.

***

## ✨ Core Technology & Features

| Feature | Component | Benefit to the System |
| :--- | :--- | :--- |
| **Event Ingestion** | `BookingConfirmationConsumer` | Uses **Spring Kafka** to passively consume events, maximizing throughput and ensuring **Asynchronous Processing**. |
| **Guaranteed Delivery** | `RuntimeException` Handler | **Fault Tolerance:** Any temporary failure (e.g., SMTP timeout) triggers a **Kafka retry**, preventing lost notifications. |
| **Data Mapping** | `JsonDeserializer` | Seamlessly converts raw JSON messages into the typed `BookingConfirmationEvent` DTO. |
| **Extensibility** | `EmailNotificationService` | Ready to be extended for other channels like SMS (Twilio) or push notifications (Firebase). |
| **Monitoring** | `spring-boot-starter-actuator` | Built-in endpoints for instant health checks and service monitoring. |

***

## 🗂 Project Structure & Flow

The service maintains a concise, highly focused structure, adhering to Spring Boot best practices:
