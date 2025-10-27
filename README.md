# 📧 Car-Go Notification Consumer Service: Event-Driven Email Dispatcher

<div align="center">
  <img src="https://img.shields.io/badge/Spring_Boot-3.2+-success.svg" alt="Spring Boot 3.2+">
  <img src="https://img-url-placeholder" alt="Email Icon">
  <img src="https://img.shields.io/badge/Kafka-Event--Driven-blue.svg" alt="Kafka Event-Driven">
  <img src="https://img.shields.io/badge/Fault_Tolerance-Enabled-critical.svg" alt="Fault Tolerance Enabled">
</div>

> A scalable Spring Boot microservice consuming **Kafka events** to dispatch **real-time email notifications** for **Car-Go** booking confirmations and loyalty rewards. **Notify smarter, scale faster!**

---

## Step 1: ⚙️ Prerequisites & Quick Setup

This outlines what you need to get the service running.

### 1.1. System Requirements

| Requirement | Version | Note |
| :--- | :--- | :--- |
| **Java** | 20+ | Ensure JDK is installed and configured. |
| **Maven** | 3.6+ | Used for building and dependency management. |
| **Apache Kafka** | 2.8+ | Requires a running cluster (4 brokers recommended). |
| **Email** | Gmail Account | Requires a generated **App Password** for SMTP security. |

### 1.2. Build and Run

Get the service started on `http://localhost:8082`.

1.  **Clone the Repository:**
    ```bash
    git clone [https://github.com/your-org/notification-consumer-service.git](https://github.com/your-org/notification-consumer-service.git)
    cd notification-consumer-service
    ```
2.  **Build and Start:**
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

### 1.3. Essential Configuration

Update `src/main/resources/application.properties` before running.

| Area | Key Property | **Default Values (MUST Change!)** |
| :--- | :--- | :--- |
| **Kafka Brokers** | `spring.kafka.consumer.bootstrap-servers` | `localhost:9092,localhost:9094,...` |
| **Consumer Group** | `spring.kafka.consumer.group-id` | `notification-dispatchers-v1` |
| **Email Username** | `spring.mail.username` | `cargounofficial@gmail.com` |
| **Email Password** | `spring.mail.password` | `your-app-password` (Use App Password, not main password) |
| **Server Port** | `server.port` | `8082` |

---

## Step 2: 📨 Core Event Consumption & Dispatch

This defines the service's primary function and integration points.

### 2.1. Kafka Integration Details

The service is configured to be highly available and scalable across multiple instances.

| Detail | Value | Purpose |
| :--- | :--- | :--- |
| **Topic (Booking)** | `Booking-Confirmation` | Primary source for booking-related events. |
| **Topic (Loyalty)** | `Loyalty-Rewards` | Source for user reward achievements. |
| **Consumer Group** | `notification-dispatchers-v1` | Ensures load balancing of events across service instances. |
| **Event Type** | `BookingConfirmationEvent` (JSON) | Core payload containing `bookingId`, `userEmail`, etc. |

### 2.2. Event Processing Flow

| Step | Description | Notes |
| :--- | :--- | :--- |
| **1. Consume Event** | **Spring Kafka Listener** polls the configured topics. | Group ID ensures fault-tolerant consumption. |
| **2. Validate & Log** | Event details are logged to **DEBUG**; email format is validated. | **Skips** processing malformed or invalid events. |
| **3. Send Email** | Event is passed to **`EmailNotificationService`** for construction and dispatch via **Gmail SMTP**. | Uses `SimpleMailMessage` for quick, reliable plain-text communication. |
| **4. Handle Success/Failure** | Logs success. On email failure, throws a **`RuntimeException`**. | **Forces Kafka retry** to guarantee delivery (**Fault Tolerance**). |

---

## Step 3: 🛡️ Fault Tolerance & Exception Handling

A robust strategy ensures no crucial communication is lost due to transient errors.

### 3.1. Centralized Exception Strategy

| Exception Type | Trigger | Action & Outcome |
| :--- | :--- | :--- |
| `RuntimeException` | Transient failure during email dispatch (e.g., SMTP timeout). | **$\rightarrow$ Triggers Kafka Retry** (based on Kafka config). |
| `JsonProcessingException` | Malformed or unparsable JSON received from Kafka. | **$\rightarrow$ Skips Event**, logs as **WARNING**, and moves on. |
| `MailException` | Specific email issue (e.g., invalid recipient address). | $\rightarrow$ Logs detailed error; may trigger retry depending on specific type. |

### 3.2. Reliability Goal

* **No Data Loss:** Failed events are retried by Kafka to ensure delivery.
* **Service Stability:** Malformed events are safely skipped, preventing service crashes.

---

## Step 4: ⭐ Loyalty Rewards System Extension

This feature leverages the service's architecture for immediate user engagement.

### 4.1. Observer Pattern Implementation

The system implements the **Observer Pattern** for loose coupling:

1.  **Car-Go Rewards System (Subject):** Publishes a `RewardEarnedEvent`.
2.  **Notification Service (Observer):** Consumes the event from the **`Loyalty-Rewards`** topic.
3.  **Action:** Dispatches a celebratory, personalized email.

### 4.2. Engagement Activities

| Activity Type | Notification Goal | User Impact |
| :--- | :--- | :--- |
| **Point Earning** | "Congrats! +50 points for your 5th rental." | Drives immediate revisits and activity. |
| **Badge Unlocking** | "You've earned the 'Safe Driver' badge!" | Builds loyalty and personalized experience. |
| **Tier Reaching** | "You're 2 rentals away from Gold tier!" | Encourages repeat business and retention. |

---

## Step 5: 🗺️ Roadmap & Future Scaling

This service is designed for future multi-channel expansion and enhanced monitoring.

### 5.1. Multi-Channel & UX Expansion

| Initiative | Description |
| :--- | :--- |
| **📱 Multi-Channel** | Integrate **SMS via Twilio** or **Push Notifications** (Firebase) for a unified alert system. |
| **🔔 Advanced Templates** | Implement **HTML Email Templates** with branding, dynamic content, and attachments (e.g., receipts). |
| **📅 Scheduled Reminders** | Integrate a scheduler (e.g., Quartz) for pre-booking or return reminders. |

### 5.2. Security and Analytics

| Initiative | Description |
| :--- | :--- |
| **🔐 Security** | Upgrade to **OAuth2** for Gmail/SMTP to enhance credential security. |
| **📊 Analytics** | Track delivery and open rates (via services like SendGrid) and integrate with ELK stack for operational monitoring. |

---

## Step 6: 🤝 Contributing

We welcome contributions! Please follow the standard workflow.

1.  **Fork** the repository and create your feature branch.
2.  **Code & Test:** Add unit tests with JUnit/Mockito and ensure all tests pass (`mvn test`).
3.  **Commit Changes** using **semantic messages** (e.g., `feat: Add Twilio SMS support for notifications 📱`).
4.  **Push & Open a Pull Request (PR)** with details about the feature and test results.
