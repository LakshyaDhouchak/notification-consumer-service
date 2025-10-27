package com.carGo.notification_consumer_service.Event;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {
    // define the properties
    private final JavaMailSender mailSender;

    // define the constructor
    public EmailNotificationService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    // define the methord
    public void sendEmail(BookingConfirmationEvent event){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("cargounofficial@gmail.com");
        message.setTo(event.getUserEmail());
        message.setSubject("Car-Go Booking Confirmed! ID: " + event.getBookingId());
        String emailBody = String.format(
            "Hello!\n\nYour Car-Go booking is confirmed.\n" +
            "Details: %s\n" +
            "Booking placed at: %s\n\n" +
            "Thank you for choosing Car-Go!",
            event.getBookingSummary(),
            event.getTimeStamp()
        );
        
        message.setText(emailBody);

        mailSender.send(message);
        System.out.println("Email dispatched to: " + event.getUserEmail());
    }
}
