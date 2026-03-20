package com.example.myapp.service;

import com.example.myapp.model.Booking;
import com.example.myapp.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Send Booking Confirmation Email
    public void sendBookingConfirmation(Booking booking) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(booking.getUser().getEmail());
        message.setSubject("🎉 Booking Confirmed - RentalSystem");
        message.setText(
            "Dear " + booking.getUser().getName() + ",\n\n" +
            "Your booking has been confirmed!\n\n" +
            "Booking Details:\n" +
            "----------------\n" +
            "Item: " + booking.getItem().getName() + "\n" +
            "Category: " + booking.getItem().getCategory() + "\n" +
            "Start Date: " + booking.getStartDate() + "\n" +
            "End Date: " + booking.getEndDate() + "\n" +
            "Total Price: ₹" + booking.getTotalPrice() + "\n" +
            "Status: " + booking.getStatus() + "\n\n" +
            "Thank you for choosing RentalSystem!\n\n" +
            "Regards,\n" +
            "RentalSystem Team"
        );
        mailSender.send(message);
    }

    // Send Payment Success Email
    public void sendPaymentConfirmation(Payment payment) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(payment.getUser().getEmail());
        message.setSubject("💳 Payment Successful - RentalSystem");
        message.setText(
            "Dear " + payment.getUser().getName() + ",\n\n" +
            "Your payment was successful!\n\n" +
            "Payment Details:\n" +
            "----------------\n" +
            "Transaction ID: " + payment.getTransactionId() + "\n" +
            "Item: " + payment.getBooking().getItem().getName() + "\n" +
            "Amount Paid: ₹" + payment.getAmount() + "\n" +
            "Payment Method: " + payment.getPaymentMethod() + "\n" +
            "Payment Status: " + payment.getPaymentStatus() + "\n" +
            "Payment Date: " + payment.getPaymentDate() + "\n\n" +
            "Thank you for choosing RentalSystem!\n\n" +
            "Regards,\n" +
            "RentalSystem Team"
        );
        mailSender.send(message);
    }

    // Send Booking Cancellation Email
    public void sendBookingCancellation(Booking booking) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(booking.getUser().getEmail());
        message.setSubject("❌ Booking Cancelled - RentalSystem");
        message.setText(
            "Dear " + booking.getUser().getName() + ",\n\n" +
            "Your booking has been cancelled.\n\n" +
            "Booking Details:\n" +
            "----------------\n" +
            "Item: " + booking.getItem().getName() + "\n" +
            "Start Date: " + booking.getStartDate() + "\n" +
            "End Date: " + booking.getEndDate() + "\n" +
            "Total Price: ₹" + booking.getTotalPrice() + "\n\n" +
            "If you have any questions, please contact us.\n\n" +
            "Regards,\n" +
            "RentalSystem Team"
        );
        mailSender.send(message);
    }

    // Send Welcome Email
    public void sendWelcomeEmail(String email, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("🎉 Welcome to RentalSystem!");
        message.setText(
            "Dear " + name + ",\n\n" +
            "Welcome to RentalSystem!\n\n" +
            "You can now:\n" +
            "✅ Browse items\n" +
            "✅ Book vehicles, houses, equipment and books\n" +
            "✅ Make secure payments\n\n" +
            "Visit us at: http://localhost:8080\n\n" +
            "Thank you for joining us!\n\n" +
            "Regards,\n" +
            "RentalSystem Team"
        );
        mailSender.send(message);
    }
}