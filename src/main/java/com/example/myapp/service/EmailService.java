package com.example.myapp.service;

import com.example.myapp.model.Booking;
import com.example.myapp.model.Payment;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    private void sendEmail(String to, String subject, String body) {
        Email from = new Email("srivinaygandla@gmail.com");
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
            System.out.println("Email sent to: " + to);
        } catch (IOException e) {
            System.out.println("Email sending failed: " + e.getMessage());
        }
    }

    // Send Welcome Email
    public void sendWelcomeEmail(String email, String name) {
        String body =
            "Dear " + name + ",\n\n" +
            "Welcome to RentalSystem!\n\n" +
            "You can now:\n" +
            "✅ Browse items\n" +
            "✅ Book vehicles, houses, equipment and books\n" +
            "✅ Make secure payments\n\n" +
            "Visit us at: https://online-rental-system-production.up.railway.app\n\n" +
            "Thank you for joining us!\n\n" +
            "Regards,\n" +
            "RentalSystem Team";
        sendEmail(email, "🎉 Welcome to RentalSystem!", body);
    }

    // Send Booking Confirmation Email
    public void sendBookingConfirmation(Booking booking) {
        String body =
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
            "RentalSystem Team";
        sendEmail(booking.getUser().getEmail(),
            "🎉 Booking Confirmed - RentalSystem", body);
    }

    // Send Payment Confirmation Email
    public void sendPaymentConfirmation(Payment payment) {
        String body =
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
            "RentalSystem Team";
        sendEmail(payment.getUser().getEmail(),
            "💳 Payment Successful - RentalSystem", body);
    }

    // Send Booking Cancellation Email
    public void sendBookingCancellation(Booking booking) {
        String body =
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
            "RentalSystem Team";
        sendEmail(booking.getUser().getEmail(),
            "❌ Booking Cancelled - RentalSystem", body);
    }
}