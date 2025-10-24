package com.carGo.notification_consumer_service.Event;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BookingConfirmationConsumer {

    
    private static final String BOOKING_TOPIC = "Booking-Confirmation"; 
    
    private final EmailNotificationService emailService;

    public BookingConfirmationConsumer(EmailNotificationService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(
        topics = BOOKING_TOPIC, 
        groupId = "${spring.kafka.consumer.group-id}" 
    )
    public void listenForBookingEvents(BookingConfirmationEvent event) {
        
        System.out.println("--- Event Received ---");
        System.out.println("Booking ID: " + event.getBookingId());
        System.out.println("Recipient: " + event.getUserEmail());

        try {
            emailService.sendEmail(event);
            System.out.println("Successfully processed and sent confirmation for ID: " + event.getBookingId());

        } catch (Exception e) {
            System.err.println("CRITICAL ERROR: Failed to send email for booking " + event.getBookingId());
            e.printStackTrace();
            throw new RuntimeException("Email dispatch failed, initiating Kafka retry.", e);
        }
    }
}
