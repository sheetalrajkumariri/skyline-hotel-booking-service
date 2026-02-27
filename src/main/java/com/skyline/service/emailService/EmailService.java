package com.skyline.service.emailService;

import com.skyline.entity.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.from}")
    private String fromEmail;

    public void sendBookingConfirmation(String to, Booking booking) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("SkyLine Booking Confirmation");

        message.setText(
                "Dear Customer,\n\n" +
                        "Your booking has been confirmed.\n" +
                        "Booking ID: " + booking.getId() + "\n" +
                        "Check-in: " + booking.getCheckInDate() + "\n" +
                        "Check-out: " + booking.getCheckOut() + "\n" +
                        "Status: " + booking.getStatus() + "\n\n" +
                        "Thank you for choosing SkyLine!"
        );

        mailSender.send(message);
    }

    public void sendUserCreationEmail(String to, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Welcome to SkyLine Service");

        message.setText(
                "Dear " + name + ",\n\n" +
                        "Your SkyLine account has been created successfully.\n" +
                        "You can now login using your email.\n\n" +
                        "Thank you for joining SkyLine!"
        );

        mailSender.send(message);
    }

}
