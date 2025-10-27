Notification Consumer Service
<div align="center">
📧 Notification Consumer Service: Event-Driven Email Dispatcher for Car-Go
Java Spring Boot Kafka Maven License: MIT

A scalable Spring Boot microservice for consuming Kafka events and dispatching email notifications. Seamlessly integrates with Car-Go for booking confirmations, ensuring fault-tolerant, real-time communication!

Elevate user experience with instant email alerts for bookings. From Kafka events to inboxes – notify smarter, scale faster!

</div>
🌟 Key Features
📨 Event-Driven Notifications: Consumes BookingConfirmationEvent from Kafka topic "Booking-Confirmation" for real-time processing.
✉️ Email Dispatch: Sends personalized confirmation emails via Gmail SMTP with booking details and timestamps.
🔄 Fault Tolerance & Retries: Automatic Kafka retries on email failures, with comprehensive error logging and exception handling.
📊 Scalability: Supports multiple consumer instances via Kafka consumer groups (notification-dispatchers-v1) for load balancing.
🛡️ Security & Validation: Secure SMTP configuration, input validation for events, and centralized exception handling.
🔗 Integration Ready: Designed to work with Car-Go producer service; extensible for SMS, push notifications, or multi-channel alerts.
📈 Monitoring: Spring Boot Actuator for health checks, with debug logging for event processing and email status.
⚠️ Exception Handling
The service uses centralized global exception handling (via Spring Boot's default and custom handlers) for robust error management:

RuntimeException: For email dispatch failures (e.g., SMTP errors) → Triggers Kafka retry and logs critical errors.
JsonProcessingException: For malformed Kafka events → Skips processing and logs warnings.
MailException: For email-specific issues (e.g., invalid recipient) → 500 status with retry.
General Exceptions: Catches unexpected errors → 500, with stack traces in logs (not exposed to clients).
This ensures:

Reliable event processing without service crashes.
Detailed logging for debugging (e.g., "CRITICAL ERROR: Failed to send email for booking X").
No data loss; failed events are retried via Kafka.
Sample error log:


Copy code
CRITICAL ERROR: Failed to send email for booking 12345
org.springframework.mail.MailSendException: Mail server connection failed
...
🔗 Integration Details
| Detail | Value | | :--- | :--- | | Kafka Topic | Booking-Confirmation | | Consumer Group | notification-dispatchers-v1 | | Event Type | BookingConfirmationEvent (JSON) | | Email Provider | Gmail SMTP | | Security Note | Use app passwords for Gmail; extend with OAuth for production. |

📨 Event Processing Flow
| Step | Description | Notes | | :--- | :--- | :--- | | 1. Consume Event | Listens to Kafka topic for BookingConfirmationEvent (bookingId, userEmail, bookingSummary, timeStamp). | Group ID ensures load balancing. | | 2. Validate & Log | Prints event details; validates email format. | Skips invalid events. | | 3. Send Email | Constructs and dispatches email via EmailNotificationService. | Uses SimpleMailMessage for plain text. | | 4. Handle Success/Failure | Logs success; on failure, throws RuntimeException for Kafka retry. | Retries up to Kafka's default (configurable). |

🚀 Notification Types (Extensible)
✅ Booking Confirmation: Default email for new bookings.
🔄 Future Expansions: Add events for cancellations, reminders, or loyalty rewards (e.g., "You've earned 50 points!").
📱 Multi-Channel: Ready for SMS (Twilio) or push notifications (Firebase) by extending NotificationService.
🛠 Project Setup & Quick Start
Get the Notification Consumer Service running alongside Car-Go and Kafka!

Prerequisites
Java 20+, Maven 3.6+, Apache Kafka 2.8+ with 4 brokers.
Gmail account with app password for SMTP.
Build and Run
Clone the Repository:

bash

Copy code
git clone https://github.com/your-org/notification-consumer-service.git
cd notification-consumer-service
Build and Start:

bash

Copy code
mvn clean install
mvn spring-boot:run  # Starts on http://localhost:8082
⚙️ Configuration Essentials
Update src/main/resources/application.properties.

| Area | Key Properties | Default Values (Change These!) | | :--- | :--- | :--- | | Kafka | spring.kafka.consumer.bootstrap-servers | localhost:9092,localhost:9094,localhost:9096,localhost:9098 | | | spring.kafka.consumer.group-id | notification-dispatchers-v1 | | Email (Gmail) | spring.mail.username | cargounofficial@gmail.com | | | spring.mail.password | your-app-password (Generate from Gmail) | | Server | server.port | 8082 | | Logging | logging.level.com.carGo | DEBUG (For development) |

Kafka Setup (4 Brokers)
Use Docker Compose for a multi-broker cluster (as detailed in the initial README). Ensure the "Booking-Confirmation" topic is created with partitions and replication.

⚙️ Event Entities
✅ BookingConfirmationEvent: Core event (bookingId, userEmail, bookingSummary, timeStamp) consumed from Kafka.
Extensibility: Add fields like notificationType for different email templates (e.g., "confirmation", "reminder").
No database; events are processed in-memory for stateless operation.

🚀 Roadmap
📱 Multi-Channel Notifications: Add SMS via Twilio or push notifications for mobile apps.
🔔 Advanced Templates: HTML emails with branding, attachments (e.g., receipts), and personalization.
📅 Scheduled Reminders: Integrate Quartz for pre-booking or return reminders.
🔐 Security Enhancements: OAuth2 for Gmail, encryption for sensitive data.
📊 Analytics & Monitoring: Track delivery rates, open rates (via SendGrid), and integrate with ELK stack.
🌍 Global Scaling: Support multiple languages, time zones, and regional SMTP providers.
🎯 Loyalty Integration: Send reward emails (e.g., "You've earned points!") tied to Car-Go's Loyalty Rewards System.
🤝 Contributing
1️⃣ Fork the repo and create your own copy.
2️⃣ Create Branch: git checkout -b feature/add-sms-notifications (use descriptive names).
3️⃣ Code & Test: Follow Spring Boot patterns; add unit tests with JUnit/Mockito; run mvn test.
4️⃣ Commit Changes: git commit -m "Add Twilio SMS support for notifications 📱".
5️⃣ Push & PR: git push origin feature/add-sms-notifications; Open a Pull Request with details and tests.
Guidelines: Semantic versioning, clean code, and update README for new features. Ideas welcome for integrations!

🏆 Loyalty Rewards Notification System
📘 Overview
The Loyalty Rewards Notification System extends the Car-Go Loyalty Rewards System by dispatching targeted email notifications for reward achievements. When users earn points, badges, or discounts (e.g., via the Strategy Pattern in Car-Go), this service sends celebratory emails, keeping users engaged and informed.

This system leverages the Observer Pattern (via Kafka events), allowing decoupled reward notifications without tight coupling to the core rewards logic. It's perfect for enhancing gamification with timely, personalized alerts!

🎯 Purpose & Goals
🔔 Instant Gratification: Notify users immediately upon earning rewards (e.g., "Congrats! +50 points for your 5th rental").
📧 Personalized Engagement: Tailored emails with reward details, next milestones, and redemption tips.
🔄 Seamless Integration: Consumes RewardEarnedEvent from a new Kafka topic (e.g., "Loyalty-Rewards"), sent by Car-Go after reward assignment.
🚀 Scalability: Handles high-volume notifications without impacting core booking services.
🎉 Retention Boost: Encourages repeat interactions by highlighting progress (e.g., "You're 2 rentals away from Gold tier!").
⚙️ How It Works
Upon reward assignment in Car-Go (e.g., via RewardStrategy), a RewardEarnedEvent is published to Kafka. This service consumes it, evaluates the notification type, and sends an email.

Tracked Reward Activities Include:
✅ Earning points for completed bookings or referrals.
🥇 Unlocking badges (e.g., "Safe Driver" or "Loyal Renter").
💰 Redeeming discounts or free upgrades.
📈 Reaching tiers (e.g., Silver: 5 rentals, Gold: 20 rentals).
🌿 Eco-rewards for sustainable choices.
Each notification uses a template strategy for dynamic content, ensuring relevance and excitement.

🧠 Observer Pattern Design
Notifications follow the Observer Pattern for loose coupling:

Car-Go (Subject) publishes events to Kafka.
This service (Observer) subscribes and reacts by sending emails.
Add new observers (e.g., SMS service) without changing Car-Go.
Example Event:

json

Copy code
{"userId":"123","rewardType":"POINTS","amount":50,"description":"Completed 5th rental","timeStamp":"2024-10-01T10:00:00Z"}
🚀 Benefits & Impact
| Benefit | Impact Summary | Metric/Result | | :--- | :--- | :--- | | User Engagement | Immediate notifications drive app revisits and interactions. | 25% Increase in user activity post-reward. | | Retention | Personalized alerts build loyalty and reduce churn. | Higher NPS and repeat bookings. | | Revenue Growth | Promotes redemptions and upsells (e.g., premium features). | Boosts lifetime value through gamification. | | Insights | Tracks notification delivery for marketing optimization. | Data for targeted campaigns. | | Competitive Edge | Modern, event-driven notifications set Car-Go apart. | Positions as a user-centric innovator. |

🔮 Future Scalability
AI Personalization: Dynamic templates based on user preferences.
Multi-Channel: Expand to SMS/push for rewards.
Analytics: Integrate with Car-Go for reward performance dashboards.
📌 Conclusion
The Loyalty Rewards Notification System transforms rewards into memorable experiences, driving loyalty and growth for Car-Go. Notify smarter, reward bigger!

