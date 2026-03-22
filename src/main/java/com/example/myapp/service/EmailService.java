package com.example.myapp.service;

import com.example.myapp.model.Booking;
import com.example.myapp.model.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    private void sendEmail(String to, String toName, String subject, String body) {
        try {
            String jsonBody = "{"
                + "\"sender\":{\"name\":\"RentalSystem\",\"email\":\"srivinaygandla@gmail.com\"},"
                + "\"to\":[{\"email\":\"" + to + "\",\"name\":\"" + toName + "\"}],"
                + "\"subject\":\"" + subject + "\","
                + "\"textContent\":\"" + body.replace("\n", "\\n").replace("\"", "\\\"") + "\""
                + "}";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                .header("accept", "application/json")
                .header("api-key", brevoApiKey)
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

            HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
            System.out.println("Email sent! Response: " + response.statusCode());

        } catch (Exception e) {
            System.out.println("Email sending failed: " + e.getMessage());
        }
    }

    // Send Welcome Email
    public void sendWelcomeEmail(String email, String name) {
        String body =
            "Dear " + name + ",\n\n" +
            "Welcome to RentalSystem!\n\n" +
            "You can now:\n" +
            "- Browse items\n" +
            "- Book vehicles, houses, equipment and books\n" +
            "- Make secure payments\n\n" +
            "Visit us at: https://online-rental-system-production.up.railway.app\n\n" +
            "Thank you for joining us!\n\n" +
            "Regards,\n" +
            "RentalSystem Team";
        sendEmail(email, name, "Welcome to RentalSystem!", body);
    }

    // Send Booking Confirmation Email
    public void sendBookingConfirmation(Booking booking) {
        String body =
            "Dear " + booking.getUser().getName() + ",\n\n" +
            "Your booking has been confirmed!\n\n" +
            "Booking Details:\n" +
            "Item: " + booking.getItem().getName() + "\n" +
            "Category: " + booking.getItem().getCategory() + "\n" +
            "Start Date: " + booking.getStartDate() + "\n" +
            "End Date: " + booking.getEndDate() + "\n" +
            "Total Price: Rs." + booking.getTotalPrice() + "\n" +
            "Status: " + booking.getStatus() + "\n\n" +
            "Thank you for choosing RentalSystem!\n\n" +
            "Regards,\n" +
            "RentalSystem Team";
        sendEmail(booking.getUser().getEmail(),
            booking.getUser().getName(),
            "Booking Confirmed - RentalSystem", body);
    }

    // Send Payment Confirmation Email
    public void sendPaymentConfirmation(Payment payment) {
        String body =
            "Dear " + payment.getUser().getName() + ",\n\n" +
            "Your payment was successful!\n\n" +
            "Payment Details:\n" +
            "Transaction ID: " + payment.getTransactionId() + "\n" +
            "Item: " + payment.getBooking().getItem().getName() + "\n" +
            "Amount Paid: Rs." + payment.getAmount() + "\n" +
            "Payment Method: " + payment.getPaymentMethod() + "\n" +
            "Payment Status: " + payment.getPaymentStatus() + "\n\n" +
            "Thank you for choosing RentalSystem!\n\n" +
            "Regards,\n" +
            "RentalSystem Team";
        sendEmail(payment.getUser().getEmail(),
            payment.getUser().getName(),
            "Payment Successful - RentalSystem", body);
    }

    // Send Booking Cancellation Email
    public void sendBookingCancellation(Booking booking) {
        String body =
            "Dear " + booking.getUser().getName() + ",\n\n" +
            "Your booking has been cancelled.\n\n" +
            "Booking Details:\n" +
            "Item: " + booking.getItem().getName() + "\n" +
            "Start Date: " + booking.getStartDate() + "\n" +
            "End Date: " + booking.getEndDate() + "\n" +
            "Total Price: Rs." + booking.getTotalPrice() + "\n\n" +
            "If you have any questions, please contact us.\n\n" +
            "Regards,\n" +
            "RentalSystem Team";
        sendEmail(booking.getUser().getEmail(),
            booking.getUser().getName(),
            "Booking Cancelled - RentalSystem", body);
    }
}